package com.backend_connection;

public class CardNotFoundException extends Exception{
    public CardNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
