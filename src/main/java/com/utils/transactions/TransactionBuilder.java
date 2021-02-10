package com.utils.transactions;

public class TransactionBuilder {
    private final Transactions newTransactions = new Transactions();

    public TransactionBuilder() {
    }

    public TransactionBuilder setATM_ID(String ATM_ID) {
        newTransactions.setAtm_id(ATM_ID);
        return this;
    }

    public TransactionBuilder setAmount(String amount) {
        newTransactions.setTransactionAmount(amount);
        return this;
    }

    public TransactionBuilder setCustomerID(String id) {
        newTransactions.setCustomerID(id);
        return this;
    }

    public TransactionBuilder setTransactionType(String type) {
        newTransactions.setTransactionType(type);
        return this;
    }

    public TransactionBuilder setToAccount(String toAccount) {
        newTransactions.setToAccount(toAccount);
        return this;
    }

    public TransactionBuilder setFromAccount(String fromAccount) {
        newTransactions.setFromAccount(fromAccount);
        return this;
    }

    public TransactionBuilder setTransactionID(String id) {
        newTransactions.setTransactionID(id);
        return this;
    }

    public TransactionBuilder setCardNumber(String cardNumber) {
        newTransactions.setCard(cardNumber);
        return this;
    }

    public Transactions buildTransaction() {
        return newTransactions;
    }
}
