package com.accounts;

import com.utils.enums.AccountCurrency;
import com.utils.enums.AccountType;

public abstract class AccountAbstract implements AccountInterface{
    private final String ACCOUNT_NUMBER;
    private final AccountType ACCOUNT_TYPE;
    private final AccountCurrency ACCOUNT_CURRENCY;
    private final int ACCOUNT_NUMBER_LENGTH = 16;

    public AccountAbstract(String accountNumber, AccountType accountType, AccountCurrency accountCurrency) {
        if (accountNumber.length() != ACCOUNT_NUMBER_LENGTH) throw new RuntimeException("Incorrect account number length");
        this.ACCOUNT_NUMBER = accountNumber;
        this.ACCOUNT_TYPE = accountType;
        this.ACCOUNT_CURRENCY = accountCurrency;
    }

    public String getAccountNumber() {
        return ACCOUNT_NUMBER;
    }

    public AccountType getAccountType() {
        return ACCOUNT_TYPE;
    }

    public AccountCurrency getAccountCurrency() {
        return ACCOUNT_CURRENCY;
    }

    public String toString() {
        return ACCOUNT_CURRENCY.getCurrency() + " " + ACCOUNT_TYPE.getType() + "(" + ACCOUNT_NUMBER + ")";
    }
}
