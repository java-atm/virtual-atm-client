package com;

import com.accounts.Account;

public interface CustomerInterface {
    Account getAccountByAccountNumber(String accountNumber);
}
