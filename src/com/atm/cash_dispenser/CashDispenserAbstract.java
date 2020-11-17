package com.atm.cash_dispenser;

import com.RealCash;
import com.atm.CashNotEnoughException;
import com.utils.enums.Banknote;

public abstract class CashDispenserAbstract implements CashDispenserInterface{

    private RealCash cash;

    public CashDispenserAbstract(RealCash initialCash) {
        setInitialCash(initialCash);
    }

    @Override
    public void setInitialCash(RealCash initialCash) {
        if (cash == null) {
            setCash(new RealCash(initialCash.getAmount()));
        } else {
            System.out.println("Initial cash is already set, call addCash to add cash.");
        }
    }

    public RealCash getCash() {
        return cash;
    }

    public void setCash(RealCash cash) {
        this.cash = cash;
    }

    @Override
    public void dispenseCash(Double amount) throws CashNotEnoughException {
        RealCash tempCash = cash.getClone();
        Banknote[] banknotes = Banknote.values();
        for (Banknote banknote : banknotes){
            double banknoteKey = banknote.getBanknote();
            while(amount >= banknoteKey) {
                int value = tempCash.getBanknoteNumberByKey(banknoteKey);
                if (value > 0) {
                    tempCash.subtractAmount(banknoteKey);
                    amount -= banknoteKey;
                } else {
                    break;
                }
            }

        }
        if (amount != 0) {
            System.out.println("Cash not enough. Please call maintenance.");
            throw new CashNotEnoughException("ATM doesn't have enough money");
        }
        this.cash = tempCash;
    }

    @Override
    public boolean isCashEnough(Double amount) {
        return cash.getAmount() > amount;
    }

    @Override
    public void addCash(Double banknote) {
        cash.addAmount(banknote);
    }
}
