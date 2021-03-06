package com.utils.enums;

public enum Action {

    CHECK_BALANCE(1, "Check balance"),
    WITHDRAW(2, "Withdrawal"),
    DEPOSIT(3, "Deposit"),
    TRANSFER(4, "Transfer"),
    CHANGE_PIN(5, "Change PIN"),
    EXIT(6, "Exit");

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
