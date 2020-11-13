public enum Actions {

    CHECK_BALANCE(1),
    WITHDRAWAL(2),
    DEPOSIT(3),
    PIN_CHANGE(4),
    EXIT(5);

    private final int action;

    Actions(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }
}
