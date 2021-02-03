package com.utils.exceptions.atm_exceptions;

public class CustomerNotFoundException extends AtmException{
    public CustomerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
