package com.backend_connection;

import com.Card;
import com.Cash;
import com.Customer;
import com.accounts.Account;
import com.accounts.CardAccount;
import com.accounts.CurrentAccount;
import com.accounts.SavingsAccount;
import com.utils.enums.AccountCurrency;
import com.utils.enums.CardInfo;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class BackendConnection extends DataBaseAbstract{
    public BackendConnection() {
        super();
        //Admin
        LinkedHashMap<CardInfo, String> admin = new LinkedHashMap<>();
        admin.put(CardInfo.NAME, "Admin");
        admin.put(CardInfo.SURNAME, "Admin");
        admin.put(CardInfo.CARD_NUMBER, "000000000000000");
        Card adminCard = new Card(admin);
        Customer adminCustomer = new Customer("Admin", "Admin", "Admin@Admin", "Admin", "Admin");
        this.addCustomer(adminCustomer);
        card_pins.put(admin, "1111");
        customer_cards.put(admin, adminCustomer);

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

    public String authenticate(String ATM_ID, Card card, String pin) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("atm_id", ATM_ID);
        jsonObject.put("card_info", card.getIDENTIFICATION_INFO());
        jsonObject.put("pin", pin);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/auth";

        return connection(query, jsonObject);
    }

    public HashMap<String, BigDecimal> checkBalance(String ATM_ID, String customerID) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("atm_id", ATM_ID);
        jsonObject.put("customerID", customerID);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/checkBalance";

        String balancesInJsonString = connection(query, jsonObject);
        return jsonToMap(balancesInJsonString);
    }

    private  HashMap<String, BigDecimal> jsonToMap(String jsonStringToHashMap) {
        JSONObject json = new JSONObject(jsonStringToHashMap);
        Iterator<String> keys = json.keys();
        HashMap<String, BigDecimal> balancesMap = new HashMap<>();
        while (keys.hasNext()) {
            String key = keys.next();
            BigDecimal balance = new BigDecimal(json.get(key).toString());
            balancesMap.put(key, balance);
        }
        return balancesMap;
    }

    private String connection(String query, JSONObject jsonObject) throws Exception{
        HttpURLConnection connection = (HttpURLConnection) new URL(query).openConnection();
        try(AutoCloseable autoCloseable = connection::disconnect) {

            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.setDoOutput(true);
            connection.getOutputStream().write(jsonObject.toString().getBytes());
            connection.connect();

            if(HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                 BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                 return getResponseData(responseReader);
            } else {
                throw new CustomerNotFoundException("Customer not found in Database during connection");
            }
        } catch (Exception ex) {
            throw new Exception();
        }
    }

    private String getResponseData(BufferedReader responseReader) throws Exception {
        StringBuilder responseData = new StringBuilder();
        String line;
        while ((line = responseReader.readLine()) != null) {
            responseData.append(line);
        }
        return  responseData.toString();
    }

    public Customer findCustomerByID(String customer_ID) throws CustomerNotFoundException {
        for (Customer curr_customer : customers) {
            if (customer_ID.equals(curr_customer.getCustomerID())) {
                return curr_customer;
            }
        }
        throw new CustomerNotFoundException("Customer not found in the DB.");
    }

    public String getBalanceReport(String customer_ID) throws CustomerNotFoundException{
        Customer customer = findCustomerByID(customer_ID);
        String report = "Balance report for " + customer.getName() + " " + customer.getSurname() + ".\n";
        report += buildReportFromAccounts(customer.getAccounts());
        return report;
    }

    public String buildReportFromAccounts(ArrayList<Account> accounts) {
        StringBuilder balance_report = new StringBuilder();
        for (Account account : accounts) {
            Cash curr_balance = account_balances.get(account.getAccountNumber());
            if (curr_balance == null) {
                System.out.println("Account not found, skipping it");
                continue;
            } else {
                balance_report.append(account.toString()).append(curr_balance.toString()).append("\n");
            }
        }
        return balance_report.toString();
    }
}
