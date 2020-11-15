package com;

import com.utils.enums.Banknote;
import java.util.LinkedHashMap;

public class RealCash extends CashAbstract {

    private LinkedHashMap<Double, Integer> banknotes;

    public RealCash() {
        super();
        banknotes = new LinkedHashMap<>();
        initBanknotes();
    }

    public RealCash(double amount) {
        super(amount);
        banknotes = new LinkedHashMap<>();
        initBanknotes();
    }

    private void initBanknotes() {
        Banknote[] banknotes = Banknote.values();
        for(Banknote banknote : banknotes) {
            this.banknotes.put(banknote.getBanknote(), 0);
        }
    }

    public int getBanknoteNumberByKey(Double banknoteKey) {
        return banknotes.get(banknoteKey);
    }

    public RealCash getClone() {
        RealCash newRealCash = new RealCash(this.getAmount());
        newRealCash.banknotes = new LinkedHashMap<>(banknotes);
        return newRealCash;
    }

    @Override
    public void addAmount(double amount) {
        setAmount(getAmount() + amount);
        banknotes.put(amount, banknotes.get(amount) + 1);
    }

    @Override
    public void subtractAmount(double amount) {
        setAmount(getAmount() - amount);
        banknotes.put(amount, banknotes.get(amount) - 1);
    }
}
