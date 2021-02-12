package com.utils.atm_config;

public interface AtmConfigInterface {

    boolean getCurrentCash();

    void addToCurrentCash();

    void addToCurrentCash(boolean amountToAdd);

    boolean subtractFromCurrentCash();
}
