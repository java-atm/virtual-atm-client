package com.utils.enums;

public enum Action {

    CHECK_BALANCE(1, "Check balance"),
    WITHDRAWAL(2, "Withdrawal"),
    DEPOSIT(3, "Deposit"),
    PIN_CHANGE(4, "Change PIN"),
    EXIT(5, "Exit");

    private final int actionInt;
    private final String actionString;

    Action(int actionInt, String actionString ) {
        this.actionInt = actionInt;
        this.actionString = actionString;
    }

    public int getIntAction() {
        return actionInt;
    }

    public String getStringAction() {
        return actionString;
    }
}
