package com.backend_connection;

import com.utils.card.Card;
import com.utils.enums.AccountCurrency;
import com.utils.exceptions.JsonToMapException;
import com.utils.exceptions.ReadResponseDataException;
import com.utils.exceptions.transaction_exceptions.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;

public class BackendConnection implements BackendConnectionInterface {

    private static final Logger LOGGER = LogManager.getLogger(BackendConnection.class);

    private final static String CURRENCY = AccountCurrency.AMD.getCurrency();
    private final String somethingWentWrongMsg = "Something went wrong";
    private final String BASE_URL = "http://ec2-3-129-17-241.us-east-2.compute.amazonaws.com:8080/backend/";

    public String authenticate(String ATM_ID, Card card, String pin) throws AuthenticateException {
        try {
            LOGGER.info("Start performing authentication");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("atm_id", ATM_ID);
            jsonObject.put("card_info", card.getIDENTIFICATION_INFO());
            jsonObject.put("pin", pin);
            String query = BASE_URL + "auth";
            return connection(query, jsonObject);
        } catch (ConnectException ex) {
            LOGGER.error("AUTHENTICATION ERROR");
            throw new AuthenticateException(ex.getMessage());
        }
    }

    public HashMap<String, BigDecimal> checkBalance(String ATM_ID, String customerID) throws CheckBalanceTransactionException {
        try {
            LOGGER.info("Start performing check balance");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("atm_id", ATM_ID);
            jsonObject.put("customerID", customerID);
            String query = BASE_URL + "checkBalance";
            String balancesInJsonString = connection(query, jsonObject);
            return jsonToMap(balancesInJsonString);
        } catch (ConnectException | JsonToMapException ex) {
            LOGGER.error("CHECK BALANCE TRANSACTION ERROR");
            throw new CheckBalanceTransactionException(ex.getMessage());
        }
    }

    public void withdraw(String ATM_ID, String accountNumber, BigDecimal amount) throws WithdrawTransactionException {
        try {
            LOGGER.info("Start performing withdraw");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("atm_id", ATM_ID);
            jsonObject.put("accountNumber", accountNumber);
            jsonObject.put("amount", amount);
            jsonObject.put("currency", CURRENCY);
            String query = BASE_URL + "withdraw";
            connection(query, jsonObject);
        } catch (ConnectException ex) {
            LOGGER.error("WITHDRAW TRANSACTION ERROR");
            throw new WithdrawTransactionException(ex.getMessage());
        }
    }


    public void deposit(String ATM_ID, String accountNumber, BigDecimal amount) throws DepositTransactionException {
        try {
            LOGGER.info("Start performing deposit");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("atm_id", ATM_ID);
            jsonObject.put("accountNumber", accountNumber);
            jsonObject.put("amount", amount);
            jsonObject.put("currency", CURRENCY);
            String query = BASE_URL + "deposit";
            connection(query, jsonObject);
        } catch (ConnectException ex) {
            LOGGER.error("DEPOSIT TRANSACTION ERROR");
            throw new DepositTransactionException(ex.getMessage());
        }
    }

    public void changePIN(String ATM_ID, String cardNumber, String newPIN) throws ChangePINTransactionException {
        try {
            LOGGER.info("Start performing changePIN");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("atm_id", ATM_ID);
            jsonObject.put("cardNumber", cardNumber);
            jsonObject.put("newPin", newPIN);
            String query = BASE_URL + "changePin";
            connection(query, jsonObject);
        } catch (ConnectException ex) {
            LOGGER.error("CHANGE PIN TRANSACTION ERROR");
            throw new ChangePINTransactionException(ex.getMessage());
        }
    }

    public void transfer(String ATM_ID, String fromAccount, String toAccount, String amountForTransfer) throws TransferTransactionException{
        try {
            LOGGER.info("Start performing transfer");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("atm_id", ATM_ID);
            jsonObject.put("from", fromAccount);
            jsonObject.put("to", toAccount);
            jsonObject.put("currency", CURRENCY);
            jsonObject.put("amount", amountForTransfer);
            String query = BASE_URL + "transfer";
            connection(query, jsonObject);
        } catch (ConnectException ex) {
            LOGGER.error("TRANSFER TRANSACTION ERROR");
            throw  new TransferTransactionException(ex.getMessage());
        }
    }

    public String getAccountOwnerName(String ATM_ID, String toAccount) throws AccountOwnerNameException {
        try {
            LOGGER.info("Start performing to get account owner name");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("atm_id", ATM_ID);
            jsonObject.put("accountNumber", toAccount);
            String query = BASE_URL + "getCustomerName";
            return connection(query, jsonObject);
        } catch (ConnectException ex) {
            LOGGER.error("GET ACCOUNT OWNER NAME TRANSACTION ERROR");
            throw new AccountOwnerNameException(ex.getMessage());
        }
    }

    public HashMap<String, BigDecimal> getAccountsByCustomerID(String ATM_ID, String customerID, boolean includeBalances) throws AccountsByCustomerIDException {
        try {
            LOGGER.info("Start performing to get accounts by customer ID");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("atm_id", ATM_ID);
            jsonObject.put("customerID", customerID);
            jsonObject.put("includeBalances", includeBalances);
            String query = BASE_URL + "getAccountsByCustomerID";
            String accountsInJsonString = connection(query, jsonObject);
            return jsonToMap(accountsInJsonString);
        } catch (ConnectException | JsonToMapException ex) {
            LOGGER.error("GET ACCOUNTS BY CUSTOMER ID TRANSACTION ERROR");
            throw new AccountsByCustomerIDException(ex.getMessage());
        }
    }

    private  HashMap<String, BigDecimal> jsonToMap(String jsonStringToHashMap) throws JsonToMapException {
        LOGGER.info("Start preparing map from jsonString");
        try {
            JSONObject json = new JSONObject(jsonStringToHashMap);
            Iterator<String> keys = json.keys();
            HashMap<String, BigDecimal> accountsBalancesMap = new HashMap<>();
            while (keys.hasNext()) {
                String key = keys.next();
                try {
                    BigDecimal balance = new BigDecimal(json.get(key).toString());
                    accountsBalancesMap.put(key, balance);
                } catch (NumberFormatException ex) {
                    LOGGER.warn("Number is not valid representation of BigDecimal: '{}'", json.get(key).toString(), ex);
                    throw new JsonToMapException(somethingWentWrongMsg);
                }
            }
            LOGGER.info("Map is prepared");
            return accountsBalancesMap;
        } catch (JSONException jsonException) {
            LOGGER.error("String doesn't match json format: '{}'", jsonStringToHashMap, jsonException);
            throw new JsonToMapException(jsonStringToHashMap);
        }
    }

    private String connection(String query, JSONObject jsonObject) throws ConnectException {
        LOGGER.info("Performing connection with DB");
        int responseCode = -1;
        try {
            LOGGER.info("Establishing connection with Server");
            HttpURLConnection connection = (HttpURLConnection) new URL(query).openConnection();
            try(AutoCloseable autoCloseable = connection::disconnect) {
                connection.setConnectTimeout(2000);
                connection.setReadTimeout(2000);
                connection.setDoOutput(true);
                connection.getOutputStream().write(jsonObject.toString().getBytes());
                connection.connect();
                LOGGER.info("Connection open");
                BufferedReader responseReader;
                JSONObject responseJson;
                if((responseCode = connection.getResponseCode()) == HttpURLConnection.HTTP_OK) {
                    LOGGER.info("Connection is established");
                    LOGGER.info("Preparing for getting valid response data from buffer");
                    responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    responseJson = new JSONObject(getResponseData(responseReader));
                    return responseJson.get("result").toString();
                } else if ((responseCode = connection.getResponseCode()) == HttpURLConnection.HTTP_ACCEPTED) {
                    LOGGER.info("Connection is established");
                    LOGGER.warn("Preparing for getting ERROR message FOR USER from response data from buffer");
                    responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    responseJson = new JSONObject(getResponseData(responseReader));
                    throw new TransactionException(responseJson.get("error").toString());
                } else if ((responseCode = connection.getResponseCode()) == HttpURLConnection.HTTP_FORBIDDEN) {
                    LOGGER.info("Connection is established");
                    LOGGER.error("Preparing for getting ERROR message from response data from buffer");
                    responseReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    responseJson = new JSONObject(getResponseData(responseReader));
                    LOGGER.error("ATM_ID IS NOT VALID: {}", responseJson.get("error"));
                    throw new TransactionException("WE ARE CALLING THE POLICE ! ! ! UIIIUUUUIIIIIUUUU 1-02");
                } else if ((responseCode = connection.getResponseCode()) == HttpURLConnection.HTTP_BAD_REQUEST) {
                    LOGGER.info("Connection is established");
                    LOGGER.error("Preparing for getting ERROR message from response data from buffer");
                    responseReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    responseJson = new JSONObject(getResponseData(responseReader));
                    LOGGER.error("SOMEWHERE I MADE A MISTAKE: {}", responseJson.get("error"));
                    throw new TransactionException("please, try again\nIF THE PROBLEM PERSISTS CALL YOUR BANK");
                } else {
                    responseReader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                    responseJson = new JSONObject(getResponseData(responseReader));
                    LOGGER.error("Response code: '{}', message : '{}'", connection.getResponseCode(), responseJson.get("error"));
                    throw new Exception("please, try again\nIF THE PROBLEM PERSISTS CALL YOUR BANK");
                }
            }
        } catch (SocketTimeoutException ex) {
            LOGGER.error("CONNECTION TIME OUT", ex);
            throw new ConnectException(somethingWentWrongMsg);
        } catch (MalformedURLException ex) {
            LOGGER.error("INVALID URL: '{}'", query, ex);
            throw new ConnectException(somethingWentWrongMsg);
        } catch (Exception ex) {
            LOGGER.error("BAD REQUEST EXCEPTION: '{}', '{}'", responseCode, query, ex);
            throw new ConnectException(ex.getMessage());
        }
    }

    private String getResponseData(BufferedReader responseReader) throws ReadResponseDataException {
        StringBuilder responseData = new StringBuilder();
        String line;
        LOGGER.info("Start reading response data from buffer");
        try {
            while ((line = responseReader.readLine()) != null) {
                responseData.append(line);
            }
        } catch (IOException bufferReaderEx) {
            LOGGER.error("getResponseData(): '{}'", responseData, bufferReaderEx);
            throw new ReadResponseDataException(somethingWentWrongMsg);
        }
        LOGGER.info("Response data is read, '{}'", responseData);
        return  responseData.toString();
    }
}
