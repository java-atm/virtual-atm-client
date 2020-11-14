package com.atm.cash_dispenser;

import com.Cash;
import com.RealCash;
import com.atm.CashNotEnoughException;
import com.utils.enums.Banknote;

public abstract class CashDispenserAbstract implements CashDispenserInterface{

    private RealCash cash;

    public CashDispenserAbstract(RealCash initialCash) {
        setInitialCash(initialCash);
        initBanknotes();
        separateByBanknotes(initialCash);
    }

    @Override
    public void setInitialCash(RealCash initialCash) {
        if (cash == null) {
            cash = initialCash;
        } else {
            System.out.println("Initial cash is already set, call addCash to add cash.");
        }
    }

    private void initBanknotes() {
        Banknote[] banknotes = Banknote.values();
        for(Banknote banknote : banknotes) {
            cash.getBanknotes().put(banknote.getBanknote(), 0);
        }
    }

    private void separateByBanknotes(RealCash initialCash) {
        Banknote[] banknotes = Banknote.values();
        Cash separatedCashSum = new Cash(initialCash.getAmount());
        while(separatedCashSum.getAmount() != 0) {
            for (Banknote banknote : banknotes){
                int key = banknote.getBanknote();
                int value = cash.getBanknotes().get(banknote.getBanknote()) + 1;
                if (separatedCashSum.getAmount() >= banknote.getBanknote()) {
                    cash.getBanknotes().put(key, value);
                    separatedCashSum.setAmount(separatedCashSum.getAmount() - banknote.getBanknote());
                }

            }
        }
    }

    public RealCash getCash() {
        return cash;
    }

    public void setCash(RealCash cash) {
        this.cash = cash;
    }

    @Override
    public void dispenseCash(Integer amount) throws CashNotEnoughException {
        try {
            checkBanknotesIsEnough(amount);
        } catch (CashNotEnoughException exception) {
            throw exception;
        }
    }

    private void checkBanknotesIsEnough(int amount) throws CashNotEnoughException {
        RealCash tempCash = cash.getClone();
        Banknote[] banknotes = Banknote.values();
        for (Banknote banknote : banknotes){
            int banknoteKey = banknote.getBanknote();
            while(amount >= banknoteKey) {
                int value = tempCash.getBanknotes().get(banknoteKey);
                if (value > 0) {
                    tempCash.getBanknotes().put(banknoteKey, value - 1);
                    tempCash.setAmount(tempCash.getAmount() - banknoteKey);
                    amount -= banknoteKey;
                } else {
                    break;
                }
            }

        }
        if (amount != 0) { //
            System.out.println("Cash not enough. Please call maintenance.");
            throw new CashNotEnoughException("ATM doesn't have enough money");
        }
        this.cash = tempCash;
    }

    @Override
    public boolean isCashEnough(Integer amount) {
        return this.cash.getAmount() > amount;
    }

    @Override
    public void addCash(Integer banknote) {
        this.cash.setAmount(banknote);
    }
}
