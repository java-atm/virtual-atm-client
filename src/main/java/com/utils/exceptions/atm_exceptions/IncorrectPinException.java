package com.utils.exceptions.atm_exceptions;

public class IncorrectPinException extends AtmException{
    public IncorrectPinException(String errorMessage) {
        super(errorMessage);
    }
}
