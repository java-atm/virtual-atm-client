package com.atm;

public class CashNotEnoughException extends Exception {

    public CashNotEnoughException(String errorMessage) {
        super(errorMessage);
    }
}
