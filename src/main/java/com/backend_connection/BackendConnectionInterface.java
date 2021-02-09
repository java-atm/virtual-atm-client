package com.backend_connection;

import com.utils.card.Card;
import com.utils.exceptions.transaction_exceptions.*;

import java.math.BigDecimal;
import java.util.HashMap;

public interface BackendConnectionInterface {
    String authenticate(String ATM_ID, Card card, String pin) throws AuthenticateException;

    HashMap<String, BigDecimal> checkBalance(String ATM_ID, String customerID) throws CheckBalanceTransactionException;

    void withdraw(String ATM_ID, String accountNumber, BigDecimal amount) throws WithdrawTransactionException;

    void deposit(String ATM_ID, String accountNumber, BigDecimal amount) throws DepositTransactionException;

    void changePIN(String ATM_ID, String cardNumber, String newPIN) throws ChangePINTransactionException;

    void transfer(String ATM_ID, String fromAccount, String toAccount, String amountForTransfer) throws TransferTransactionException;

    String getAccountOwnerName(String ATM_ID, String toAccount) throws Exception;

    HashMap<String, BigDecimal> getAccountsByCustomerID(String ATM_ID, String customerID, boolean includeBalances) throws Exception;
}
