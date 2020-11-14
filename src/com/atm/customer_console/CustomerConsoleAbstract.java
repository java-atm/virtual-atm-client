package com.atm.customer_console;

import com.Cash;
import com.utils.enums.Action;
import com.utils.enums.Banknote;

import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class CustomerConsoleAbstract implements CustomerConsoleInterface {
    private static Scanner console = initConsole();

    public CustomerConsoleAbstract() {

    }

    private static Scanner initConsole() {
        return new Scanner(System.in);
    }

    public static Integer askPIN() throws InputMismatchException{
        Integer pin = null;
        int attempts_count = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts_count < MAX_ATTEMPTS) {
            displayMessage("Please enter your PIN: ");
            attempts_count++;
            try {
                pin = console.nextInt();
                if (!checkPinIsValid(pin)) throw new InputMismatchException("PIN invalid");
                console.nextLine();
                break;
            } catch (InputMismatchException exception) {
                pin = null;
                console.nextLine();
                displayMessage("Failed to read the pin.");
                if (attempts_count == MAX_ATTEMPTS) throw exception;
            }
        }
        return pin;
    }

    public static void checkCancel() {
        displayMessage("Do you want to perform another transaction ? Yes (y) No (n)");
        //Demo version // Don't care about that what I wrote here
        String cancel;
        try {
            cancel = console.nextLine();
            if(cancel.equals("n")) throw new NullPointerException("");
        } catch (NullPointerException exception) {
            throw new NullPointerException();
        }
    }

    public static boolean continueOperation() {
        int accept;
        try {
            accept = console.nextInt();
            console.nextLine();
            return accept != 0;
        } catch (InputMismatchException exception) {
            console.nextLine();
            return true;
        }
    }

    private static boolean checkPinIsValid(Integer pin) {
        return pin.toString().length() == 4;
    }

    public static int acceptCash() {
        displayMessage("Please, put in your banknotes");
        int banknote;
        try {
            banknote = console.nextInt();
            if(invalidBanknote(banknote)) throw new InputMismatchException("");
            displayMessage("You put in: " + banknote);
            console.nextLine();
        } catch (InputMismatchException exception) {
            banknote = 0;
            console.nextLine();
        }
        return banknote;
    }

    private static boolean invalidBanknote(int banknote) {
        Banknote[] banknotes = Banknote.values();
        for(Banknote one_banknote : banknotes) {
            if(one_banknote.getBanknote() == banknote) {
                return false;
            }
        }
        displayMessage("Something went wrong, take your banknote");
        return true;
    }

    public static int withdrawProcess() {
        int amount;
        displayMessage("Please, enter amount");
        while(true) {
            try {
                amount = console.nextInt();
                if (amount % Banknote.BANKNOTE_10.getBanknote() != 0) throw new InputMismatchException("");
                console.nextLine();
                break;
            } catch (InputMismatchException exception) {
                CustomerConsole.displayMessage("Please, enter right amount");
                console.nextLine();
            }
        }
        return amount;
    }

    public static String askAccountNumber() {
        displayMessage("Please, insert your account number: ");
        String accountNumber;
        int attempts_count = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts_count < MAX_ATTEMPTS){
            attempts_count++;
            accountNumber = console.nextLine();
            if(checkAccountNumberIsValid(accountNumber)) {
                return accountNumber;
            }
            displayMessage("Inserted account number is not valid");
            displayMessage("Try again");
            displayMessage("Please, insert your account number: ");
        }
        return null;
    }

    private static boolean checkAccountNumberIsValid(String accountNumber) {
        return accountNumber.length() == 16;
    }

    public static Cash askAmount() {
        return null;
    }

    public static Action chooseAction() {
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
        return Action.values()[action];
    }

    public static void displayActions() {
        Action[] actions = Action.values();
        for (Action action : actions) {
            displayMessage(action.getStringAction() + " - " + action.getIntAction());
        }
    }

    public static void checkActionNumber(int action) throws InputMismatchException{
        int actionsNumber = Action.values().length;
        if(action >= actionsNumber || action < 0) {
            throw new InputMismatchException("Wrong number of action");
        }
    }

    public static void displayMessage(String screenMsg) {
        System.out.println(screenMsg);
    }

//    @Override
//    public atm.Cash readCash() {
//        System.out.print("Please enter the amount: ");
//        double amount;
//        try {
//            amount = scanner.nextDouble();
//        } catch (InputMismatchException exception) {
//            System.out.println("Failed to read the amount.");
//            return null;
//        }
//        return new atm.Cash(amount);
//    }
//
//    @Override
//    public void displayCash(atm.Cash cash) {
//        System.out.println("Amount: " + cash.toString());
//    }
}
