package com.botthebuilder.backend.exceptionhandling;

import com.botthebuilder.backend.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = InvalidAccessTokenException.class)
    public ResponseEntity<ExceptionResponse> invalidAccessToken(InvalidAccessTokenException e){

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status("400")
                .msg("Invalid access token")
                .build();

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleExceptions(Exception e){

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status("500")
                .msg("Internal Server Error")
                .build();

        return new ResponseEntity<ExceptionResponse>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
