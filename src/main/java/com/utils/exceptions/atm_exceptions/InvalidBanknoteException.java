package com.utils.exceptions.atm_exceptions;

public class InvalidBanknoteException extends AtmException {
    public InvalidBanknoteException(String errorMessage) {
        super(errorMessage);
    }
}
