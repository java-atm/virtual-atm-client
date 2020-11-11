public enum AccountCurrency {
    AMD("AMD"),
    RUB("RUB"),
    USD("USD"),
    EUR("EUR");

    private final String info;

    AccountCurrency(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}

