package com.accounts;

import com.utils.enums.AccountCurrency;
import com.utils.enums.AccountType;

public class CurrentAccount extends Account{
    public CurrentAccount(String accountNumber, AccountCurrency accountCurrency) {
        super(accountNumber, AccountType.CURRENT, accountCurrency);
    }
}
