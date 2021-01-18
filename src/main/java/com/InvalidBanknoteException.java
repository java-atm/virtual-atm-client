package com;

public class InvalidBanknoteException extends Exception {
    public InvalidBanknoteException(String errorMessage) {
        super(errorMessage);
    }
}
