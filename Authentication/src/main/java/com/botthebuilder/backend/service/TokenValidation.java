package com.botthebuilder.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TokenValidation {


    public static Boolean validate(String token){

        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/oauth2/v1/tokeninfo?access_token=";
        ResponseEntity<String> response = restTemplate.getForEntity(url+token, String.class);

        return response.getStatusCode() == HttpStatus.OK;

    }
}
