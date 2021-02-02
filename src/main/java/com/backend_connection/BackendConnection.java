package com.backend_connection;

import com.Card;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

public class BackendConnection implements BackendConnectionInterface {

    private static final Logger LOGGER = LogManager.getLogger(BackendConnection.class);

    private final static String CURRENCY = "AMD";
    private final String somethingWentWrongMsg = "Something went wrong";

    public String authenticate(String ATM_ID, Card card, String pin) throws Exception {
        LOGGER.info("Start performing authentication");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("atm_id", ATM_ID);
        jsonObject.put("card_info", card.getIDENTIFICATION_INFO());
        jsonObject.put("pin", pin);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/auth";

        return connection(query, jsonObject);
    }

    public HashMap<String, BigDecimal> checkBalance(String ATM_ID, String customerID) throws Exception {
        LOGGER.info("Start performing check balance");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("atm_id", ATM_ID);
        jsonObject.put("customerID", customerID);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/checkBalance";

        String balancesInJsonString = connection(query, jsonObject);
        return jsonToMap(balancesInJsonString);
    }

    public void withdraw(String ATM_ID, String accountNumber, BigDecimal amount) throws Exception {
        LOGGER.info("Start performing withdraw");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("atm_id", ATM_ID);
        jsonObject.put("accountNumber", accountNumber);
        jsonObject.put("amount", amount);
        jsonObject.put("currency", CURRENCY);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/withdraw";

        connection(query, jsonObject);
    }


    public void deposit(String ATM_ID, String accountNumber, BigDecimal amount) throws Exception {
        LOGGER.info("Start performing deposit");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("atm_id", ATM_ID);
        jsonObject.put("accountNumber", accountNumber);
        jsonObject.put("amount", amount);
        jsonObject.put("currency", CURRENCY);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/deposit";

        connection(query, jsonObject);
    }

    public void changePIN(String ATM_ID, String cardNumber, String newPIN) throws Exception {
        LOGGER.info("Start performing changePIN");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("atm_id", ATM_ID);
        jsonObject.put("cardNumber", cardNumber);
        jsonObject.put("newPin", newPIN);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/changePin";

        connection(query, jsonObject);
    }

    public void transfer(String ATM_ID, String fromAccount, String toAccount, String amountForTransfer) throws Exception{
        LOGGER.info("Start performing transfer");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("atm_id", ATM_ID);
        jsonObject.put("from", fromAccount);
        jsonObject.put("to", toAccount);
        jsonObject.put("currency", CURRENCY);
        jsonObject.put("amount", amountForTransfer);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/transfer";
        connection(query, jsonObject);
    }

    public String getAccountOwnerName(String ATM_ID, String toAccount) throws Exception {
        LOGGER.info("Start performing to get account owner name");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("atm_id", ATM_ID);
        jsonObject.put("accountNumber", toAccount);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/getCustomerName";
        return connection(query, jsonObject);
    }

    public HashMap<String, BigDecimal> getAccountsByCustomerID(String ATM_ID, String customerID, boolean includeBalances) throws Exception {
        LOGGER.info("Start performing to get accounts by customer ID");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("atm_id", ATM_ID);
        jsonObject.put("customerID", customerID);
        jsonObject.put("includeBalances", includeBalances);
        String query = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/getAccountsByCustomerID";

        String accountsInJsonString = connection(query, jsonObject);
        return jsonToMap(accountsInJsonString);
    }

    private  HashMap<String, BigDecimal> jsonToMap(String jsonStringToHashMap) throws Exception {
        LOGGER.info("Start preparing map from json");
        try {
            JSONObject json = new JSONObject(jsonStringToHashMap);
            Iterator<String> keys = json.keys();
            HashMap<String, BigDecimal> accountsBalancesMap = new HashMap<>();
            while (keys.hasNext()) {
                String key = keys.next();
                try {
                    BigDecimal balance = new BigDecimal(json.get(key).toString());
                    accountsBalancesMap.put(key, balance);
                } catch (NumberFormatException exception) {
                    LOGGER.warn("Number is not valid representation of BigDecimal: {}", json.get(key).toString());
                    throw new Exception(somethingWentWrongMsg);
                }
            }
            return accountsBalancesMap;
        } catch (JSONException jsonException) {
            LOGGER.error("String doesn't match json format: {}", jsonStringToHashMap);
            throw new Exception(jsonStringToHashMap);
        }
    }

    private String connection(String query, JSONObject jsonObject) throws Exception {
        LOGGER.info("Performing connection with DB");
        HttpURLConnection connection = (HttpURLConnection) new URL(query).openConnection();
        try(AutoCloseable autoCloseable = connection::disconnect) {

            connection.setConnectTimeout(1000);
            connection.setReadTimeout(1000);
            connection.setDoOutput(true);
            connection.getOutputStream().write(jsonObject.toString().getBytes());
            connection.connect();

            BufferedReader responseReader;
            if(HttpURLConnection.HTTP_OK == connection.getResponseCode()) {
                LOGGER.info("Connection is established");
                LOGGER.info("Preparing for getting response data from buffer");
                responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                return getResponseData(responseReader);
            } else {
                //responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                LOGGER.error("Response code: '{}', message : '{}'", connection.getResponseCode(), connection.getResponseMessage());
                throw new Exception(somethingWentWrongMsg);
            }
        } catch (SocketTimeoutException ex) {
            LOGGER.error("Connection time out", ex);
            throw new IOException(somethingWentWrongMsg);
        }
    }

    private String getResponseData(BufferedReader responseReader) throws IOException {
        StringBuilder responseData = new StringBuilder();
        String line;
        LOGGER.info("Start reading response data from buffer");
        try {
            while ((line = responseReader.readLine()) != null) {
                responseData.append(line);
            }
        } catch (IOException bufferReaderEx) {
            LOGGER.error(bufferReaderEx);
            LOGGER.error("getResponseData(): {}", responseReader.readLine());
            throw new IOException(somethingWentWrongMsg);
        }
        LOGGER.info("Response data is read");
        return  responseData.toString();
    }
}
