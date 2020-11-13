package com.accounts;

import com.utils.enums.AccountCurrency;
import com.utils.enums.AccountType;

public class SavingsAccount extends Account{
    public SavingsAccount(String accountNumber, AccountCurrency accountCurrency) {
        super(accountNumber, AccountType.SAVINGS, accountCurrency);
    }
}
