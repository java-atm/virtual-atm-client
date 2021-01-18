package com.accounts;

import com.utils.enums.AccountCurrency;
import com.utils.enums.AccountType;

public class Account extends AccountAbstract{
    public Account(String accountNumber, AccountType accountType, AccountCurrency accountCurrency) {
        super(accountNumber, accountType, accountCurrency);
    }
}
