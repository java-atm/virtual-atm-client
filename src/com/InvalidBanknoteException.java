package com;

import java.util.InputMismatchException;

public class InvalidBanknoteException extends InputMismatchException {
    InvalidBanknoteException(String errorMessage) {
        super(errorMessage);
    }
}
