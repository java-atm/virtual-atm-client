package com.utils.customer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomerAbstract {
    private final String CUSTOMER_ID;
    private ArrayList<String> accounts;
    private String phoneNumber;
    private String email;
    private String name;
    private String surname;
    private static final int INITIAL_ACCOUNT_COUNT = 9;

    public CustomerAbstract(String customerID, String phoneNumber, String email, String name, String surname) {
        this.CUSTOMER_ID = customerID;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.name = name;
        this.surname = surname;
        accounts = new ArrayList<>(INITIAL_ACCOUNT_COUNT);
    }

    public String getCustomerID() {
        return CUSTOMER_ID;
    }

    public ArrayList<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(HashMap<String, BigDecimal> accounts) {
        for(Map.Entry<String, BigDecimal> account : accounts.entrySet()) {
            this.accounts.add(account.getKey());
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAccountByAccountNumber(int accountNumberIndex) {
        return accounts.get(accountNumberIndex);
    }
}
