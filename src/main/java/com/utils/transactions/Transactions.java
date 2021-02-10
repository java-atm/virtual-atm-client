package com.utils.transactions;

public class Transactions {

    private String atm_id = "NONE";
    private String customerID = "NONE";
    private String transactionType = "NONE";
    private String transactionID = "NONE";
    private String fromAccount = "NONE";
    private String toAccount = "NONE";
    private String transactionAmount = "NONE";
    private String card = "NONE";

    Transactions() {
    }

    public void setAtm_id(String atm_id) {
        this.atm_id = atm_id;
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

    public void setCard(String card) {
        this.card = card;
    }

    public String getAtm_id() {
        return atm_id;
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

    public String getCard() {
        return card;
    }

    public static TransactionBuilder getTransactionBuilder() {
        return new TransactionBuilder();
    }

    @Override
    public String toString() {
        return "Transactions{" +
                "ATM_ID='" + atm_id + '\'' +
                ", customerID='" + customerID + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", transactionID='" + transactionID + '\'' +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", transactionAmount='" + transactionAmount + '\'' +
                '}';
    }
}
