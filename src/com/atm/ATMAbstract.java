package com.atm;

import com.Customer;
import com.RealCash;
import com.atm.card_reader.CardReader;
import com.Cash;
import com.atm.cash_dispenser.CashDispenser;
import com.atm.customer_console.CustomerConsole;
import com.db.CustomerNotFoundException;
import com.db.DataBase;
import com.db.IncorrectPinException;
import com.utils.enums.Action;

import java.io.IOException;
import java.util.InputMismatchException;

public abstract class ATMAbstract implements ATMInterface {

    private final String ATM_ID;
    private final CashDispenser cashDispenser;
    private final CardReader cardReader;
    //private final ReceiptPrinter;
    private Customer currentCustomer;
    //private final Transaction currentTransaction;

    public ATMAbstract(final String ATM_ID, RealCash initialCash) {
        this.ATM_ID = ATM_ID;
        cashDispenser = new CashDispenser(initialCash);
        cardReader = new CardReader();
        currentCustomer = null;
    }

    public String getATM_ID() {
        return ATM_ID;
    }

    @Override
    public void startATM() {
        boolean atm_is_on = true;
        while (atm_is_on) {
            CustomerConsole.displayMessage(ATM_ID + " Home Screen");
            Integer pin;
            try {
                readCard();
                pin = CustomerConsole.askPIN();
            } catch (NullPointerException | InputMismatchException exception) {
                pin = 0;
                CustomerConsole.displayMessage(exception.getLocalizedMessage());
            }
            while(atm_is_on) {
                try {
                    currentCustomer = new DataBase().getCustomer(cardReader.getCard(), pin.toString());
                    if (currentCustomer.getName().equals("Admin")) {
                        atm_is_on = false;
                        CustomerConsole.displayMessage("ATM SWITCH OFF");
                    } else {
                        CustomerConsole.displayMessage("Welcome To Main Menu " + cardReader.getCard().getName());
                        Action selectedAction = CustomerConsole.chooseAction();
                        performSelectedAction(selectedAction);
                        if (selectedAction != Action.EXIT) CustomerConsole.checkCancel();
                    }
                } catch (NullPointerException |
                        CustomerNotFoundException |
                        IncorrectPinException exception) {
                    CustomerConsole.displayMessage(exception.getMessage());
                    break;
                }
            }
            ejectCard();
            CustomerConsole.displayMessage("Card is ejecting");
            CustomerConsole.displayMessage("Please take your card");
        }
    }

    protected void readCard() throws NullPointerException {
        CustomerConsole.displayMessage("Please insert your card info");
        cardReader.readCard();
        if (!cardReader.cardIsValid()) {
            cardReader.ejectCard();
            throw new NullPointerException("Something went wrong, try again");
        }
    }

    protected Cash getAccountEntry() {
        return null;
    }

    public void addCash(Double cash) {
        cashDispenser.addCash(cash);
    }

    protected void displayCash() {
        System.out.println(cashDispenser.getCash().toString());
    }


    protected boolean checkIfCashAvailable(Cash cash) {
        return false;
    }

    protected void performSelectedAction(Action action) {
        switch (action) {
            case CHECK_BALANCE:
                checkBalance();
                break;
            case WITHDRAWAL:
                withdrawal();
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

    protected void withdrawal() {
        CustomerConsole.displayMessage("Start withdraw transaction");
        double amount = CustomerConsole.withdrawProcess();
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
        do {
            try {
                banknote = CustomerConsole.acceptCash();
                cashDispenser.addCash(banknote);
                wholeDeposit += banknote;
            } catch (InputMismatchException exception) {
                CustomerConsole.displayMessage(exception.getMessage());
            }
        } while (CustomerConsole.continueOperation());
        CustomerConsole.displayMessage("Your deposit is: " + wholeDeposit);
        CustomerConsole.displayMessage("Finish deposit transaction");
    }

    protected void changePIN() {
        System.out.println("changePIN");
    }

    protected void exit() {
        ejectCard();
        System.out.println("exit");
    }

    public void ejectCard() {
        cardReader.ejectCard();
    }
}

