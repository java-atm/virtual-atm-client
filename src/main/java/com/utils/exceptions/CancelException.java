package com.utils.exceptions;

public class CancelException extends Exception {
    public CancelException(String atm_power_off) {
        super(atm_power_off);
    }
}
