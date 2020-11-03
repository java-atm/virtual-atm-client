public interface CustomerConsoleInterface {

    Integer readPin();
    Cash readAmount();
    void displayAmount(Cash cash);
    void displayMessage(String message);
}
