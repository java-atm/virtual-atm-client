package com.utils.exceptions;

public class CashNotEnoughException extends Exception {

    public CashNotEnoughException(String errorMessage) {
        super(errorMessage);
    }
}
