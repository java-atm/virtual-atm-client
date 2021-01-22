package com;

public class Customer extends CustomerAbstract{
    public Customer(String customerID, String phoneNumber, String email, String name, String surname) {
        super(customerID, phoneNumber, email, name, surname);
    }

    public Customer(String customerID, String name, String surname) {
        this(customerID, null, null, name, surname);
    }
}
