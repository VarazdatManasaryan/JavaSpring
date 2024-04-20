package com.armdoctor;

import com.armdoctor.dto.requestdto.DoctorDTO;
import com.armdoctor.exceptions.ApiException;
import com.armdoctor.exceptions.DoctorValidationException;
import com.armdoctor.model.DoctorEntity;
import com.armdoctor.repository.DoctorRepository;
import com.armdoctor.repository.HospitalRepository;
import com.armdoctor.service.DoctorService;
import com.armdoctor.service.impl.DoctorServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = DoctorServiceImpl.class)
public class ArmDoctorTest {

    @MockBean
    private DoctorRepository doctorRepository;

    @MockBean
    private HospitalRepository hospitalRepository;

    @MockBean
    private MailSender mailSender;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DoctorService doctorService;

    DoctorEntity entityWithNullId = DoctorEntity
            .builder()
            .name("Artak")
            .surname("Sargsyan")
            .email("artshnik@gmail.com")
            .build();

    DoctorEntity entityWithId = DoctorEntity
            .builder()
            .id(1)
            .name("Artak")
            .surname("Sargsyan")
            .email("artshnik@gmail.com")
            .build();

    DoctorDTO dto = DoctorDTO
            .builder()
            .name("Artak")
            .surname("Sargsyan")
            .email("artshnik@gmail.com")
            .build();


    @Test
    void createDoctor() throws ApiException {
        given(doctorRepository.save(entityWithNullId)).willReturn(entityWithId);

        dto.setName(null);
        DoctorEntity entity = doctorService.createUser(dto);
        Assertions.assertThrows(DoctorValidationException.class, () -> {
            doctorService.createUser(dto);
        });

        assertThat(entity.getId().equals(entityWithId.getId()));
        assertThat(entity.getName().equals(entityWithId.getName()));
    }
}
