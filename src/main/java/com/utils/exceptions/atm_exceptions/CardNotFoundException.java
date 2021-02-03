package com.utils.exceptions.atm_exceptions;

public class CardNotFoundException extends AtmException{
    public CardNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
