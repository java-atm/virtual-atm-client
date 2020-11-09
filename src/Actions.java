public enum Actions {

    CHECK_BALANCE(0),
    WITHDRAWAL(1),
    DEPOSIT(2),
    PIN_CHANGE(3),
    EXIT(4),
    WRONG_ACTION(5);

    private final int action;

    Actions(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }
}
