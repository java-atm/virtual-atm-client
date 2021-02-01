package com.atm.customer_console;

import com.InvalidBanknoteException;
import com.atm.CancelException;
import com.backend_connection.IncorrectPinException;
import com.utils.enums.Action;
import com.utils.enums.Banknote;

import java.io.Console;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public interface CustomerConsole {

    Scanner console = new Scanner(System.in);
    Console pinReader = System.console();

    static String askPIN() throws IncorrectPinException {
        String pin = null;
        int attempts_count = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts_count < MAX_ATTEMPTS) {
            displayDialogMessage("Enter PIN: ");
            attempts_count++;
            try {
                //for debugging use code below
                //pin = console.nextLine();
                pin = String.valueOf(pinReader.readPassword("Please enter your PIN: "));
                if (!pin.matches("^[0-9]+$")) throw new IncorrectPinException("Something went wrong");
                break;
            } catch (IncorrectPinException exception) {
                displayMessage("Failed to read the pin.");
                if (attempts_count == MAX_ATTEMPTS) throw exception;
            }
        }
        return pin;
    }

    static void continueOperation() throws CancelException {
        String cancel = console.nextLine();
        if(cancel.equals("n") || cancel.equals("N") ||
           cancel.equals("no") || cancel.equals("No") ||
           cancel.equals("nO") || cancel.equals("NO") ||
           cancel.equals("n0") || cancel.equals("N0")) throw new CancelException("Have a good day");
    }

    static double acceptCash() {
        displayBanknotes();
        displayDialogMessage("Please, put in your banknotes: ");
        double banknote = 0.0;
        boolean endAcceptCash = false;
        while(!endAcceptCash) {
            try {
                banknote = console.nextDouble();
                isBanknoteValid(banknote);
                displayMessage("You put in: " + banknote);
                endAcceptCash = true;
                console.nextLine();
            } catch (InputMismatchException  ex) {
                displayMessage("Invalid banknote, take that");
            } catch (InvalidBanknoteException exception) {
                displayMessage(exception.getMessage());
            }
        }

        return banknote;
    }

    static private void displayBanknotes() {
        displayMessage("This is valid banknotes");
        int counter = 1;
        for (Banknote banknote : Banknote.values()) {
            counter++;
            if(counter % 3 == 0) displayMessage("\n");
            displayDialogMessage(banknote.getBanknote() + " ");
        }
        displayMessage("\n");
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
        displayDialogMessage("Please, enter amount: ");
        while(true) {
            try {
                amount = console.nextDouble();
                if (amount % Banknote.MINIMAL_BANKNOTE != 0) throw new InvalidBanknoteException("");
                break;
            } catch (Exception ex) {
                displayDialogMessage("Please, enter right amount: ");
            } finally {
                console.nextLine();
            }
        }
        return amount;
    }

    static BigDecimal askAmountForTransfer() {
        BigDecimal amount;
        displayDialogMessage("Please, enter amount: ");
        while(true) {
            try {
                amount = console.nextBigDecimal();
                break;
            } catch (InputMismatchException exception) {
                displayDialogMessage("Please, enter right amount: ");
            } finally {
                console.nextLine();
            }
        }
        return amount;
    }

    static String askAccountNumber() {
        displayDialogMessage("Please, insert account number: ");
        String accountNumber;

        while (true){
            accountNumber = console.nextLine();
            if(checkAccountNumberIsValid(accountNumber)) {
                return accountNumber;
            }
            displayMessage("Inserted account number is not valid");
            displayMessage("Try again");
            displayDialogMessage("Please, insert account number: ");
        }
    }

    private static boolean checkAccountNumberIsValid(String accountNumber) {
        return accountNumber.length() == 16 && accountNumber.matches("^[0-9]+$");
    }

    static Action chooseAction() {
        displayActions();
        displayDialogMessage("Please choose an action: ");
        boolean rightAction = false;
        int action = 0;
        while(!rightAction) {
            try {
                action = console.nextInt() - 1;
                checkActionNumber(action);
                rightAction = true;
            } catch (InputMismatchException exception) {
                displayDialogMessage("Please choose an action from the list: ");
            } finally {
                console.nextLine();
            }
        }
        return Action.values()[action];
    }

    static void displayAccounts(HashMap<String, BigDecimal> accounts) {
        CustomerConsole.displayMessage("#:   Account number : balance");
        StringBuilder stringBuilder = new StringBuilder();
        int count = 1;
        for (Map.Entry<String, BigDecimal> account : accounts.entrySet()) {
            stringBuilder.append(count).append(": ").
                    append(account.getKey()).append(" : ").
                    append(account.getValue()).append("\n");
            count++;
        }
        CustomerConsole.displayMessage(stringBuilder.toString());
    }

    static int chooseAccountIndex(int accountNumbers) {
        displayDialogMessage("Please choose an account: ");
        boolean rightAccount = false;
        int accountIndex = -1;
        while(!rightAccount) {
            try {
                accountIndex = console.nextInt() - 1;
                if(accountIndex < 0 || accountIndex > accountNumbers-1)
                    throw new InputMismatchException();
                rightAccount = true;
            } catch (InputMismatchException exception) {
                displayDialogMessage("Please choose an account from the list: ");
            } finally {
                console.nextLine();
            }
        }
        return accountIndex;
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

    private static void displayDialogMessage(String dialogMsg) {
        System.out.print(dialogMsg);
    }
}
