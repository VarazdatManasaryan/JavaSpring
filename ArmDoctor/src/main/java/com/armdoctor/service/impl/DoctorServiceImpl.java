package com.armdoctor.service.impl;

import com.armdoctor.dto.requestdto.DoctorDTO;
import com.armdoctor.dto.responsedto.DoctorResponseDTO;
import com.armdoctor.enums.Status;
import com.armdoctor.exceptions.ApiException;
import com.armdoctor.exceptions.ResourceAlreadyExistException;
import com.armdoctor.exceptions.DoctorNotFoundException;
import com.armdoctor.exceptions.DoctorValidationException;
import com.armdoctor.model.DoctorEntity;
import com.armdoctor.model.HospitalEntity;
import com.armdoctor.repository.DoctorRepository;
import com.armdoctor.repository.HospitalRepository;
import com.armdoctor.service.DoctorService;
import com.armdoctor.util.ArmDoctorMailSender;
import com.armdoctor.util.TokenGenerate;
import com.armdoctor.util.DoctorValidation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private HospitalRepository hospitalRepository;

    @Autowired
    private ArmDoctorMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public DoctorEntity createUser(DoctorDTO dto) throws ApiException {

        //DoctorValidation.validateFields(dto);
        //DoctorValidation.validatePassword(dto.getPassword());
        validateDuplicate(dto);

        String verifyCode = TokenGenerate.generateVerifyCode();

        DoctorEntity doctorEntity = new DoctorEntity();
        doctorEntity.setId(0);
        doctorEntity.setName(dto.getName());
        doctorEntity.setSurname(dto.getSurname());
        doctorEntity.setYear(dto.getYear());
        doctorEntity.setEmail(dto.getEmail());
        doctorEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        doctorEntity.setVerifyCode(verifyCode);
        doctorEntity.setStatus(Status.INACTIVE);
        doctorEntity.setRole(dto.getRole());
        doctorEntity.setProfession(dto.getProfession());
        doctorEntity.setWorkTime(dto.getWorkTime());

        Set<HospitalEntity> set = new HashSet<>();

        for (String x : dto.getHospitals()) {
            HospitalEntity hospital = hospitalRepository.getByName(x);
            set.add(hospital);
        }
        doctorEntity.setHospitals(set);

        try {
            doctorRepository.save(doctorEntity);
        } catch (Exception e) {
            throw new ApiException("Problem during saving of user");
        }

        mailSender.sendEmail(dto.getEmail(), "Your verify code", "Your verify code " + verifyCode);

        return doctorEntity;
    }

    @Override
    public List<DoctorEntity> getByUsername(String email) throws ApiException {

        List<DoctorEntity> entityList = null;
        try {
            entityList = doctorRepository.getByEmail(email);
        } catch (Exception e) {
            throw new ApiException("Problem during getting of user");
        }
        return entityList;
    }

    @Override
    public List<DoctorEntity> getAll() throws ApiException {

        List<DoctorEntity> doctorEntities = null;

        try {
            doctorEntities = doctorRepository.findAll();
        } catch (Exception e) {
            throw new ApiException("Problem during getting doctors");
        }
        return doctorEntities
                .stream()
                .filter(u -> u.getName() != null && !u.getName().equals(HospitalServiceImpl.NAME))
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorResponseDTO> getByProfession(String profession) throws ApiException {

        List<DoctorEntity> dtoList = null;
        try {
            dtoList = doctorRepository.getByProfession(profession);
        } catch (Exception e) {
            throw new ApiException("Problem during getting Doctors");
        }
        if (dtoList.isEmpty()) {
            throw new DoctorNotFoundException("Doctor not found with given details");
        }
        return new ObjectMapper().convertValue(dtoList, new TypeReference<List<DoctorResponseDTO>>() {
        });
    }

    @Override
    public DoctorEntity verifyUser(String email, String verifyCode) throws ApiException {

        DoctorEntity doctorEntity = null;
        try {
            doctorEntity = doctorRepository.getByEmailAndVerifyCode(email, verifyCode);
            if (doctorEntity == null) {
                throw new DoctorValidationException("Wrong verify code " + verifyCode);
            }
            doctorEntity.setStatus(Status.ACTIVE);
            doctorEntity.setVerifyCode(null);
            doctorRepository.save(doctorEntity);
        } catch (Exception e) {
            throw new ApiException("Problem during verifying user");
        }
        return doctorEntity;
    }

    @Override
    public DoctorEntity changePassword(String oldPassword, String newPassword, String confirmPassword, String email) throws ApiException {

        DoctorEntity doctorEntity = null;
        DoctorValidation.validatePassword(newPassword);

        if (!newPassword.equals(confirmPassword)) {
            throw new DoctorValidationException("Passwords don't match");
        }
        try {
            doctorEntity = doctorRepository.findByEmail(email);
        } catch (Exception e) {
            throw new ApiException("Problem during changing password");
        }

        if (!doctorEntity.getPassword().equals(passwordEncoder.encode(oldPassword))) {
            throw new DoctorValidationException("Wrong old password");
        }
        doctorEntity.setPassword(passwordEncoder.encode(newPassword));
        try {
            doctorRepository.save(doctorEntity);
        } catch (Exception e) {
            throw new ApiException("Problem during changing password");
        }
        return doctorEntity;
    }

    @Override
    public DoctorEntity sendToken(String email) throws ApiException {

        DoctorEntity doctorEntity = null;
        try {
            doctorEntity = doctorRepository.findByEmail(email);
        } catch (Exception e) {
            throw new ApiException("Problem during sending email");
        }
        if (doctorEntity == null) {
            throw new DoctorNotFoundException("Wrong email " + email);
        }
        String resetToken = TokenGenerate.generateResetToken();
        doctorEntity.setResetToken(resetToken);
        doctorRepository.save(doctorEntity);

        mailSender.sendEmail(doctorEntity.getEmail(), "Reset token", "Your reset token " + resetToken);
        return doctorEntity;
    }

    @Override
    public Boolean verifyToken(String email, String token) throws ApiException {

        DoctorEntity doctorEntity = null;
        try {
            doctorEntity = doctorRepository.findByEmail(email);
        } catch (Exception e) {
            throw new ApiException("Problem during verifying token");
        }
        if (!doctorEntity.getResetToken().equals(token)) {
            throw new DoctorValidationException("Wrong reset token " + token);
        }
        return true;
    }

    @Override
    public DoctorEntity forgotPassword(String email, String password, String confirmPassword) throws ApiException {

        DoctorEntity doctorEntity = null;
        DoctorValidation.validatePassword(password);

        if (!password.equals(confirmPassword)) {
            throw new DoctorValidationException("Passwords don't match");
        }
        try {
            doctorEntity = doctorRepository.findByEmail(email);
        } catch (Exception e) {
            throw new ApiException("Problem during changing password");
        }
        if (doctorEntity.getResetToken() == null) {
            throw new ApiException("Problem during changing password");
        }
        doctorEntity.setResetToken(null);
        doctorEntity.setPassword(passwordEncoder.encode(confirmPassword));
        doctorRepository.save(doctorEntity);
        return doctorEntity;
    }

    @Override
    public DoctorEntity update(DoctorDTO dto) throws ApiException {

        //DoctorValidation.validateFields(dto);
        validateDuplicate(dto);

        Optional<DoctorEntity> optionalUser = doctorRepository.findById(dto.getId());
        if (optionalUser.isEmpty()) {
            throw new DoctorNotFoundException("User not found with the given ID");
        }
        DoctorEntity doctorEntity = optionalUser.get();

        doctorEntity.setName(dto.getName());
        doctorEntity.setSurname(dto.getSurname());
        doctorEntity.setYear(dto.getYear());
        doctorEntity.setEmail(dto.getEmail() == null ? doctorEntity.getEmail() : dto.getEmail());

        Set<HospitalEntity> hospitals = new HashSet<>();

        for (String x : dto.getHospitals()) {
            HospitalEntity hospital = hospitalRepository.getByName(x);
            hospitals.add(hospital);
        }
        doctorEntity.setHospitals(hospitals);

        try {
            doctorRepository.save(doctorEntity);
        } catch (Exception e) {
            throw new ApiException("Problem during updating user");
        }
        return doctorEntity;
    }

    @Override
    public DoctorEntity bookTime(Integer id, String time, boolean isCanceled) throws ApiException {

        Optional<DoctorEntity> doctorEntity = doctorRepository.findById(id);
        if (doctorEntity.isEmpty()) {
            throw new DoctorNotFoundException("Doctor not found with given id");
        }
        DoctorEntity entity = doctorEntity.get();
        int workTime = Integer.parseInt(entity.getWorkTime());
        int bookTime = Integer.parseInt(entity.getBookTime());
        int newTime = Integer.parseInt(time);
        if (bookTime + newTime > workTime) {
            throw new DoctorValidationException("Doctor is busy");
        }
        if (isCanceled) {
            entity.setBookTime(String.valueOf(bookTime - newTime));
        } else {
            entity.setBookTime(String.valueOf(bookTime + newTime));
        }

        try {
            doctorRepository.save(entity);
        } catch (Exception e) {
            throw new ApiException("Problem during booking time");
        }
        return entity;
    }

    @Override
    public void delete(Integer id) throws ApiException {

        Optional<DoctorEntity> optionalUser = doctorRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new DoctorNotFoundException("User not found with the given ID");
        }
        try {
            doctorRepository.deleteById(id);
        } catch (Exception e) {
            throw new ApiException("Problem during deleting user");
        }
    }

    private void validateDuplicate(DoctorDTO doctorDTO) {

        if (doctorDTO.getId() == null) {
            List<DoctorEntity> doctorEntityList = doctorRepository.getByEmail(doctorDTO.getEmail());
            if (!doctorEntityList.isEmpty()) {
                throw new ResourceAlreadyExistException("User already exist");
            }
        } else {
            DoctorEntity doctorEntity = doctorRepository.getByEmailAndIdNot(doctorDTO.getEmail(), doctorDTO.getId());
            if (doctorEntity != null) {
                throw new ResourceAlreadyExistException("Email of user already exist");
            }
        }
    }
}
