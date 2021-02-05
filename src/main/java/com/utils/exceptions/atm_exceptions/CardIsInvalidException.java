package com.utils.exceptions.atm_exceptions;

public class CardIsInvalidException extends AtmException {
    public CardIsInvalidException(String invalid_card) {
        super(invalid_card);
    }
}
