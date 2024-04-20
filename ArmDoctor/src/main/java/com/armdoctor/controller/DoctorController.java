package com.armdoctor.controller;

import com.armdoctor.dto.requestdto.DoctorDTO;
import com.armdoctor.dto.responsedto.DoctorResponseDTO;
import com.armdoctor.exceptions.ApiException;
import com.armdoctor.model.DoctorEntity;
import com.armdoctor.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/user")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/create-user")
    public DoctorEntity createUser(@RequestBody @Valid DoctorDTO doctorDTO) throws ApiException {
        return doctorService.createUser(doctorDTO);
    }

    @GetMapping("/get-by-username")
    public List<DoctorEntity> getByUsername(@RequestParam String email) throws ApiException {
        return doctorService.getByUsername(email);
    }

    @PatchMapping("/verify")
    public DoctorEntity verifyUser(@RequestParam String email,
                                   @RequestParam String verifyCode) throws ApiException {
        return doctorService.verifyUser(email, verifyCode);
    }

    @PatchMapping("/change-password")
    public DoctorEntity changePassword(@RequestParam String oldPassword,
                                       @RequestParam String newPassword,
                                       @RequestParam String confirmPassword,
                                       Principal principal) throws ApiException {
        String email = principal.getName();
        return doctorService.changePassword(oldPassword, newPassword, confirmPassword, email);
    }

    @PatchMapping("/send-reset-token")
    public DoctorEntity sendResetToken(@RequestParam String email) throws ApiException {
        return doctorService.sendToken(email);
    }

    @GetMapping("/verify-reset-token")
    public Boolean verifyResetToken(@RequestParam String email,
                                    @RequestParam String token) throws ApiException {
        return doctorService.verifyToken(email, token);
    }

    @PatchMapping("/forgot-password")
    public DoctorEntity forgotPassword(@RequestParam String email,
                                       @RequestParam String password,
                                       @RequestParam String confirmPassword) throws ApiException {
        return doctorService.forgotPassword(email, password, confirmPassword);
    }

    @PutMapping("/update")
    public DoctorEntity update(@RequestBody @Valid DoctorDTO doctorDTO) throws ApiException {
        return doctorService.update(doctorDTO);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) throws ApiException {
        doctorService.delete(id);
    }

    @GetMapping("/get-all")
    public List<DoctorEntity> getAll() throws ApiException {
        return doctorService.getAll();
    }

    @PatchMapping("/book-time/{id}")
    public DoctorEntity bookTime(@PathVariable Integer id, @RequestParam String bookTime, @RequestParam boolean isCanceled) throws ApiException {
        return doctorService.bookTime(id, bookTime, isCanceled);
    }

    @GetMapping("/get-by-profession")
    public List<DoctorResponseDTO> getByProfession(@RequestParam String profession) throws ApiException {
        return doctorService.getByProfession(profession);
    }
}
