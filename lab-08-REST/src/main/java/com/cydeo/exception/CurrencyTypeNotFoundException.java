package com.cydeo.exception;

public class CurrencyTypeNotFoundException extends RuntimeException {
    public CurrencyTypeNotFoundException(String message) {
        super(message);
    }
}
