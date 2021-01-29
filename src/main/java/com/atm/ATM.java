package com.atm;

import com.Customer;
import com.InvalidBanknoteException;
import com.RealCash;
import com.atm.card_reader.CardIsInvalidException;
import com.atm.card_reader.CardReader;
import com.atm.cash_dispenser.CashDispenser;
import com.atm.customer_console.CustomerConsole;
import com.backend_connection.BackendConnection;
import com.utils.enums.Action;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ATM implements ATMInterface {

    private final String ATM_ID;
    private final CashDispenser cashDispenser;
    private final CardReader cardReader;
    //private final ReceiptPrinter;
    private Customer currentCustomer;
    private final BackendConnection backendConnection;
    //private final Transaction currentTransaction;

    public ATM(final String ATM_ID, RealCash initialCash) {
        this.ATM_ID = ATM_ID;
        cashDispenser = new CashDispenser(initialCash);
        cardReader = new CardReader();
        currentCustomer = null;
        backendConnection = new BackendConnection();
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
                String customerID = backendConnection.authenticate(ATM_ID, cardReader.getCard(), pin.toString());
                currentCustomer = new Customer(customerID,
                                               cardReader.getCard().getName(),
                                               cardReader.getCard().getSurname());
                serveCustomer();
            } catch (CancelException exception) {
                if (exception.getMessage().equals("ATM POWER OFF")) break;
                CustomerConsole.displayMessage(exception.getMessage());
            } catch (Exception exception) {
                CustomerConsole.displayMessage(exception.getMessage() + " esa");
            } finally {
                ejectCard();
                CustomerConsole.displayMessage("Card is ejecting");
                CustomerConsole.displayMessage("Please take your card");
            }
        }
    }

    private void serveCustomer() throws CancelException {
        while(true) {
            CustomerConsole.displayMessage("Welcome To Main Menu " + currentCustomer.getName());
            Action selectedAction = CustomerConsole.chooseAction();
            performSelectedAction(selectedAction);
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
            case TRANSFER:
                transfer();
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

    private void transfer() {
        CustomerConsole.displayMessage("Start transfer transaction");
        CustomerConsole.displayMessage("Choose account what you want to transfer from");

        HashMap<String, BigDecimal> accounts = new HashMap<>();
        try {
            accounts = backendConnection.getAccountsByCustomerID(currentCustomer.getCustomerID(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentCustomer.setAccounts(accounts);
        CustomerConsole.displayMessage("Account number : balance");
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, BigDecimal> account : accounts.entrySet()) {
            stringBuilder.append(account.getKey()).append(" : ").append(account.getValue()).append("\n");
        }
        CustomerConsole.displayMessage(stringBuilder.toString());
        String fromAccount = currentCustomer.getAccountByAccountNumber(new Scanner(System.in).nextInt());
        CustomerConsole.displayMessage("Enter an account number where you want to transfer");
        String toAccount = new Scanner(System.in).nextLine();
        CustomerConsole.displayMessage("Enter amount which you want to transfer");
        Integer amountForTransfer = new Scanner(System.in).nextInt();
        backendConnection.transfer(fromAccount, toAccount, amountForTransfer.toString());
    }

    protected void checkBalance() {
        try {
            HashMap<String, BigDecimal> balances = backendConnection.checkBalance(ATM_ID, currentCustomer.getCustomerID());
            CustomerConsole.displayMessage("Account number : balance");
            StringBuilder stringBuilder = new StringBuilder();
            for (Map.Entry<String, BigDecimal> balance : balances.entrySet()) {
                stringBuilder.append(balance.getKey()).append(" : ").append(balance.getValue()).append("\n");
            }
            CustomerConsole.displayMessage(stringBuilder.toString());
        } catch (Exception ex) {
            CustomerConsole.displayMessage("Some information");
        }

    }

    protected void withdraw() {
        CustomerConsole.displayMessage("Start withdraw transaction");

        HashMap<String, BigDecimal> accounts = new HashMap<>();
        try {
            accounts = backendConnection.getAccountsByCustomerID(currentCustomer.getCustomerID(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentCustomer.setAccounts(accounts);
        CustomerConsole.displayMessage("Choose account what you want to perform withdraw from");
        CustomerConsole.displayMessage("Account number : balance");
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, BigDecimal> account : accounts.entrySet()) {
            stringBuilder.append(account.getKey()).append(" : ").append(account.getValue()).append("\n");
        }
        CustomerConsole.displayMessage(stringBuilder.toString());
        String account = currentCustomer.getAccountByAccountNumber(new Scanner(System.in).nextInt());
        BigDecimal balance = accounts.get(account);

        double amount = CustomerConsole.askAmount();


        if (balance.compareTo(BigDecimal.valueOf(amount)) >= 0) {
            try {
                cashDispenser.dispenseCash(amount);
                backendConnection.withdraw(account, BigDecimal.valueOf(amount));
            } catch (CashNotEnoughException exception) {
                CustomerConsole.displayMessage(exception.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CustomerConsole.displayMessage("YOU ARE A POOR-MAN");
        }
        CustomerConsole.displayMessage("Take your cash: " + amount);

        CustomerConsole.displayMessage("Finish withdraw transaction");
    }

    protected void deposit() {
        CustomerConsole.displayMessage("Start deposit transaction");
        HashMap<String, BigDecimal> accounts = new HashMap<>();
        try {
            accounts = backendConnection.getAccountsByCustomerID(currentCustomer.getCustomerID(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentCustomer.setAccounts(accounts);
        CustomerConsole.displayMessage("Choose account where you want to perform deposit");
        CustomerConsole.displayMessage("Account number : balance");
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, BigDecimal> account : accounts.entrySet()) {
            stringBuilder.append(account.getKey()).append(" : ").append(account.getValue()).append("\n");
        }
        CustomerConsole.displayMessage(stringBuilder.toString());
        String account = currentCustomer.getAccountByAccountNumber(new Scanner(System.in).nextInt());
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
        if (wholeDeposit != 0.0) {
            try {
                backendConnection.deposit(account, new BigDecimal(wholeDeposit));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CustomerConsole.displayMessage("Your deposit is: " + wholeDeposit);
        CustomerConsole.displayMessage("Finish deposit transaction");
    }

    protected void changePIN() {
        CustomerConsole.displayMessage("Enter your new PIN");
        int newPIN = new Scanner(System.in).nextInt();
        backendConnection.changePIN(cardReader.getCard().getCardNumber(), newPIN);
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
