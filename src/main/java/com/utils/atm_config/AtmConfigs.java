package com.utils.atm_config;

import com.utils.cash.RealCash;
import com.utils.enums.AccountCurrency;
import com.utils.exceptions.InvalidConfigFileException;
import com.utils.exceptions.InvalidJSONForRealCash;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class AtmConfigs {
    private static final Logger LOGGER = LogManager.getLogger(AtmConfigs.class);
    private final String CASH_FILE_PATH;
    private RealCash currentCash;
    private final String atmId;
    private JSONObject cashJson;

    public AtmConfigs(String atmId) throws InvalidConfigFileException {
        this.atmId = atmId;
        CASH_FILE_PATH = String.format("./.%s_cash_holder.json", atmId);
        File cashHolder = new File(CASH_FILE_PATH);
        if (cashHolder.exists() && ! cashHolder.isFile()) {
            throw new InvalidConfigFileException("Config file does not exist or is not a proper file.");
        }
        if (cashHolder.exists() && cashHolder.isFile()) {
            try {
                LOGGER.info("Loading cash holder.");
                FileReader jsonStringReader = new FileReader(cashHolder);
                Scanner s = new Scanner(jsonStringReader);
                s.useDelimiter("\\Z");
                String content = s.next();
                cashJson = new JSONObject(content);
                currentCash = new RealCash(cashJson);
                dumpCurrentCash(currentCash);
            } catch (NoSuchElementException | IOException | JSONException | InvalidJSONForRealCash e) {
                throw new InvalidConfigFileException("FILE NOT FOUND, INVALID JSON OR PARAMETER MISSING: " + e.getMessage());
            }
        } else {
            currentCash = new RealCash(0);
            cashJson = currentCash.toJson();
            LOGGER.info("Creating empty ATM");
            dumpCurrentCash(currentCash);
        }
    }

    public String getAtmId() {
        return atmId;
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
