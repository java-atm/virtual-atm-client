package com.utils.transactions;

public class Transactions {

    private String ATM_ID = "NONE";
    private String customerID = "NONE";
    private String transactionType = "NONE";
    private String transactionID = "NONE";
    private String fromAccount = "NONE";
    private String toAccount = "NONE";
    private String transactionAmount = "NONE";

    Transactions() {
    }

    public void setATM_ID(String ATM_ID) {
        this.ATM_ID = ATM_ID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public void setFromAccount(String fromAccount) {
        this.fromAccount = fromAccount;
    }

    public void setToAccount(String toAccount) {
        this.toAccount = toAccount;
    }

    public void setTransactionAmount(String transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getATM_ID() {
        return ATM_ID;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public String getTransactionAmount() {
        return transactionAmount;
    }

    public static TransactionBuilder getTransactionBuilder() {
        return new TransactionBuilder();
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "ATM_ID='" + ATM_ID + '\'' +
                ", customerID='" + customerID + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", transactionID='" + transactionID + '\'' +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", transactionAmount='" + transactionAmount + '\'' +
                '}';
    }
}
