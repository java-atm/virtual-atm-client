import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class CustomerConsoleAbstract implements CustomerConsoleInterface {
    private final Scanner scanner;

    public CustomerConsoleAbstract() {
        scanner = new Scanner(System.in);
    }

    @Override
    public Integer readPin() {
        System.out.print("Please enter your pin: ");
        Integer pin;
        try {
            pin = scanner.nextInt();
        } catch (InputMismatchException exception) {
            System.out.println("Failed to read the pin.");
            return null;
        }
        return pin;
    }

    @Override
    public Cash readCash() {
        System.out.print("Please enter the amount: ");
        double amount;
        try {
            amount = scanner.nextDouble();
        } catch (InputMismatchException exception) {
            System.out.println("Failed to read the amount.");
            return null;
        }
        return new Cash(amount);
    }

    @Override
    public void displayCash(Cash cash) {
        System.out.println("Amount: " + cash.toString());
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }
}
