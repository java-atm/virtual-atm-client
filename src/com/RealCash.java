package com;

import com.utils.enums.Banknote;
import java.util.LinkedHashMap;

public class RealCash extends CashAbstract {

    private LinkedHashMap<Double, Integer> banknotes;

    public RealCash() {
        super();
        banknotes = new LinkedHashMap<>();
        initBanknotes(0);
    }

    public RealCash(double initialAmount) throws InvalidBanknoteException {
        if (initialAmount % Banknote.BANKNOTE_10.getBanknote() != 0)
            throw new InvalidBanknoteException("Invalid Banknote");
        banknotes = new LinkedHashMap<>();
        initBanknotes(initialAmount);
    }

    private void initBanknotes(double initialAmount) {
        Banknote[] banknotes = Banknote.values();
        for(Banknote banknote : banknotes) {
            this.banknotes.put(banknote.getBanknote(), 0);
        }
        separateByBanknotes(initialAmount);
    }

    private void separateByBanknotes(Double initialAmount) {
        Banknote[] banknotes = Banknote.values();
        while(initialAmount != 0) {
            for (Banknote banknote : banknotes){
                double key = banknote.getBanknote();
                if (initialAmount >= key) {
                    addAmount(key);
                    initialAmount -= key;
                }
            }
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
