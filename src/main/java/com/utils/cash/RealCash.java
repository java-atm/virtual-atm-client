package com.utils.cash;

import com.utils.enums.Banknote;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;

public class RealCash extends CashAbstract {

    private static final Logger LOGGER = LogManager.getLogger(RealCash.class);

    private LinkedHashMap<Double, Integer> banknotes;

    public RealCash() {
        super();
        banknotes = new LinkedHashMap<>();
        initBanknotes(0);
    }

    public RealCash(double initialAmount) {
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
        int numberOfBanknotes = banknotes.get(amount);
        banknotes.put(amount, banknotes.get(amount) + 1);
        if (numberOfBanknotes == banknotes.get(amount)) {
            LOGGER.fatal("AMOUNT IS NOT ADDED");
            throw new RuntimeException("AMOUNT IS NOT ADDED");
        }
    }

    @Override
    public void subtractAmount(double amount) {
        setAmount(getAmount() - amount);
        int numberOfBanknotes = banknotes.get(amount);
        banknotes.put(amount, banknotes.get(amount) - 1);
        if (numberOfBanknotes == banknotes.get(amount)) {
            LOGGER.fatal("AMOUNT IS NOT SUBTRACTED");
            throw new RuntimeException("AMOUNT IS NOT SUBTRACTED");
        }
    }
}
