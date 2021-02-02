package com.atm;

import com.Customer;
import com.RealCash;
import com.atm.card_reader.CardReader;
import com.atm.cash_dispenser.CashDispenser;
import com.atm.customer_console.CustomerConsole;
import com.backend_connection.BackendConnection;
import com.utils.enums.Action;
import com.utils.exceptions.CancelException;
import com.utils.exceptions.CardIsInvalidException;
import com.utils.exceptions.CashNotEnoughException;
import com.utils.exceptions.IncorrectPinException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.HashMap;

public class ATM implements ATMInterface {

    private static final Logger LOGGER = LogManager.getLogger(ATM.class);

    private final String ATM_ID;
    private final CashDispenser cashDispenser;
    private final CardReader cardReader;
    //private final ReceiptPrinter;
    private Customer currentCustomer;
    private final BackendConnection backendConnection;
    private HashMap<String, BigDecimal> accounts;
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
        LOGGER.info("ATM started");
        while (true) {
            CustomerConsole.displayMessage(ATM_ID + " Home Screen");
            String pin;
            try {
                readCard();
                pin = CustomerConsole.askPIN();
                LOGGER.info("Card number : {}", cardReader.getCard().getCardNumber());
                String customerID = backendConnection.authenticate(ATM_ID, cardReader.getCard(), pin);
                LOGGER.info("Customer ID: {}", customerID);
                currentCustomer = new Customer(customerID,
                                               cardReader.getCard().getName(),
                                               cardReader.getCard().getSurname());
                serveCustomer();
            } catch (CancelException exception) {
                if (exception.getMessage().equals("ATM POWER OFF")) break;
                CustomerConsole.displayMessage(exception.getMessage());
            } catch (Exception exception) {
                CustomerConsole.displayMessage(exception.getMessage());
                LOGGER.error(exception);
                //CustomerConsole.displayMessage(Arrays.toString(exception.getStackTrace()));
            } finally {
                ejectCard();
                CustomerConsole.displayMessage("Card is ejecting");
                LOGGER.info("Card is ejected");
                CustomerConsole.displayMessage("Please take your card");
                checkCustomerInfoIsDeleted();
            }
        }
    }

    private void checkCustomerInfoIsDeleted() {
        try {
            cardReader.getCard();
            LOGGER.warn("CARD INFO IS NOT DELETED: {}", cardReader.getCard().getCardNumber());
        } catch (NullPointerException ex) {
            LOGGER.info("Card info is deleted");
        }
        if (currentCustomer != null) {
            LOGGER.warn("CUSTOMER INFO IS NOT DELETED: {}", currentCustomer.getCustomerID());
        } else {
            LOGGER.warn("Customer info is deleted");
        }
    }

    private void serveCustomer() throws CancelException {
        LOGGER.info("Start serving customer");
        while(true) {
            CustomerConsole.displayMessage("Welcome To Main Menu " + currentCustomer.getName());
            Action selectedAction = CustomerConsole.chooseAction();
            performSelectedAction(selectedAction);
            try {
                LOGGER.info("Dialog to choose another transaction");
                CustomerConsole.displayMessage("Do you want to perform another transaction ? Yes (any) No (n)");
                CustomerConsole.continueOperation();
            } catch (CancelException exception) {
                LOGGER.info("Customer cancel transactions");
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
            String toAccount = CustomerConsole.askAccountNumber();
            String toAccountOwnerName = backendConnection.getAccountOwnerName(ATM_ID, toAccount);
            LOGGER.info("To account owner name: {}", toAccountOwnerName);
            CustomerConsole.displayMessage("Customer name: " + toAccountOwnerName);
            CustomerConsole.displayMessage("Enter amount which you want to transfer");
            BigDecimal amountForTransfer = CustomerConsole.askAmountForTransfer();
            LOGGER.info("Amount for transfer: {}", amountForTransfer);
            backendConnection.transfer(ATM_ID, fromAccount, toAccount, amountForTransfer.toString());
            CustomerConsole.displayMessage("Transfer performed successful");
            accounts = backendConnection.getAccountsByCustomerID(ATM_ID, currentCustomer.getCustomerID(), true);
            LOGGER.info("Transfer performed successful");
            CustomerConsole.displayAccounts(accounts);
        } catch (Exception ex) {
            LOGGER.error("Transfer transaction is failed");
            CustomerConsole.displayMessage(ex.getMessage());
        }

    }

    protected void checkBalance() {
        LOGGER.info("Start check balances");
        try {
            accounts = backendConnection.checkBalance(ATM_ID, currentCustomer.getCustomerID());
            CustomerConsole.displayAccounts(accounts);
            LOGGER.info("Check balances performed successful");
        } catch (Exception ex) {
            LOGGER.info("Check balance transaction is failed");
            CustomerConsole.displayMessage(ex.getMessage());
        }

    }

    private String getAccountByAccountNumber() throws Exception {
        LOGGER.info("Get account by account number");
        accounts = backendConnection.getAccountsByCustomerID(ATM_ID, currentCustomer.getCustomerID(), true);
        currentCustomer.setAccounts(accounts);
        CustomerConsole.displayAccounts(accounts);
        int accountNumberIndex = CustomerConsole.chooseAccountIndex(accounts.size());
        String account = currentCustomer.getAccountByAccountNumber(accountNumberIndex);
        LOGGER.info("Chosen account: {}", account);
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
            LOGGER.info("Account: {}, balance: {}", account, balance);
            double amount = CustomerConsole.askAmount();
            if (balance.compareTo(BigDecimal.valueOf(amount)) >= 0) {
                try {
                    LOGGER.info("Dispense cash from ATM");
                    cashDispenser.dispenseCash(amount);
                    backendConnection.withdraw(ATM_ID, account, BigDecimal.valueOf(amount));
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

        } catch (Exception e) {
            LOGGER.error("WITHDRAW TRANSACTION IS FAILED. CAUSE getAccountByAccountNumber ");
            CustomerConsole.displayMessage(e.getMessage());
        }

    }

    protected void deposit() {
        LOGGER.info("Start deposit transaction");
        CustomerConsole.displayMessage("Start deposit transaction");
        try {
            CustomerConsole.displayMessage("Choose account where you want to perform deposit");
            String account = getAccountByAccountNumber();
            LOGGER.info("Account: {}", account);
            double banknote;
            double wholeDeposit = 0.0;
            while (true) {
                try {
                    LOGGER.info("Start accepting cash");
                    banknote = CustomerConsole.acceptCash();
                    LOGGER.info("Adding banknote to ATM");
                    cashDispenser.addCash(banknote);
                    wholeDeposit += banknote;
                    CustomerConsole.displayMessage("Total: " + wholeDeposit);
                    LOGGER.info("Whole deposit: {}", wholeDeposit);
                    CustomerConsole.displayMessage("Continue operation? Yes(any), No(n)");
                    CustomerConsole.continueOperation();
                } catch (CancelException exception) {
                    LOGGER.info("Customer ended to insert banknotes");
                    break;
                }
            }
            if (wholeDeposit != 0.0) {
                try {
                    LOGGER.info("Preparing for performing deposit transaction when customer has already inserted all his money to ATM, but we won't going to performing deposit, because almost all people on the Earth is SHIT");
                    backendConnection.deposit(ATM_ID, account, new BigDecimal(wholeDeposit));
                    LOGGER.info("Deposit is performed successful");
                    CustomerConsole.displayMessage("Your deposit is: " + wholeDeposit);
                } catch (Exception e) {
                    LOGGER.error("DEPOSIT TRANSACTION IS FAILED");
                    CustomerConsole.displayMessage(e.getMessage());
                }
            }
            CustomerConsole.displayMessage("Finish deposit transaction");
        } catch (Exception e) {
            LOGGER.error("DEPOSIT TRANSACTION IS FAILED. CAUSE getAccountByAccountNumber ");
            CustomerConsole.displayMessage(e.getMessage());
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
        } catch (Exception ex) {
            LOGGER.error("CHANGE PIN TRANSACTION IS FAILED");
            CustomerConsole.displayMessage(ex.getMessage());
        }

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
