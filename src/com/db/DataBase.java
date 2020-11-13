package com.db;

import com.*;

import com.accounts.Account;
import com.accounts.CardAccount;
import com.accounts.CurrentAccount;
import com.accounts.SavingsAccount;
import com.utils.enums.AccountCurrency;
import com.utils.enums.CardInfo;

import java.util.LinkedHashMap;

public class DataBase extends DataBaseAbstract{
    public DataBase() {
        super();
        LinkedHashMap<CardInfo, String> c1 = new LinkedHashMap<>();
        c1.put(CardInfo.NAME, "Artak");
        c1.put(CardInfo.SURNAME, "Kirakosyan");
        c1.put(CardInfo.CARD_NUMBER, "1234567891123111");
        Card card1 = new Card(c1);

        CardAccount acc1 = new CardAccount("1234567890123456", AccountCurrency.AMD, card1);
        CurrentAccount acc2 = new CurrentAccount("1234567890123457", AccountCurrency.AMD);
        Customer customer1 = new Customer("ARTAK_01", "+37499099448", "artakkirakosyan96@mail.ru", "Artak", "Kirakosyan");
        customer1.addAccount(acc1);
        customer1.addAccount(acc2);

        LinkedHashMap<CardInfo, String> c2 = new LinkedHashMap<>();
        c2.put(CardInfo.NAME, "Edo");
        c2.put(CardInfo.SURNAME, "Matveev");
        c2.put(CardInfo.CARD_NUMBER, "1234567891123112");
        Card card2 = new Card(c2);

        CardAccount acc3 = new CardAccount("1234567890123458", AccountCurrency.AMD, card2);
        CurrentAccount acc4 = new CurrentAccount("1234567890123459", AccountCurrency.AMD);
        SavingsAccount acc5 = new SavingsAccount("1234567890123460", AccountCurrency.AMD);
        Customer customer2 = new Customer("EDO_02", "+37455033354", "dev.edomatveev@gmail.com", "Edo", "Matveev");
        customer2.addAccount(acc3);
        customer2.addAccount(acc4);
        customer2.addAccount(acc5);

        LinkedHashMap<CardInfo, String> c3 = new LinkedHashMap<>();
        c3.put(CardInfo.NAME, "Tatev");
        c3.put(CardInfo.SURNAME, "Khachaturyan");
        c3.put(CardInfo.CARD_NUMBER, "9994567891123112");
        Card card3 = new Card(c3);

        LinkedHashMap<CardInfo, String> c4 = new LinkedHashMap<>();
        c4.put(CardInfo.NAME, "Tatev");
        c4.put(CardInfo.SURNAME, "Khachaturyan");
        c4.put(CardInfo.CARD_NUMBER, "9994567891123333");
        Card card4 = new Card(c4);

        CardAccount acc6 = new CardAccount("9994567890123456", AccountCurrency.AMD, card3);
        CardAccount acc7 = new CardAccount("9994567890123999", AccountCurrency.AMD, card4);
        CurrentAccount acc8 = new CurrentAccount("9994567890123457", AccountCurrency.AMD);
        SavingsAccount acc9 = new SavingsAccount("9994567890123458", AccountCurrency.AMD);

        Customer customer3 = new Customer("TATEV_03", "+37499999999", "tatev@tatev.com", "Tatev", "Khachaturyan");
        customer3.addAccount(acc6);
        customer3.addAccount(acc7);
        customer3.addAccount(acc8);
        customer3.addAccount(acc9);

        this.addCustomer(customer1);
        this.addCustomer(customer2);
        this.addCustomer(customer3);

        card_pins.put(c1, "1234");
        card_pins.put(c2, "4321");
        card_pins.put(c3, "9999");
        card_pins.put(c4, "9911");

        customer_cards.put(c1, customer1);
        customer_cards.put(c2, customer2);
        customer_cards.put(c3, customer3);
        customer_cards.put(c4, customer3);

        account_balances.put(acc1.getAccountNumber(), new Cash(10000));
        account_balances.put(acc2.getAccountNumber(), new Cash(1000));
        account_balances.put(acc3.getAccountNumber(), new Cash(100));
        account_balances.put(acc4.getAccountNumber(), new Cash(10));
        account_balances.put(acc5.getAccountNumber(), new Cash(333));
        account_balances.put(acc6.getAccountNumber(), new Cash(4325));
        account_balances.put(acc7.getAccountNumber(), new Cash(9284));
        account_balances.put(acc8.getAccountNumber(), new Cash(12345));
        account_balances.put(acc9.getAccountNumber(), new Cash(745));
    }

    public Customer getCustomer(Card card, String pin) throws IncorrectPinException, CustomerNotFoundException {
        String true_pin = card_pins.get(card.getIDENTIFICATION_INFO());
        if (pin.equals(true_pin)) {
            Customer customer = customer_cards.get(card.getIDENTIFICATION_INFO());
            if (customer == null) {
                throw new CustomerNotFoundException("Customer not found");
            }
            return customer;
        } else {
            throw new IncorrectPinException("Pins didn't match");
        }
    }

    public String getBalanceReport(Customer customer) throws CustomerNotFoundException {
        Customer cust = null;
        for (Customer curr_customer : customers) {
            if (customer.getCustomerID().equals(curr_customer.getCustomerID())) {
                cust = curr_customer;
                break;
            }
        }
        if (cust == null) {
            throw new CustomerNotFoundException("Customer not found in the DB.");
        }
        StringBuilder balance_report = new StringBuilder("Balance report for " + cust.getName() + " " + cust.getSurname() + ".\n");
        for (Account account : cust.getAccounts()) {
            Cash curr_balance = account_balances.get(account.getAccountNumber());
            balance_report.append(account.toString()).append(curr_balance.toString()).append("\n");
        }
        return balance_report.toString();
    }
}
