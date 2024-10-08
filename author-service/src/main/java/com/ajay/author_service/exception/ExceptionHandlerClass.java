package com.ajay.author_service.exception;

import com.ajay.author_service.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerClass {


    @ExceptionHandler(AuthorExistsException.class)
    public ResponseEntity<CustomExceptionClass> handleUsernameExceptions(AuthorExistsException exc) {
        CustomExceptionClass custom = new CustomExceptionClass();
        custom.setMessage(exc.getMessage());
        custom.setStatus(HttpStatus.CONFLICT.value());
        custom.setTimestamp(String.valueOf(System.currentTimeMillis()));

        return new ResponseEntity<>(custom, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<CustomExceptionClass> handleException(InvalidAuthorException exc){
        CustomExceptionClass custom=new CustomExceptionClass();
        custom.setMessage(exc.getMessage());
        custom.setStatus(HttpStatus.UNAUTHORIZED.value());
        custom.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return new ResponseEntity<>(custom,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<CustomExceptionClass> handleAuthorNtFoundExceptions(AuthorNotFoundException exc) {
        CustomExceptionClass custom = new CustomExceptionClass();
        custom.setMessage(exc.getMessage());
        custom.setStatus(HttpStatus.NOT_FOUND.value());
        custom.setTimestamp(String.valueOf(System.currentTimeMillis()));

        return new ResponseEntity<>(custom, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<CustomExceptionClass> handleOtherExceptions(Exception exc) {
        CustomExceptionClass custom = new CustomExceptionClass();
        custom.setMessage(Constants.UNEXPECTED_ERROR);
        custom.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        custom.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return new ResponseEntity<>(custom, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<CustomExceptionClass> handleException(InvalidTokenException exc){
        CustomExceptionClass custom=new CustomExceptionClass();
        custom.setMessage(exc.getMessage());
        custom.setStatus(HttpStatus.UNAUTHORIZED.value());
        custom.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return new ResponseEntity<>(custom,HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<CustomExceptionClass> handleException(InvalidRequestException exc){
        CustomExceptionClass custom=new CustomExceptionClass();
        custom.setMessage(exc.getMessage());
        custom.setStatus(HttpStatus.BAD_REQUEST.value());
        custom.setTimestamp(String.valueOf(System.currentTimeMillis()));
        return new ResponseEntity<>(custom,HttpStatus.BAD_REQUEST);
    }


}