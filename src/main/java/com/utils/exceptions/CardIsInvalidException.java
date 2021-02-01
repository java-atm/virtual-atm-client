package com.utils.exceptions;

public class CardIsInvalidException extends Exception {
    public CardIsInvalidException(String invalid_card) {
        super(invalid_card);
    }
}
