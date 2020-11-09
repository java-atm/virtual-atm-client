import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class CustomerConsoleAbstract implements CustomerConsoleInterface {
    private final Scanner console;

    public CustomerConsoleAbstract() {
        console = new Scanner(System.in);
    }

    public void checkCancel(String actionCancel) {

    }

    public void askPIN() {
        displayMessage("Please enter your PIN: ");
        Integer pin;
        try {
            pin = console.nextInt();
        } catch (InputMismatchException exception) {
            System.out.println("Failed to read the pin.");
        }
    }

    public String askAccountNumber() {
        return null;
    }

    public Cash askAmount() {
        return null;
    }

    public void chooseAction() {

    }

    public void performTransaction(Integer someVar) {

    }

    public static void displayMessage(String screenMsg) {
        System.out.print(screenMsg);
    }

//
//    @Override
//    public Integer askPIN() {
//        System.out.print("Please enter your pin: ");
//        Integer pin;
//        try {
//            pin = scanner.nextInt();
//        } catch (InputMismatchException exception) {
//            System.out.println("Failed to read the pin.");
//            return null;
//        }
//        return pin;
//    }
//
//    @Override
//    public Cash readCash() {
//        System.out.print("Please enter the amount: ");
//        double amount;
//        try {
//            amount = scanner.nextDouble();
//        } catch (InputMismatchException exception) {
//            System.out.println("Failed to read the amount.");
//            return null;
//        }
//        return new Cash(amount);
//    }
//
//    @Override
//    public void displayCash(Cash cash) {
//        System.out.println("Amount: " + cash.toString());
//    }
//
//    @Override
//    public void displayMessage(String message) {
//        System.out.println(message);
//    }
}
