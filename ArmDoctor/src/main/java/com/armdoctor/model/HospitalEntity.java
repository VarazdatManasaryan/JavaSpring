package com.armdoctor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "hospital")
public class HospitalEntity {

    @Id
    @Column(name = "hospital_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private String password;
    private String address;

    @ManyToMany(mappedBy = "hospitals")
    @JsonIgnore
    private Set<DoctorEntity> doctors;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HospitalEntity hospital = (HospitalEntity) o;
        return Objects.equals(id, hospital.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
