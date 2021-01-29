package com.backend_connection;

import com.Card;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class BackendConnection {
    private final static String CURRENCY = "AMD";

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

    public void withdraw(String accountNumber, BigDecimal amount) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountNumber", accountNumber);
        jsonObject.put("amount", amount);
        jsonObject.put("currency", CURRENCY);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/withdraw";

        try {
            connection(query, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deposit(String accountNumber, BigDecimal amount) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("accountNumber", accountNumber);
        jsonObject.put("amount", amount);
        jsonObject.put("currency", CURRENCY);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/deposit";

        connection(query, jsonObject);
    }

    public void changePIN(String cardNumber, String newPIN) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("cardNumber", cardNumber);
        jsonObject.put("newPin", newPIN);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/changePin";

        try {
            connection(query, jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void transfer(String fromAccount, String toAccount, String amountForTransfer) throws Exception{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("from", fromAccount);
        jsonObject.put("to", toAccount);
        jsonObject.put("currency", CURRENCY);
        jsonObject.put("amount", amountForTransfer);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/transfer";

        connection(query, jsonObject);
    }

    public HashMap<String, BigDecimal> getAccountsByCustomerID(String customerID, boolean includeBalances) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerID", customerID);
        jsonObject.put("includeBalances", includeBalances);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/getAccountsByCustomerID";

        String accountsInJsonString = connection(query, jsonObject);
        return jsonToMap(accountsInJsonString);
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

    private String connection(String query, JSONObject jsonObject) throws Exception {
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
                throw new Exception("Something went wrong during connection " + connection.getResponseCode());
            }
        } catch (IOException ex) {
            throw new IOException("Something went wrong, during read your data");
        }

    }

    private String getResponseData(BufferedReader responseReader) throws IOException {
        StringBuilder responseData = new StringBuilder();
        String line;
        while ((line = responseReader.readLine()) != null) {
            responseData.append(line);
        }
        return  responseData.toString();
    }

//    public Customer findCustomerByID(String customer_ID) throws CustomerNotFoundException {
//        for (Customer curr_customer : customers) {
//            if (customer_ID.equals(curr_customer.getCustomerID())) {
//                return curr_customer;
//            }
//        }
//        throw new CustomerNotFoundException("Customer not found in the DB.");
//    }

//    public String getBalanceReport(String customer_ID) throws CustomerNotFoundException{
//        Customer customer = findCustomerByID(customer_ID);
//        String report = "Balance report for " + customer.getName() + " " + customer.getSurname() + ".\n";
//        report += buildReportFromAccounts(customer.getAccounts());
//        return report;
//    }

//    public String buildReportFromAccounts(ArrayList<Account> accounts) {
//        StringBuilder balance_report = new StringBuilder();
//        for (Account account : accounts) {
//            Cash curr_balance = account_balances.get(account.getAccountNumber());
//            if (curr_balance == null) {
//                System.out.println("Account not found, skipping it");
//                continue;
//            } else {
//                balance_report.append(account.toString()).append(curr_balance.toString()).append("\n");
//            }
//        }
//        return balance_report.toString();
//    }
}
