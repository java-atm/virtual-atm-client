package com.atm.cash_dispenser;

import com.Cash;
import com.RealCash;

public interface CashDispenserInterface {

    public void setInitialCash(RealCash initialCash);

    public boolean isCashEnough(Integer cash);

    public void dispenseCash(Integer cash) throws Exception;

    public void addCash(Integer cash);
}
