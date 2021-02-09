package com.utils.card;

import com.utils.enums.CardInfo;

import java.util.LinkedHashMap;

public interface CardInterface {

    String getCardNumber();

    String getName();

    String getSurname();

    LinkedHashMap<CardInfo, String> getIDENTIFICATION_INFO();
}
