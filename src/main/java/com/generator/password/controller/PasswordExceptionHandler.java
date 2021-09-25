package com.generator.password.controller;

import com.generator.password.exception.PasswordErrorResponse;
import com.generator.password.exception.PasswordException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PasswordExceptionHandler {

    @ExceptionHandler(value = PasswordException.class)
    public ResponseEntity<PasswordErrorResponse> handleException(PasswordException exc) {
        PasswordErrorResponse error = new PasswordErrorResponse();

        error.setStatus(HttpStatus.FORBIDDEN.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PasswordException.class)
    public ResponseEntity<PasswordErrorResponse> handleException(Exception exc) {
        PasswordErrorResponse error = new PasswordErrorResponse();

        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage(exc.getMessage());
        error.setTimeStamp(System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}
