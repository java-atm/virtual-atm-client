import java.util.ArrayList;

public class CardAccount extends Account{

    private final Card card;

    public CardAccount(String accountNumber, AccountCurrency accountCurrency, Card card) {
        super(accountNumber, AccountType.CARD, accountCurrency);
        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
