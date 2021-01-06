package com.atm;

import com.Customer;
import com.InvalidBanknoteException;
import com.RealCash;
import com.atm.card_reader.CardIsInvalidException;
import com.atm.card_reader.CardReader;
import com.atm.cash_dispenser.CashDispenser;
import com.atm.customer_console.CustomerConsole;
import com.db.DataBase;
import com.utils.enums.Action;

public class ATM implements ATMInterface {

    private final String ATM_ID;
    private final CashDispenser cashDispenser;
    private final CardReader cardReader;
    //private final ReceiptPrinter;
    private Customer currentCustomer;
    private DataBase dataBase;
    //private final Transaction currentTransaction;

    public ATM(final String ATM_ID, RealCash initialCash) {
        this.ATM_ID = ATM_ID;
        cashDispenser = new CashDispenser(initialCash);
        cardReader = new CardReader();
        currentCustomer = null;
        dataBase = new DataBase();
    }

    public String getATM_ID() {
        return ATM_ID;
    }

    @Override
    public void startATM() {
        while (true) {
            CustomerConsole.displayMessage(ATM_ID + " Home Screen");
            Integer pin;
            try {
                readCard();
                pin = CustomerConsole.askPIN();
                currentCustomer = dataBase.getCustomer(cardReader.getCard(), pin.toString());
                serveCustomer();
            } catch (CancelException exception) {
                if (exception.getMessage().equals("ATM POWER OFF")) break;
                CustomerConsole.displayMessage(exception.getMessage());
            } catch (Exception exception) {
                CustomerConsole.displayMessage(exception.getMessage());
            } finally {
                ejectCard();
                CustomerConsole.displayMessage("Card is ejecting");
                CustomerConsole.displayMessage("Please take your card");
            }
        }
    }

    private void serveCustomer() throws CancelException {
        while(true) {
            if (currentCustomer.getName().equals("Admin")) { // temp statement
                throw new CancelException("ATM POWER OFF");
            } else {
                CustomerConsole.displayMessage("Welcome To Main Menu " + cardReader.getCard().getName());
                Action selectedAction = CustomerConsole.chooseAction();
                performSelectedAction(selectedAction);
            }
            try {
                CustomerConsole.displayMessage("Do you want to perform another transaction ? Yes (any) No (n)");
                CustomerConsole.continueOperation();
            } catch (CancelException exception) {
                CustomerConsole.displayMessage(exception.getMessage());
                break;
            }
        }
    }

    protected void readCard() throws CardIsInvalidException {
        CustomerConsole.displayMessage("Please insert your card info");
        cardReader.readCard();
    }

    protected void displayCash() {
        System.out.println(cashDispenser.getCash().toString());
    }

    protected void performSelectedAction(Action action) throws CancelException{
        switch (action) {
            case CHECK_BALANCE:
                checkBalance();
                break;
            case WITHDRAWAL:
                withdraw();
                break;
            case DEPOSIT:
                deposit();
                break;
            case PIN_CHANGE:
                changePIN();
                break;
            case EXIT:
                exit();
                break;
            default:
                System.out.println("Do something");
        }
    }

    protected void checkBalance() {
        System.out.println("checkBalance");
    }

    protected void withdraw() {
        CustomerConsole.displayMessage("Start withdraw transaction");
        double amount = CustomerConsole.askAmount();
        try {
            cashDispenser.dispenseCash(amount);
            CustomerConsole.displayMessage("Take your cash: " + amount);
        } catch (CashNotEnoughException exception) {
            CustomerConsole.displayMessage(exception.getMessage());
        }
        CustomerConsole.displayMessage("Finish withdraw transaction");
    }

    protected void deposit() {
        CustomerConsole.displayMessage("Start deposit transaction");
        double banknote;
        double wholeDeposit = 0.0;
        while (true) {
            try {
                banknote = CustomerConsole.acceptCash();
                cashDispenser.addCash(banknote);
                wholeDeposit += banknote;
                CustomerConsole.displayMessage("Continue operation? Yes(any), No(n)");
                CustomerConsole.continueOperation();
            } catch (InvalidBanknoteException exception) {
                CustomerConsole.displayMessage(exception.getMessage());
            } catch (CancelException exception) {
                break;
            }
        }
        CustomerConsole.displayMessage("Your deposit is: " + wholeDeposit);
        CustomerConsole.displayMessage("Finish deposit transaction");
    }

    protected void changePIN() {
        System.out.println("changePIN");
    }

    protected void exit() throws CancelException {
        ejectCard();
        System.out.println("exit");
        throw new CancelException("Have a good day " + "❤️");
    }

    public void ejectCard() {
        currentCustomer = null;
        cardReader.ejectCard();
    }

}
