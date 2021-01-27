package com.backend_connection;

public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
