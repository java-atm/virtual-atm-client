public enum AccountType {
    CURRENT("CURRENT"),
    SAVINGS("SAVINGS"),
    CARD("CARD");

    private final String info;

    AccountType(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
