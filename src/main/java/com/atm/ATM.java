package com.atm;

import com.atm.card_reader.CardReader;
import com.atm.cash_dispenser.CashDispenser;
import com.atm.customer_console.CustomerConsole;
import com.atm.receipt_printer.ReceiptPrinter;
import com.backend_connection.BackendConnection;
import com.utils.cash.RealCash;
import com.utils.customer.Customer;
import com.utils.enums.Action;
import com.utils.exceptions.BaseException;
import com.utils.exceptions.atm_exceptions.CancelException;
import com.utils.exceptions.atm_exceptions.CardIsInvalidException;
import com.utils.exceptions.atm_exceptions.CashNotEnoughException;
import com.utils.exceptions.atm_exceptions.IncorrectPinException;
import com.utils.exceptions.transaction_exceptions.*;
import com.utils.transactions.TransactionBuilder;
import com.utils.transactions.Transactions;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.NoSuchElementException;

public class ATM implements ATMInterface {

    private static final Logger LOGGER = LogManager.getLogger(ATM.class);

    private final String ATM_ID;
    private final CashDispenser cashDispenser;
    private final CardReader cardReader;
    private Customer currentCustomer;
    private final BackendConnection backendConnection;
    private HashMap<String, BigDecimal> accounts;
    private TransactionBuilder currentTransactionBuilder;
    private Action lastSelectedAction;
    private double wholeDeposit = 0.0;
    int counter = 0;
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
        LOGGER.info("ATM started");
        while (true) {
            CustomerConsole.displayMessage(ATM_ID + " Home Screen");
            String pin;
            try {
                readCard();
                pin = CustomerConsole.askPIN();
                LOGGER.info("Card number: '{}'", cardReader.getCard().getCardNumber());
                String customerID = backendConnection.authenticate(ATM_ID, cardReader.getCard(), pin);
                LOGGER.info("Customer ID: '{}'", customerID);
                currentCustomer = new Customer(customerID,
                                               cardReader.getCard().getName(),
                                               cardReader.getCard().getSurname());
                serveCustomer();
//                First catch is being caused if the customer entered CTRL+D
            } catch (IllegalStateException | NoSuchElementException exception) {
                LOGGER.error("ATM IS PREPARING FOR POWERING OFF", exception);
                doExtremelyPowerOffATM();
                break;
            } catch (CancelException exception) {
                if (exception.getMessage().equals("ATM POWER OFF")) break;
                CustomerConsole.displayMessage(exception.getMessage());
            } catch (BaseException exception) {
                CustomerConsole.displayMessage(exception.getMessage());
                LOGGER.error(exception);
            } finally {
                ejectCard();
                CustomerConsole.displayMessage("Card is ejecting");
                LOGGER.info("Card is ejected");
                CustomerConsole.displayMessage("Please take your card");
                checkCustomerInfoIsDeleted();
            }
        }
    }

    private void doExtremelyPowerOffATM() {
        CustomerConsole.displayMessage("Something went wrong");
        CustomerConsole.displayMessage("The last transaction is not successful");
        CustomerConsole.displayMessage("Please accept our apologies");
        if (lastSelectedAction == Action.DEPOSIT && wholeDeposit != 0.0) {
            CustomerConsole.displayMessage("Take your cash: " + wholeDeposit);
            try {
                cashDispenser.dispenseCash(wholeDeposit);
                LOGGER.info("Customer took his deposit before POWER OFF: '{}'", wholeDeposit);
                wholeDeposit = 0.0;
            } catch (CashNotEnoughException ignore) {

            }
        }
    }

    private void checkCustomerInfoIsDeleted() {
        try {
            cardReader.getCard();
            LOGGER.warn("CARD INFO IS NOT DELETED: '{}'", cardReader.getCard().getCardNumber());
        } catch (NullPointerException ex) {
            LOGGER.info("Card info is deleted");
        }
        if (currentCustomer != null) {
            LOGGER.warn("CUSTOMER INFO IS NOT DELETED: '{}'", currentCustomer.getCustomerID());
        } else {
            LOGGER.warn("Customer info is deleted");
        }
    }

    private void serveCustomer() throws CancelException {
        LOGGER.info("Start serving customer");
        while(true) {
            currentTransactionBuilder = Transactions.getTransactionBuilder().
                                                        setATM_ID(ATM_ID).
                                                        setCustomerID(currentCustomer.getCustomerID()).
                                                        setCardNumber(cardReader.getCard().getCardNumber());
            CustomerConsole.displayMessage("Welcome To Main Menu " + currentCustomer.getName());
            lastSelectedAction = CustomerConsole.chooseAction();
            performSelectedAction(lastSelectedAction);
            try {
                LOGGER.info("Dialog to choose another transaction");
                //some code for getting transactionID
                currentTransactionBuilder.setTransactionID("TransID123" + counter);
                counter++;
                Transactions transactions = currentTransactionBuilder.buildTransaction();
                if(!transactions.getTransactionID().equals("TransID")) {
                    printReceipt(new JSONObject(transactions));
                }
                CustomerConsole.displayMessage("Do you want to perform another transaction ? Yes (any) No (n)");
                CustomerConsole.cancelOperation();
            } catch (CancelException exception) {
                LOGGER.info("Customer cancel transactions");
                CustomerConsole.displayMessage(exception.getMessage());
                break;
            }
        }
    }

    private void printReceipt(JSONObject trsInfo) {
        LOGGER.info("Preparing for printing receipt");
        try {
            CustomerConsole.displayMessage("Do you want print receipt or you are loving nature? Yes (any) No (n)");
            CustomerConsole.cancelOperation();
            ReceiptPrinter.printReceipt(trsInfo);
        } catch (CancelException e) {
            LOGGER.error("I DUNNO WHAT I CAN WRITE HERE", e);
            CustomerConsole.displayMessage("We knew that YOU ARE THE KINDEST PERSON IN ALL THE WORLD ! ! !");
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
                currentTransactionBuilder.
                        setTransactionType(Action.CHECK_BALANCE.name());
                checkBalance();
                break;
            case WITHDRAW:
                currentTransactionBuilder.
                        setTransactionType(Action.WITHDRAW.name());
                withdraw();
                break;
            case DEPOSIT:
                currentTransactionBuilder.
                        setTransactionType(Action.CHECK_BALANCE.name());
                deposit();
                break;
            case TRANSFER:
                currentTransactionBuilder.
                        setTransactionType(Action.TRANSFER.name());
                transfer();
                break;
            case CHANGE_PIN:
                currentTransactionBuilder.
                        setTransactionType(Action.CHANGE_PIN.name());
                changePIN();
                break;
            case EXIT:
                exit();
                break;
            default:
                CustomerConsole.displayMessage("Please, choose an action from the list");
        }
    }

    private void transfer() {
        LOGGER.info("Chosen transaction is transfer");
        CustomerConsole.displayMessage("Start transfer transaction");
        CustomerConsole.displayMessage("Choose account you want to transfer from");
        try {
            String fromAccount = getAccountByAccountNumber();
            CustomerConsole.displayMessage("Enter an account number where you want to transfer");
            String toAccount = getToAccount();
            CustomerConsole.displayMessage("Enter amount which you want to transfer");
            BigDecimal amountForTransfer = CustomerConsole.askAmountForTransfer();
            LOGGER.info("Amount for transfer: '{}'", amountForTransfer);
            backendConnection.transfer(ATM_ID, fromAccount, toAccount, amountForTransfer.toString());
            CustomerConsole.displayMessage("Transfer performed successful");
            accounts = backendConnection.getAccountsByCustomerID(ATM_ID, currentCustomer.getCustomerID(), true);
            LOGGER.info("Transfer performed successful");
            CustomerConsole.displayAccounts(accounts);
            currentTransactionBuilder.
                    setFromAccount(fromAccount).
                    setToAccount(toAccount).
                    setAmount(amountForTransfer.toString());
        } catch (AccountsByCustomerIDException | TransferTransactionException ex) {
            LOGGER.error("Transfer transaction is failed", ex);
            CustomerConsole.displayMessage(ex.getMessage());
        }
    }

    private String getToAccount() {
        while(true) {
            String toAccount = CustomerConsole.askAccountNumber();
            try {
                String toAccountOwnerName = backendConnection.getAccountOwnerName(ATM_ID, toAccount);
                LOGGER.info("To account owner name: '{}'", toAccountOwnerName);
                CustomerConsole.displayMessage("Customer name: " + toAccountOwnerName);
                return toAccount;
            } catch (AccountOwnerNameException ex) {
                LOGGER.warn("Something went wrong: '{}'", ex.getMessage(), ex);
                CustomerConsole.displayMessage(ex.getMessage() + " try again");
            }
        }
    }

    protected void checkBalance() {
        LOGGER.info("Start check balances");
        try {
            accounts = backendConnection.checkBalance(ATM_ID, currentCustomer.getCustomerID());
            CustomerConsole.displayAccounts(accounts);
            LOGGER.info("Check balances performed successful");
        } catch (CheckBalanceTransactionException ex) {
            LOGGER.error("Check balance transaction is failed", ex);
            CustomerConsole.displayMessage(ex.getMessage());
        }

    }

    private String getAccountByAccountNumber() throws AccountsByCustomerIDException {
        LOGGER.info("Get account by account number");
        accounts = backendConnection.getAccountsByCustomerID(ATM_ID, currentCustomer.getCustomerID(), true);
        currentCustomer.setAccounts(accounts);
        CustomerConsole.displayAccounts(accounts);
        int accountNumberIndex = CustomerConsole.chooseAccountIndex(accounts.size());
        String account = currentCustomer.getAccountByAccountNumber(accountNumberIndex);
        LOGGER.info("Chosen account: '{}'", account);
        CustomerConsole.displayMessage("This is your chosen account: " + account);
        return account;
    }

    protected void withdraw() {
        LOGGER.info("Start withdraw transaction");
        CustomerConsole.displayMessage("Start withdraw transaction");
        try {
            CustomerConsole.displayMessage("Choose account you want to perform withdraw from");
            String account = getAccountByAccountNumber();
            BigDecimal balance = accounts.get(account);
            LOGGER.info("Account: '{}', balance: '{}'", account, balance);
            double amount = CustomerConsole.askAmountForWithdraw();
            if (balance.compareTo(BigDecimal.valueOf(amount)) >= 0) {
                try {
                    LOGGER.info("Dispense cash from ATM");
                    cashDispenser.dispenseCash(amount);
                    backendConnection.withdraw(ATM_ID, account, BigDecimal.valueOf(amount));
                    currentTransactionBuilder.
                            setFromAccount(account).
                            setAmount(String.valueOf(amount));
                    CustomerConsole.displayMessage("Take your cash: " + amount);
                } catch (CashNotEnoughException exception) {
                    LOGGER.error("ATM DOES NOT HAVE ENOUGH MONEY");
                    CustomerConsole.displayMessage(exception.getMessage());
                } catch (Exception e) {
                    LOGGER.error("WITHDRAW TRANSACTION IS FAILED");
                    CustomerConsole.displayMessage(e.getMessage());
                }
                LOGGER.info("Withdraw transaction performed successful");
            } else {
                LOGGER.info("Customer does not have enough money");
                CustomerConsole.displayMessage("YOU ARE A POOR-MAN");
            }
            CustomerConsole.displayMessage("Finish withdraw transaction");
        } catch (AccountsByCustomerIDException ex) {
            LOGGER.error("WITHDRAW TRANSACTION IS FAILED. CAUSE getAccountByAccountNumber ", ex);
            CustomerConsole.displayMessage(ex.getMessage());
        }

    }

    protected void deposit() {
        LOGGER.info("Start deposit transaction");
        CustomerConsole.displayMessage("Start deposit transaction");
        try {
            CustomerConsole.displayMessage("Choose account where you want to perform deposit");
            String account = getAccountByAccountNumber();
            LOGGER.info("Account: '{}'", account);
            double banknote;
            wholeDeposit = 0.0;
            while (true) {
                try {
                    LOGGER.info("Start accepting banknotes");
                    banknote = CustomerConsole.acceptBanknote();
                    LOGGER.info("Adding banknote to ATM");
                    cashDispenser.addCash(banknote);
                    wholeDeposit += banknote;
                    CustomerConsole.displayMessage("Total: " + wholeDeposit);
                    LOGGER.info("Whole deposit: '{}'", wholeDeposit);
                    CustomerConsole.displayMessage("Continue operation? Yes(any), No(n)");
                    CustomerConsole.cancelOperation();
                } catch (CancelException exception) {
                    LOGGER.info("Customer ended to insert banknotes");
                    break;
                }
            }
            if (wholeDeposit != 0.0) {
                try {
                    LOGGER.info("Preparing for adding deposit to chosen account: '{}'", account);
                    backendConnection.deposit(ATM_ID, account, new BigDecimal(wholeDeposit));
                    LOGGER.info("Deposit is performed successful");
                    CustomerConsole.displayMessage("Your deposit is: " + wholeDeposit);
                    currentTransactionBuilder.
                            setToAccount(account).
                            setAmount(String.valueOf(wholeDeposit));
                } catch (Exception e) {
                    LOGGER.error("DEPOSIT TRANSACTION IS FAILED");
                    CustomerConsole.displayMessage(e.getMessage());
                }
                wholeDeposit = 0.0;
            }
            CustomerConsole.displayMessage("Finish deposit transaction");
        } catch (AccountsByCustomerIDException ex) {
            LOGGER.error("DEPOSIT TRANSACTION IS FAILED. CAUSE getAccountByAccountNumber ", ex);
            CustomerConsole.displayMessage(ex.getMessage());
        }
    }

    protected void changePIN() {
        LOGGER.info("Start change PIN transaction");
        CustomerConsole.displayMessage("Enter your new PIN");
        String newPIN;
        try {
            newPIN = CustomerConsole.askPIN();
            LOGGER.info("Change PIN transaction. New PIN is entered");
            backendConnection.changePIN(ATM_ID, cardReader.getCard().getCardNumber(), newPIN);
            LOGGER.info("PIN has changed successfully");
            CustomerConsole.displayMessage("PIN has changed successfully");
        } catch (IncorrectPinException e) {
            LOGGER.error("INVALID PIN WAS INSERTED");
            LOGGER.error("CHANGE PIN TRANSACTION IS FAILED");
            CustomerConsole.displayMessage(e.getMessage());
        } catch (ChangePINTransactionException ex) {
            LOGGER.error("CHANGE PIN TRANSACTION IS FAILED", ex);
            CustomerConsole.displayMessage(ex.getMessage());
        }

    }

    protected void exit() throws CancelException {
        ejectCard();
        throw new CancelException("Have a good day " + "❤️");
    }

    public void ejectCard() {
        currentCustomer = null;
        cardReader.ejectCard();
    }

}
