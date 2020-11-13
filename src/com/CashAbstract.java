package com;

import java.util.Objects;

public abstract class CashAbstract implements CashInterface {

    private double amount;
    private final double DEFAULT_CASH = 0;

    public CashAbstract() {
        amount = DEFAULT_CASH;
    }

    public CashAbstract(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public void add(Cash cash) {
        amount += cash.getAmount();
    }

    @Override
    public void subtract(Cash cash) {
        amount -= cash.getAmount();
    }

    @Override
    public boolean greaterThan(Cash cash) {
        return amount > cash.getAmount();
    }

    @Override
    public boolean lessThan(Cash cash) {
        return amount < cash.getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CashAbstract)) return false;
        CashAbstract that = (CashAbstract) o;
        return Double.compare(that.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return " " + amount;
    }
}
