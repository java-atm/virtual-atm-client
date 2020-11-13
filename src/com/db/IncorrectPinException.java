package com.db;

public class IncorrectPinException extends Exception{
    public IncorrectPinException(String errorMessage) {
        super(errorMessage);
    }
}
