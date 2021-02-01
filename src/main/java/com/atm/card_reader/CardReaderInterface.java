package com.atm.card_reader;

import com.Card;
import com.utils.exceptions.CardIsInvalidException;

public interface CardReaderInterface {

    Card getCard() throws NullPointerException;

    void readCard() throws CardIsInvalidException;

    void ejectCard();
}
