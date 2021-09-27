package com.generator.password.controller;

import com.generator.password.exception.PasswordException;
import com.generator.password.response.PasswordErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class PasswordExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<PasswordErrorResponse> handlePasswordException(PasswordException exc) {
        final PasswordErrorResponse error = new PasswordErrorResponse(
                HttpStatus.BAD_REQUEST.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PasswordErrorResponse> handleIllegalArgumentException(IllegalArgumentException exc) {
        final PasswordErrorResponse error = new PasswordErrorResponse(
                HttpStatus.BAD_REQUEST.value(), exc.getMessage(), System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<PasswordErrorResponse> handleAll(Exception exc) {
        final String message = "form fields can not be null";
        final PasswordErrorResponse error = new PasswordErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), message, System.currentTimeMillis());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
