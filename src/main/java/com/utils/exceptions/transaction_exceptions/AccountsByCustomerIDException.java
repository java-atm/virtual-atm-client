package com.utils.exceptions.transaction_exceptions;

public class AccountsByCustomerIDException extends TransactionException{
    public AccountsByCustomerIDException(String exceptionMsg) {
        super(exceptionMsg);
    }
}
