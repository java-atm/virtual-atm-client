import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class CustomerConsoleAbstract implements CustomerConsoleInterface {
    private static Scanner console = initConsole();

    public CustomerConsoleAbstract() {

    }

    private static Scanner initConsole() {
        return new Scanner(System.in);
    }

    public static Integer askPIN() {
        Integer pin = null;
        int attempts_count = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts_count < MAX_ATTEMPTS) {
            displayMessage("Please enter your PIN: ");
            attempts_count++;
            try {
                pin = console.nextInt();
                if (!checkPinIsValid(pin)) throw new InputMismatchException(null);
                break;
            } catch (InputMismatchException exception) {
                pin = null;
                console = new Scanner(System.in);
                displayMessage("Failed to read the pin.");
            }
        }
        return pin;
    }

    public void checkCancel(String actionCancel) {

    }

    private static boolean checkPinIsValid(Integer pin) {
        return pin.toString().length() == 4;
    }


    public static String askAccountNumber() {
        displayMessage("Please, insert your account number: ");
        String accountNumber;
        int attempts_count = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts_count < MAX_ATTEMPTS){
            attempts_count++;
            displayMessage("Inserted account number is not valid");
            displayMessage("Try again");
            displayMessage("Please, insert your account number: ");
            accountNumber = console.nextLine();
            if(checkAccountNumberIsValid(accountNumber)) {
                return accountNumber;
            }
        }
        return null;
    }

    private static boolean checkAccountNumberIsValid(String accountNumber) {
        return accountNumber.length() == 16;
    }

    public static Cash askAmount() {
        return null;
    }

    public static Actions chooseAction() {
        displayMessage("Please choose an action");
        boolean rightAction = false;
        int action = -1;
        while(!rightAction) {
            displayActions();
            try {
                action = console.nextInt() - 1;
                checkActionNumber(action);
                rightAction = true;
            } catch (InputMismatchException exception) {
                console.nextLine();
                displayMessage("Please choose an action from the list");
                System.out.println(exception.getMessage());
            }
        }
        return Actions.values()[action];
    }

    public static void displayActions() {
        displayMessage("Check Balance - " + Actions.CHECK_BALANCE.getAction());
        displayMessage("Withdrawal    - " + Actions.WITHDRAWAL.getAction());
        displayMessage("Deposit       - " + Actions.DEPOSIT.getAction());
        displayMessage("Change PIN    - " + Actions.PIN_CHANGE.getAction());
        displayMessage("Exit          - " + Actions.EXIT.getAction());
    }

    public static void checkActionNumber(int action) throws InputMismatchException{
        int actionsNumber = Actions.values().length;
        if(action >= actionsNumber || action < 0) {
            throw new InputMismatchException("Wrong number of action");
        }
    }

    public static void displayMessage(String screenMsg) {
        System.out.println(screenMsg);
    }

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
}
