package com.utils.card;

import com.utils.enums.CardInfo;

import java.util.LinkedHashMap;

public class Card extends CardAbstract {

    public Card(LinkedHashMap<CardInfo, String> identification_info) {
        super(identification_info);
    }
}
