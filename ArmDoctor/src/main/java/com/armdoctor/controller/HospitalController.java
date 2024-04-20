package com.armdoctor.controller;

import com.armdoctor.dto.requestdto.HospitalDTO;
import com.armdoctor.dto.responsedto.DoctorResponseDTO;
import com.armdoctor.exceptions.ApiException;
import com.armdoctor.model.HospitalEntity;
import com.armdoctor.service.HospitalService;
import com.armdoctor.service.RelatedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private RelatedService relatedService;

    @PostMapping("/add-hospital")
    public HospitalEntity addHospital(@RequestBody @Valid HospitalDTO dto) throws ApiException {
        return hospitalService.addHospital(dto);
    }

    @GetMapping("/get-all")
    public List<HospitalEntity> getAll(@RequestParam(required = false) String name) throws ApiException {
        return hospitalService.getAll(name);
    }

    @GetMapping("/get-doctors")
    public List<DoctorResponseDTO> getDoctors(@RequestParam Integer hospitalId) {
        return relatedService.geByHospitalId(hospitalId);
    }
}
