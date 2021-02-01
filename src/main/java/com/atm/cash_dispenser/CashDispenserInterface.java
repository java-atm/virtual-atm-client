package com.atm.cash_dispenser;

import com.RealCash;
import com.utils.exceptions.CashNotEnoughException;

public interface CashDispenserInterface {

    void setInitialCash(RealCash initialCash);

    boolean isCashEnough(Double amount);

    void dispenseCash(Double cash) throws CashNotEnoughException;

    void addCash(Double cash);
}
