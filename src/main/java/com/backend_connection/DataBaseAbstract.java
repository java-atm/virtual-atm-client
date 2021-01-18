package com.backend_connection;

import com.Cash;
import com.Customer;
import com.utils.enums.CardInfo;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public abstract class DataBaseAbstract implements DataBaseInterface{
    public final ArrayList<Customer> customers;
    public final LinkedHashMap<String, Cash> account_balances;
    public LinkedHashMap<LinkedHashMap<CardInfo, String>, Customer> customer_cards;
    public LinkedHashMap<LinkedHashMap<CardInfo, String>, String> card_pins;


    private static final int INITIAL_CUSTOMER_COUNT = 10;

    public DataBaseAbstract() {
        customers = new ArrayList<>(INITIAL_CUSTOMER_COUNT);
        account_balances = new LinkedHashMap<>();
        customer_cards = new LinkedHashMap<>();
        card_pins = new LinkedHashMap<>();
    }

    private ArrayList<Customer> getCustomers() {
        return customers;
    }
    public void addCustomer(Customer newCustomer) {
        customers.add(newCustomer);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

}
