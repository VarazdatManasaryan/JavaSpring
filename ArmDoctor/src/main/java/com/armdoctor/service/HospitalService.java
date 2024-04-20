package com.armdoctor.service;

import com.armdoctor.dto.requestdto.HospitalDTO;
import com.armdoctor.exceptions.ApiException;
import com.armdoctor.model.HospitalEntity;

import java.util.List;

public interface HospitalService {

    HospitalEntity addHospital(HospitalDTO dto) throws ApiException;

    List<HospitalEntity> getAll(String name) throws ApiException;
}
