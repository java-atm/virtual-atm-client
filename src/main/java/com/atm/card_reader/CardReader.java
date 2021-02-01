package com.atm.card_reader;

import com.Card;
import com.utils.enums.CardInfo;
import com.utils.exceptions.CardIsInvalidException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedHashMap;
import java.util.Scanner;

public class CardReader implements CardReaderInterface {

    public static final Logger LOGGER = LogManager.getLogger(CardReader.class);

    private Card card;
    private final Scanner scanner;

    public CardReader() {
        scanner = new Scanner(System.in);
        card = null;
    }

    @Override
    public Card getCard() throws NullPointerException {
        if (card == null) throw new NullPointerException("Invalid Card");
        return card;
    }

    @Override
    public void readCard() throws CardIsInvalidException {
        LOGGER.info("Waiting for reading card info");
        LinkedHashMap<CardInfo,String> cardIdentifyInfo = new LinkedHashMap<>();
        String info;
        for (CardInfo cardInfo : CardInfo.values()) {
            System.out.print("Insert your " + cardInfo + ": ");
            info = scanner.nextLine();
            LOGGER.info("{} : {}", cardInfo, info);
            cardIdentifyInfo.put(cardInfo, info);
        }
        checkCardIsValid(cardIdentifyInfo);
        LOGGER.info("Card info is read");
        card = new Card(cardIdentifyInfo);
    }

    private void checkCardIsValid(LinkedHashMap<CardInfo, String> cardIdentifyInfo) throws CardIsInvalidException {
        LOGGER.info("Checking card validity");
        int cardNumberLength = cardIdentifyInfo.get(CardInfo.CARD_NUMBER).length();
        if (cardNumberLength >= Card.ID_MAX_LENGTH || cardNumberLength <= Card.ID_MIN_LENGTH) {
            LOGGER.error("Card number: {}, length: {}", cardIdentifyInfo.get(CardInfo.CARD_NUMBER), cardNumberLength);
            throw new CardIsInvalidException("Invalid Card");
        }
    }

    @Override
    public void ejectCard() {
        card = null;
    }

    public void eatCard() {
        System.out.println("Please contact card service");
    }
}
