package com.utils.exceptions.transaction_exceptions;

public class AccountOwnerNameException extends TransactionException{
    public AccountOwnerNameException(String exceptionMsg) {
        super(exceptionMsg);
    }
}
