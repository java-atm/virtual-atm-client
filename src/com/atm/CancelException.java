package com.atm;

public class CancelException extends Exception {
    public CancelException(String atm_power_off) {
        super(atm_power_off);
    }
}
