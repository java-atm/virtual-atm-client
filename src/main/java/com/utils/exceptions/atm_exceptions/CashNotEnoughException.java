package com.utils.exceptions.atm_exceptions;

public class CashNotEnoughException extends AtmException {

    public CashNotEnoughException(String errorMessage) {
        super(errorMessage);
    }
}
