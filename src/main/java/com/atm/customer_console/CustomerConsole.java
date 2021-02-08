package com.atm.customer_console;

import com.utils.enums.Action;
import com.utils.enums.Banknote;
import com.utils.exceptions.atm_exceptions.CancelException;
import com.utils.exceptions.atm_exceptions.IncorrectPinException;
import com.utils.exceptions.atm_exceptions.InvalidBanknoteException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Console;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;

public class CustomerConsole {

    static Scanner console = new Scanner(System.in);
    static Console pinReader = System.console();

    static Logger LOGGER = LogManager.getLogger(CustomerConsole.class);

    public static String askPIN() throws IncorrectPinException {
        LOGGER.info("Waiting for reading PIN");
        String pin = null;
        int attempts_count = 0;
        final int MAX_ATTEMPTS = 3;

        while (attempts_count < MAX_ATTEMPTS) {
            displayDialogMessage("Enter PIN: ");
            attempts_count++;
            try {
                //for debugging use code below
                pin = console.nextLine();
                LOGGER.info("PIN is entered");
                //pin = String.valueOf(pinReader.readPassword("Please enter your PIN: "));
                if (!pin.matches("^[0-9]+$") || pin.length() > 6 ) {
                    throw new IncorrectPinException("Failed to read the PIN.");
                }
                break;
            } catch (IncorrectPinException exception) {
                displayMessage(exception.getMessage());
                LOGGER.warn("'{}', Invalid PIN inserted", exception.getMessage());
                if (attempts_count == MAX_ATTEMPTS) {
                    LOGGER.error("Number of attempts ended", exception);
                    throw new IncorrectPinException("Number of attempts ended");
                }
            }
        }
        LOGGER.info("PIN is read");
        return pin;
    }

    public static void continueOperation() throws CancelException {
        String cancel = console.nextLine();
        LOGGER.info("Perform another transaction answer: '{}'", cancel);
        if(cancel.equals("n") || cancel.equals("N") ||
           cancel.equals("no") || cancel.equals("No") ||
           cancel.equals("nO") || cancel.equals("NO") ||
           cancel.equals("n0") || cancel.equals("N0")) throw new CancelException("Have a good day ❤️");
    }

    public static double acceptBanknote() {
        LOGGER.info("Performing acceptCash");
        displayBanknotes();
        displayDialogMessage("Please, put in your banknotes: ");
        double banknote = 0.0;
        while(true) {
            try {
                banknote = console.nextDouble();
                LOGGER.info("Banknote is inserted");
                isBanknoteValid(banknote);
                displayMessage("You put in: " + banknote);
                console.nextLine();
                break;
            } catch (InputMismatchException exception) {
                LOGGER.warn("WRONG INPUT OF BANKNOTE");
                displayMessage("Invalid banknote, take that");
            } catch (InvalidBanknoteException exception) {
                LOGGER.warn("INVALID BANKNOTE: '{}'", banknote);
                displayMessage(exception.getMessage());
            }
        }
        LOGGER.info("Banknote is accepted: '{}'", banknote);
        return banknote;
    }

    private static void displayBanknotes() {
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

    public static double askAmountForWithdraw() {
        LOGGER.info("Performing askAmount");
        double amount = -1;
        displayDialogMessage("Please, enter amount: ");
        while(true) {
            try {
                amount = console.nextDouble();
                if (amount % Banknote.MINIMAL_BANKNOTE != 0 ||
                        amount == 0 || amount < 0) throw new InvalidBanknoteException("");
                break;
            } catch (InputMismatchException | InvalidBanknoteException ex) {
                LOGGER.warn("INVALID AMOUNT: '{}'", Double.toString(amount));
                displayDialogMessage("Please, enter right amount: ");
            } finally {
                LOGGER.info("Entered Amount: '{}'", amount);
                console.nextLine();
            }
        }
        LOGGER.info("Amount is read");
        return amount;
    }

    public static BigDecimal askAmountForTransfer() {
        LOGGER.info("Performing askAmountForTransfer");
        BigDecimal amount = new BigDecimal(-1);
        displayDialogMessage("Please, enter amount: ");
        while(true) {
            try {
                amount = console.nextBigDecimal();
                int isValidAmount = amount.compareTo(BigDecimal.ZERO);
                if(isValidAmount <= 0) throw new InputMismatchException();
                break;
            } catch (InputMismatchException exception) {
                LOGGER.warn("INVALID AMOUNT: '{}'", amount.toString());
                displayDialogMessage("Please, enter right amount: ");
            } finally {
                console.nextLine();
            }
        }
        LOGGER.info("Amount for transfer is read");
        return amount;
    }

    public static String askAccountNumber() {
        LOGGER.info("Performing askAccountNumber");
        displayDialogMessage("Please, insert account number: ");
        String accountNumber;
        while (true){
            accountNumber = console.nextLine();
            if(checkAccountNumberIsValid(accountNumber)) {
                LOGGER.info("Account number is read: '{}'", accountNumber);
                return accountNumber;
            }
            LOGGER.warn("INSERTED ACCOUNT NUMBER NOT VALID: '{}'", accountNumber);
            displayMessage("Inserted account number is not valid");
            displayMessage("Try again");
            displayDialogMessage("Please, insert account number: ");
        }
    }

    private static boolean checkAccountNumberIsValid(String accountNumber) {
        return accountNumber.length() == 16 && accountNumber.matches("^[0-9]+$");
    }

    public static Action chooseAction() {
        LOGGER.info("Start choosing action");
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
                LOGGER.warn("Wrong number of action or not valid format");
                displayDialogMessage("Please choose an action from the list: ");
            } finally {
                console.nextLine();
            }
        }
        LOGGER.info("Action is chosen: '{}' : '{}'", action, Action.values()[action]);
        return Action.values()[action];
    }

    public static void displayAccounts(HashMap<String, BigDecimal> accounts) {
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

    public static int chooseAccountIndex(int accountNumbers) {
        LOGGER.info("Start choosing account index");
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
                LOGGER.warn("WRONG ACCOUNT INDEX");
                displayDialogMessage("Please choose an account from the list: ");
            } finally {
                console.nextLine();
            }
        }
        LOGGER.info("Account index is chosen");
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

    public static void displayMessage(String screenMsg) {
        System.out.println(screenMsg);
    }

    private static void displayDialogMessage(String dialogMsg) {
        System.out.print(dialogMsg);
    }
}
