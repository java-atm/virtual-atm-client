import java.util.LinkedHashMap;

public abstract class CardAbstract implements CardInterface {

    private final LinkedHashMap<CardInfo, String> IDENTIFICATION_INFO;

    public final static int ID_MIN_LENGTH = 13;
    public final static int ID_MAX_LENGTH = 20;

    public CardAbstract(LinkedHashMap<CardInfo, String> identifyInfo) {
        IDENTIFICATION_INFO = identifyInfo;
    }

    public String getCardNumber() {
        return IDENTIFICATION_INFO.get(CardInfo.CARD_NUMBER);
    }

    public String getName() {
        return IDENTIFICATION_INFO.get(CardInfo.NAME);
    }

    public String getSurname() {
        return IDENTIFICATION_INFO.get(CardInfo.SURNAME);
    }

    public LinkedHashMap<CardInfo, String> getIDENTIFICATION_INFO() {
        return IDENTIFICATION_INFO;
    }
}
