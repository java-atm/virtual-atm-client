package com.utils.exceptions.transaction_exceptions;

import com.utils.exceptions.BaseException;

public class TransactionException extends BaseException {
    public TransactionException(String exceptionMsg) {
        super(exceptionMsg);
    }
}
