package com.cydeo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionWrapper>  processValidationError(MethodArgumentNotValidException ex) {
        return handleMethodArgumentNotValid(ex);
    }

    protected ResponseEntity<ExceptionWrapper> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        ExceptionWrapper exceptionWrapper = new ExceptionWrapper("Invalid Input(s)", HttpStatus.BAD_REQUEST);
        List<ValidationError> validationErrors = new ArrayList<>();

    // For each loop to assign all ValidationError fields for each error
        for (ObjectError error : ex.getBindingResult().getAllErrors()){

            String fieldName = ((FieldError) error).getField();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            String errorMessage = error.getDefaultMessage();

            ValidationError validationError = new ValidationError(fieldName, rejectedValue, errorMessage);
            validationErrors.add(validationError);
        // adds ValidationError to List (validationErrors) once fields assigned
        }

        exceptionWrapper.setValidationErrorList(validationErrors);
        exceptionWrapper.setErrorCount(validationErrors.size());
    // sets ExceptionWrapper validation Error fields ^^ (error list and # of errors)

        return new ResponseEntity<>(exceptionWrapper,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({Exception.class, RuntimeException.class, Throwable.class})
    public ResponseEntity<ExceptionWrapper> genericExceptionHandler() {
        return new ResponseEntity<>(new ExceptionWrapper("Action failed: An error occurred!",HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CurrencyTypeNotFoundException.class)
    public ResponseEntity<ExceptionWrapper> handleCurrencyTypeNotFoundException(
            CurrencyTypeNotFoundException ex) {
        // ^^ ex captures the message inside each NotFoundException we pass in our App

        // create JSON body and return
        ExceptionWrapper exceptionWrapper = new ExceptionWrapper(ex.getMessage(),
                HttpStatus.NOT_FOUND);

        exceptionWrapper.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exceptionWrapper);
    }

}
