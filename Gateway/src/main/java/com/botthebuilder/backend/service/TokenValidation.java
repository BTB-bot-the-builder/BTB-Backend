package com.botthebuilder.backend.service;

import org.springframework.stereotype.Service;

@Service
public class TokenValidation {

    public static Boolean validate(String token){
        if(token.startsWith("a")){
            return false;
        }
        return true;
    }
}
