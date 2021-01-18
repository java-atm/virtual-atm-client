package com.atm.customer_console;

import com.InvalidBanknoteException;
import com.atm.CancelException;
import com.backend_connection.IncorrectPinException;
import com.utils.enums.Action;
import com.utils.enums.Banknote;

import java.util.InputMismatchException;
import java.util.Scanner;

public interface CustomerConsole {

    Scanner console = new Scanner(System.in);

    static Integer askPIN() throws IncorrectPinException {
        Integer pin = null;
        int attempts_count = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts_count < MAX_ATTEMPTS) {
            displayMessage("Please enter your PIN: ");
            attempts_count++;
            try {
                pin = console.nextInt();
                if (!checkPinIsValid(pin)) throw new IncorrectPinException("Something went wrong");
                break;
            } catch (IncorrectPinException exception) {
                pin = null;
                displayMessage("Failed to read the pin.");
                if (attempts_count == MAX_ATTEMPTS) throw exception;
            } finally {
                console.nextLine();
            }
        }
        return pin;
    }

    private static boolean checkPinIsValid(Integer pin) {
        return pin.toString().length() == 4;
    }

    static void continueOperation() throws CancelException {
        String cancel = console.nextLine();
        if(cancel.equals("n")) throw new CancelException("Have a good day");
    }

    static double acceptCash() throws InvalidBanknoteException {
        displayMessage("Please, put in your banknotes");

        double banknote = console.nextDouble();
        isBanknoteValid(banknote);
        displayMessage("You put in: " + banknote);
        console.nextLine();

        return banknote;
    }

    private static void isBanknoteValid(double banknote) throws InvalidBanknoteException {
        Banknote[] banknotes = Banknote.values();
        for(Banknote one_banknote : banknotes) {
            if(one_banknote.getBanknote() == banknote) {
                return;
            }
        }
        throw new InvalidBanknoteException("Invalid banknote, take that");
    }

    static double askAmount() {
        double amount;
        displayMessage("Please, enter amount");
        while(true) {
            try {
                amount = console.nextDouble();
                if (amount % Banknote.MINIMAL_BANKNOTE != 0) throw new InvalidBanknoteException("");
                break;
            } catch (InputMismatchException | InvalidBanknoteException exception) {
                CustomerConsole.displayMessage("Please, enter right amount");
            } finally {
                console.nextLine();
            }
        }
        return amount;
    }

    static String askAccountNumber() {
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

    static Action chooseAction() {
        displayMessage("Please choose an action");
        boolean rightAction = false;
        int action = 0;
        while(!rightAction) {
            displayActions();
            try {
                action = console.nextInt() - 1;
                checkActionNumber(action);
                rightAction = true;
            } catch (InputMismatchException exception) {
                displayMessage(exception.getMessage());
                displayMessage("Please choose an action from the list");
            } finally {
                console.nextLine();
            }
        }
        return Action.values()[action];
    }

    private static void displayActions() {
        Action[] actions = Action.values();
        for (Action action : actions) {
            displayMessage(action.getStringAction() + " - " + action.getIntAction());
        }
    }

    private static void checkActionNumber(int action) throws InputMismatchException {
        int actionsNumber = Action.values().length;
        if(action >= actionsNumber || action < 0) {
            throw new InputMismatchException("Wrong number of action");
        }
    }

    static void displayMessage(String screenMsg) {
        System.out.println(screenMsg);
    }
}
