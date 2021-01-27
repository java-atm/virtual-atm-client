package com.atm.card_reader;

public class CardIsInvalidException extends Exception {
    public CardIsInvalidException(String invalid_card) {
        super(invalid_card);
    }
}
