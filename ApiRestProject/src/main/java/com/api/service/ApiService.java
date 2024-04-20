package com.api.service;

import com.api.model.DoctorDTO;
import com.api.model.DoctorResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ApiService {

    public static final String URL = "http://localhost:9090";

    public static final String TOKEN = "di5tYW5hc2FyeWFuLjk0QG1haWwucnU6RHprbmlrMTIzNA==";

    public ResponseEntity<List<DoctorResponseDTO>> getAll() {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + TOKEN);
        HttpEntity<Object> entity = new HttpEntity<>(headers);

        return restTemplate.exchange(
                URL + "/user/get-all",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() { });
    }

//    public Object post(DoctorDTO dto) throws JsonProcessingException {
//
//        RestTemplate restTemplate = new RestTemplate();
//        ObjectMapper mapper = new ObjectMapper();
//        String s = mapper.writeValueAsString(dto);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//    //    HttpEntity<DoctorResponseDTO> entity = new HttpEntity<>(s, headers);
//
//      //  return restTemplate.exchange(URL + "/user/create-user", HttpMethod.POST, entity, DoctorResponseDTO.class);
//  //  }
}
