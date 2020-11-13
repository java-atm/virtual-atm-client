public enum AccountType {
    CURRENT("CURRENT"),
    SAVINGS("SAVINGS"),
    CARD("CARD");

    private final String type;

    AccountType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
