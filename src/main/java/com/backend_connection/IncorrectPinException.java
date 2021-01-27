package com.backend_connection;

public class IncorrectPinException extends Exception{
    public IncorrectPinException(String errorMessage) {
        super(errorMessage);
    }
}
