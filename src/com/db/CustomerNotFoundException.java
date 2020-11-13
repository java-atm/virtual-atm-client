package com.db;

public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
