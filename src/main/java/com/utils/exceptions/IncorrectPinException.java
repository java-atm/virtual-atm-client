package com.utils.exceptions;

public class IncorrectPinException extends Exception{
    public IncorrectPinException(String errorMessage) {
        super(errorMessage);
    }
}
