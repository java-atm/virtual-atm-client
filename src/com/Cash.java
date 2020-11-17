package com;

public class Cash extends CashAbstract {

    public Cash() {
        super();
    }

    public Cash(double amount) {
        super(amount);
    }

    @Override
    public void addAmount(double amount) {
        setAmount(getAmount() + amount);
    }

    @Override
    public void subtractAmount(double amount) {
        setAmount(getAmount() - amount);
    }
}
