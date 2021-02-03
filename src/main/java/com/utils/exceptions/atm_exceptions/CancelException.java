package com.utils.exceptions.atm_exceptions;

public class CancelException extends AtmException {
    public CancelException(String atm_power_off) {
        super(atm_power_off);
    }
}
