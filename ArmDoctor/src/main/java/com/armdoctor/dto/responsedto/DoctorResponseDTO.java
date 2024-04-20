package com.armdoctor.dto.responsedto;

import com.armdoctor.enums.Role;
import com.armdoctor.enums.Status;
import com.armdoctor.model.HospitalEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder
public class DoctorResponseDTO {

    @JsonIgnore
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("email")
    private String email;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String verifyCode;

    @JsonIgnore
    private Status status;

    @JsonIgnore
    private Role role;

    @JsonIgnore
    private String resetToken;

    @JsonProperty("profession")
    private String profession;

    @JsonProperty("workTime")
    private String workTime;

    @JsonProperty("bookTime")
    private String bookTime;

    @JsonIgnore
    private Set<HospitalEntity> hospitals;
}
