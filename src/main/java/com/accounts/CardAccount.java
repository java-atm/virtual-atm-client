package com.accounts;

import com.Card;
import com.utils.enums.AccountCurrency;
import com.utils.enums.AccountType;

public class CardAccount extends Account{

    private final Card card;

    public CardAccount(String accountNumber, AccountCurrency accountCurrency, Card card) {
        super(accountNumber, AccountType.CARD, accountCurrency);
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public String toString() {
        return getAccountCurrency().getCurrency() + " " + getAccountType().getType() + " " + card.getCardNumber() + "(" + getAccountNumber() + ")";
    }
}
