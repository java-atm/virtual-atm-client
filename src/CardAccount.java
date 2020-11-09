import java.util.ArrayList;

public class CardAccount extends Account{
    private ArrayList<Card> cards;
    private final int INITIAL_CARD_COUNT = 5;

    public CardAccount(String accountNumber, AccountCurrency accountCurrency) {
        super(accountNumber, AccountType.CARD, accountCurrency);
        cards = new ArrayList<>(INITIAL_CARD_COUNT);
    }

    public void addCard(Card newCard) {
        cards.add(newCard);
    }

    public void removeCard(Card card) {
        cards.remove(card);
    }

    public Card getCardByCardNumber(String cardNumber) {
        for (Card card:cards) {
            if (card.getCardNumber().equals(cardNumber)) {
                return card;
            }
        }
        return null;
    }
}
