package com.utils.exceptions.transaction_exceptions;

public class WithdrawTransactionException extends TransactionException{
    public WithdrawTransactionException(String exceptionMsg) {
        super(exceptionMsg);
    }
}
