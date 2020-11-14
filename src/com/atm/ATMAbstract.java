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
                //System.out.println("Analyze "+ exception.getMessage());
                pin = 1111;
            }
            while(true) {
                try {
                    currentCustomer = new DataBase().getCustomer(cardReader.getCard(), pin.toString());
                    if (cardReader.getCard().getName().equals("Eduard") && pin == 1111) {
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
                        IncorrectPinException nullPointerException) {
                    //System.out.println("Analyze " + nullPointerException.getLocalizedMessage());
                    break;
                }
            }
            ejectCard();
            CustomerConsole.displayMessage("Card is ejecting");
            CustomerConsole.displayMessage("Please take your card");
        }
    }

    protected void readCard() {

        CustomerConsole.displayMessage("Please insert your card info");
        cardReader.readCard();
        if (!cardReader.cardIsValid()) {
            cardReader.ejectCard();
            CustomerConsole.displayMessage("Something went wrong, try again");
            throw new NullPointerException();
        }
    }

    protected Cash getAccountEntry() {
        return null;
    }

    public void addCash(Integer cash) {
        //System.out.println("Adding cash" + cash.toString());
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
        int amount = CustomerConsole.withdrawProcess();
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
        int banknote;
        do {
            banknote = CustomerConsole.acceptCash();
            if (banknote != 0) cashDispenser.addCash(banknote);
            CustomerConsole.displayMessage("Continue operation? Yes(any), No(0)");
        } while (CustomerConsole.continueOperation());
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

