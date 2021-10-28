package com.botthebuilder.userproject.exceptionhandling;

import com.botthebuilder.userproject.responses.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // needs update

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Response> userNotFound(Exception e){
        Response ex = new Response("User not found", "404" );
        return new ResponseEntity<Response>(ex , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ProjectNotFoundException.class})
    public ResponseEntity<Response> projectNotFound(Exception e){
        Response ex = new Response("Project not found", "404" );
        return new ResponseEntity<Response>(ex , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {FileStorageException.class})
    public ResponseEntity<Response> uploadFailed(Exception e){
        Response ex = new Response("Failed to upload file", "500" );
        return new ResponseEntity<Response>(ex , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {InvalidUserException.class})
    public ResponseEntity<Response> invalidUser(Exception e){
        Response ex = new Response("Invalid User", "400" );
        return new ResponseEntity<Response>(ex , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidProjectException.class})
    public ResponseEntity<Response> invalidProject(Exception e){
        Response ex = new Response("Invalid User", "400" );
        return new ResponseEntity<Response>(ex , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidActionException.class})
    public ResponseEntity<Response> invalidAction(Exception e){
        Response ex = new Response("Invalid User", "400" );
        return new ResponseEntity<Response>(ex , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidFileContentException.class})
    public ResponseEntity<Response> invalidFileContent(Exception e){
        Response ex = new Response("Invalid data file content", "400" );
        return new ResponseEntity<Response>(ex , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {InvalidFileExtensionException.class})
    public ResponseEntity<Response> invalidFileExtension(Exception e){
        Response ex = new Response("Invalid data file extension", "400" );
        return new ResponseEntity<Response>(ex , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Response> generalException(Exception e){
        Response ex = new Response("Internal Server Error", "500" );
        return new ResponseEntity<Response>(ex , HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
