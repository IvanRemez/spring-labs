package com.cydeo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionWrapper> processNotFoundException(
            NotFoundException ex) {
        // ^^ ex captures the message inside each NotFoundException we pass in our App

    // create JSON body and return
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(ex.getMessage(),
                HttpStatus.NOT_FOUND);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }

}
