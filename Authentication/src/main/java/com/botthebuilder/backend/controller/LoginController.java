package com.botthebuilder.backend.controller;

import com.botthebuilder.backend.entity.User;
import com.botthebuilder.backend.request.LoginRequest;
import com.botthebuilder.backend.response.LoginResponse;
import com.botthebuilder.backend.service.MyUserDetailsService;
import com.botthebuilder.backend.service.TokenValidation;
import com.botthebuilder.backend.service.UserService;
import com.botthebuilder.backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.spec.PSource;

@RestController
public class LoginController {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws Exception {


        String email = request.getEmail();

        if(!TokenValidation.validate(request.getAccess_token())){
            throw new Exception("Invalid access token");
        }

        User user = userService.getUserFromEmail(email);
        if(user==null){
            user = User.builder()
                    .email(email)
                    .username(request.getUsername())
                    .access_token(request.getAccess_token())
                    .build();
        }

        user.setAccess_token(request.getAccess_token());

        userService.saveToDb(user);

        UserDetails userDetails = myUserDetailsService.loadUserByUsername(request.getEmail());

        String jwt = jwtUtil.generateToken(userDetails);

        LoginResponse loginResponse = LoginResponse.builder()
                .msg("Logged in successfully")
                .status(200)
                .token(jwt)
                .userId(user.getUserId())
                .build();

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }

}
