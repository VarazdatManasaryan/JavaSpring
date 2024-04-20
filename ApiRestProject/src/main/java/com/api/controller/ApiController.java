package com.api.controller;

import com.api.model.DoctorDTO;
import com.api.model.DoctorResponseDTO;
import com.api.service.ApiService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ApiService apiService;

    @GetMapping
    public ResponseEntity<List<DoctorResponseDTO>> getAll() {
        return apiService.getAll();
    }

//    @PostMapping
//    public Object post(@RequestBody DoctorDTO dto) throws JsonProcessingException {
//        return apiService.post(dto);
//    }
}
