public abstract class CardAbstract implements CardInterface {

    private final String ID;
    private Integer pin;

    public final static int ID_MIN_LENGTH = 13;
    public final static int ID_MAX_LENGTH = 20;
    public final static int PIN_LENGTH = 4;


    public CardAbstract(final String ID, Integer pin) {
        this.ID = ID;
        this.pin = pin;
    }

    public String getID() {
        return ID;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }

}
