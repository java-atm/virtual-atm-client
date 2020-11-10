public enum CardInfo {
    CARD_NUMBER("CARD_NUMBER"),
    NAME("NAME"),
    SURNAME("SURNAME");

    private final String info;

    CardInfo(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
