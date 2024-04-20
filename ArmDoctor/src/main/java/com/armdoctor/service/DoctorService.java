package com.armdoctor.service;

import com.armdoctor.dto.requestdto.DoctorDTO;
import com.armdoctor.dto.responsedto.DoctorResponseDTO;
import com.armdoctor.exceptions.ApiException;
import com.armdoctor.model.DoctorEntity;

import java.util.List;

public interface DoctorService {

    DoctorEntity createUser(DoctorDTO doctorDTO) throws ApiException;

    List<DoctorEntity> getByUsername(String email) throws ApiException;

    List<DoctorEntity> getAll() throws ApiException;

    List<DoctorResponseDTO> getByProfession(String profession) throws ApiException;

    DoctorEntity verifyUser(String email, String verifyCode) throws ApiException;

    DoctorEntity changePassword(String oldPassword, String newPassword, String confirmPassword, String email) throws ApiException;

    DoctorEntity sendToken(String email) throws ApiException;

    Boolean verifyToken(String email, String token) throws ApiException;

    DoctorEntity forgotPassword(String email, String password, String confirmPassword) throws ApiException;

    DoctorEntity update(DoctorDTO dto) throws ApiException;

    DoctorEntity bookTime(Integer id, String time, boolean isCanceled) throws ApiException;

    void delete(Integer id) throws ApiException;
}
