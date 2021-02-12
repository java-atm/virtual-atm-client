package com.utils.atm_config;

import com.utils.cash.RealCash;
import com.utils.enums.AccountCurrency;
import com.utils.exceptions.InvalidConfigFileException;
import com.utils.exceptions.InvalidJSONForRealCash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class AtmConfigs {
    private static final Logger LOGGER = LogManager.getLogger(AtmConfigs.class);
    private final String CONFIG_FILE_PATH;
    private final String CASH_FILE_PATH;
    private final String atmId;
    private final String backendBaseURL;
    private final AccountCurrency currency;
    private RealCash currentCash;

    private JSONObject cashJson;
    private JSONObject atmConfigJson;

    public AtmConfigs(String filePath, String cashFilePath) throws InvalidConfigFileException {
        CONFIG_FILE_PATH = filePath;
        CASH_FILE_PATH = cashFilePath;

        try {
            FileReader jsonStringReader = new FileReader(CONFIG_FILE_PATH);
            Scanner s = new Scanner(jsonStringReader);
            s.useDelimiter("\\Z");
            String content = s.next();
            atmConfigJson = new JSONObject(content);
            atmId = atmConfigJson.getString("atm_id");
            backendBaseURL = atmConfigJson.getString("backend_base_url");
            currency = AccountCurrency.valueOf(atmConfigJson.getString("currency"));
        } catch (NoSuchElementException| IOException | JSONException e) {
            throw new InvalidConfigFileException("FILE NOT FOUND, INVALID JSON OR PARAMETER MISSING: " + e.getMessage());
        }

        try {
            FileReader jsonStringReader = new FileReader(CASH_FILE_PATH);
            Scanner s = new Scanner(jsonStringReader);
            s.useDelimiter("\\Z");
            String content = s.next();
            cashJson = new JSONObject(content);
            currentCash = new RealCash(cashJson);
        } catch (NoSuchElementException | IOException | JSONException | InvalidJSONForRealCash e) {
            throw new InvalidConfigFileException("FILE NOT FOUND, INVALID JSON OR PARAMETER MISSING: " + e.getMessage());
        }
    }

    public String getAtmId() {
        return atmId;
    }

    public String getBackendBaseURL() {
        return backendBaseURL;
    }

    public AccountCurrency getCurrency() {
        return currency;
    }

    public RealCash getCurrentCash() {
        return currentCash;
    }

    public void dumpCurrentCash(RealCash newCash) {
        currentCash = newCash;
        try {
            FileWriter fileWriter = new FileWriter(CASH_FILE_PATH);
            fileWriter.write(currentCash.toJson().toString());
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.fatal("Failed to dump new cash '{}': {}", newCash, e.getMessage(), e);
        }
    }


}
