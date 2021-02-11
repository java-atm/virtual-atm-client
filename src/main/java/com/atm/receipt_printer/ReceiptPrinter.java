package com.atm.receipt_printer;

import com.utils.enums.Action;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReceiptPrinter {
    private static final Logger LOGGER = LogManager.getLogger(ReceiptPrinter.class);

    public static void printReceipt(JSONObject transactionInfo) {
        LOGGER.info("Start printing receipt");

        try (PrintWriter receiptPrinter = new PrintWriter(String.format("%s.txt", transactionInfo.get("transactionID")));) {
            receiptPrinter.print("JAVA-ATM-THE-KINDEST-BANK\n");
            receiptPrinter.print("ADDRESS: YOUR SWEET HOME AND IP ADDRESS:) " + getIP() + "\n");
            receiptPrinter.print("CONTACTS:\nPM: ARTAK KIRAKOSYAN +374-(99)-099-448\n");
            receiptPrinter.print("DEVELOPER: EDUARD MATVEEV +374-(55)-033-354\n");
            DateTimeFormatter d = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            receiptPrinter.print("DATE TIME: " + LocalDateTime.now().format(d) + "\n");
            receiptPrinter.print("CARD: " + transactionInfo.get("card") + "\n");
            receiptPrinter.print("ATM ID: " + transactionInfo.get("atm_id") + "\n");
            receiptPrinter.print("TRANSACTION ID: " + transactionInfo.get("transactionID") + "\n");
            String trsType = transactionInfo.get("transactionType").toString();
            receiptPrinter.print("SERVICE: " + trsType + "\n");
            if(trsType.equals(Action.DEPOSIT.name())
                    || trsType.equals(Action.WITHDRAW.name())
                    || trsType.equals(Action.TRANSFER.name())) {
                receiptPrinter.print("AMOUNT: " + transactionInfo.get("transactionAmount") + "\n");
            }
            receiptPrinter.print("!!! THANK YOU !!!");
            LOGGER.info("Finish printing receipt");
        } catch (FileNotFoundException e) {
            LOGGER.error("receipt.txt FILE NOT FOUND", e);
        }
    }

    private static String getIP() {
        LOGGER.info("Getting User IP ADDRESS");
        try {
            URL whatIsMyIp = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatIsMyIp.openStream()));
            LOGGER.info("User IP ADDRESS is gotten");
            return in.readLine();
        } catch (IOException e) {
            LOGGER.error("Something went wrong during getting an IP");
            return "YOU ARE A HACKER ! ! !";
        }
    }
}
