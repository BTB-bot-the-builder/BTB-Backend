package com.botthebuilder.userproject.exceptionhandling;

import com.botthebuilder.userproject.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
//
//    @ExceptionHandler
//    public ResponseEntity<ExceptionResponse> handleExceptions(Exception e){
//        ExceptionResponse ex = new ExceptionResponse(e.getMessage(), "500" );
//        return new ResponseEntity<ExceptionResponse>(ex , HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Response> invalidUser(Exception e){
        Response ex = new Response("User not found", "404" );
        return new ResponseEntity<Response>(ex , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ProjectNotFoundException.class})
    public ResponseEntity<Response> invalidProject(Exception e){
        Response ex = new Response("Project not found", "404" );
        return new ResponseEntity<Response>(ex , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {FileStorageException.class})
    public ResponseEntity<Response> uploadFailed(Exception e){
        Response ex = new Response("Failed to upload file", "500" );
        return new ResponseEntity<Response>(ex , HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
