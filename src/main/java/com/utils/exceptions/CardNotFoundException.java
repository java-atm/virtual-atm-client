package com.utils.exceptions;

public class CardNotFoundException extends Exception{
    public CardNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
