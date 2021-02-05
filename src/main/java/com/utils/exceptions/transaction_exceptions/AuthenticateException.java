package com.utils.exceptions.transaction_exceptions;

public class AuthenticateException extends TransactionException{
    public AuthenticateException(String exceptionMsg) {
        super(exceptionMsg);
    }
}
