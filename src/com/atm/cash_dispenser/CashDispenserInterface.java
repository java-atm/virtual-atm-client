package com.atm.cash_dispenser;

import com.Cash;
import com.RealCash;

public interface CashDispenserInterface {

    void setInitialCash(RealCash initialCash);

    boolean isCashEnough(Double cash);

    void dispenseCash(Double cash) throws Exception;

    void addCash(Double cash);
}
