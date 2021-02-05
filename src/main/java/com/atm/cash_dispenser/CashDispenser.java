package com.atm.cash_dispenser;

import com.RealCash;
import com.utils.enums.Banknote;
import com.utils.exceptions.atm_exceptions.CashNotEnoughException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CashDispenser implements CashDispenserInterface{

    private static final Logger LOGGER = LogManager.getLogger(CashDispenser.class);

    private RealCash cash;

    public CashDispenser(RealCash initialCash) {
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
        LOGGER.info("Start dispenseCash. Amount: '{}'", amount);
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
            LOGGER.warn("Cash not enough in ATM");
            throw new CashNotEnoughException("Cash not enough. Please call maintenance.");
        }
        LOGGER.info("Cash is dispensed");
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
