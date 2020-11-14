package com;

import java.util.LinkedHashMap;
import java.util.Objects;

public class RealCash extends CashAbstract {

    private LinkedHashMap<Integer, Integer> banknotes = new LinkedHashMap<>();

    public RealCash() {
        super();
    }

    public RealCash(double amount) {
        super(amount);
    }

    public LinkedHashMap<Integer, Integer> getBanknotes() {
        return banknotes;
    }

    public LinkedHashMap<Integer, Integer> getCloneBanknotes() {
        return new LinkedHashMap<>(banknotes);
    }

    public RealCash getClone() {
        RealCash newRealCash = new RealCash(this.getAmount());
        newRealCash.setBanknotes(this.getCloneBanknotes());
        return newRealCash;
    }

    public void setBanknotes(LinkedHashMap<Integer, Integer> banknotes) {
        this.banknotes = banknotes;
    }

    public void setAmount(int amount) {
        super.setAmount(amount);
        banknotes.put(amount, banknotes.get(amount) + 1);

    }
}
