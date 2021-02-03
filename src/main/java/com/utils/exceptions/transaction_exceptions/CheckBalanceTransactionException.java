package com.utils.exceptions.transaction_exceptions;

public class CheckBalanceTransactionException extends TransactionException{
    public CheckBalanceTransactionException(String exceptionMsg) {
        super(exceptionMsg);
    }
}
