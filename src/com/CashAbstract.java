package com;

public abstract class CashAbstract {

    private double amount;
    private final double DEFAULT_CASH = 0;

    public CashAbstract() {
        amount = DEFAULT_CASH;
    }

    public CashAbstract(double amount) {
        this.amount = amount;
    }

    public abstract void addAmount(double amount);

    public abstract void subtractAmount(double amount);

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Amount = " + amount;
    }
}
