package com.armdoctor.service;

import com.armdoctor.dto.responsedto.DoctorResponseDTO;

import java.util.List;

public interface RelatedService {

    List<DoctorResponseDTO> geByHospitalId(Integer hospitalId);
}
