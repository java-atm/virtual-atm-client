package com.backend_connection;

import com.Card;

import java.math.BigDecimal;
import java.util.HashMap;

public interface BackendConnectionInterface {
    String authenticate(String ATM_ID, Card card, String pin) throws Exception;

    HashMap<String, BigDecimal> checkBalance(String ATM_ID, String customerID) throws Exception;

    void withdraw(String ATM_ID, String accountNumber, BigDecimal amount) throws Exception;

    void deposit(String ATM_ID, String accountNumber, BigDecimal amount) throws Exception;

    void changePIN(String ATM_ID, String cardNumber, String newPIN) throws Exception;

    void transfer(String ATM_ID, String fromAccount, String toAccount, String amountForTransfer) throws Exception;

    String getToAccountOwnerName(String ATM_ID, String toAccount) throws Exception;

    HashMap<String, BigDecimal> getAccountsByCustomerID(String ATM_ID, String customerID, boolean includeBalances) throws Exception;
}
