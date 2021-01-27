package com.atm.card_reader;

import com.Card;
import com.utils.enums.CardInfo;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public interface CardReaderInterface {

    Card getCard() throws NullPointerException;

    void readCard() throws CardIsInvalidException;

    void ejectCard();
}
