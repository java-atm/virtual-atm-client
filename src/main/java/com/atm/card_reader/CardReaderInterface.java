package com.atm.card_reader;

import com.utils.card.Card;
import com.utils.exceptions.atm_exceptions.CardIsInvalidException;

public interface CardReaderInterface {

    Card getCard() throws NullPointerException;

    void readCard() throws CardIsInvalidException;

    void ejectCard();
}
