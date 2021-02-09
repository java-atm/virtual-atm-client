package com.atm.cash_dispenser;

import com.utils.cash.RealCash;
import com.utils.exceptions.atm_exceptions.CashNotEnoughException;

public interface CashDispenserInterface {

    void setInitialCash(RealCash initialCash);

    boolean isCashEnough(Double amount);

    void dispenseCash(Double cash) throws CashNotEnoughException;

    void addCash(Double cash);
}
