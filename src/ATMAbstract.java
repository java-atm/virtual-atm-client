import java.util.LinkedHashMap;
import java.util.Scanner;

public abstract class ATMAbstract implements ATMInterface {

    private final String ATM_ID;
    private final CashDispenser cashDispenser;
    private final Scanner scanner;
    private final CardReader cardReader;
    //private final ReceiptPrinter;
    //private final Customer currentCustomer;
    //private final Transaction currentTransaction;

    public ATMAbstract(final String ATM_ID) {
        this.ATM_ID = ATM_ID;
        cashDispenser = new CashDispenser();
        scanner = new Scanner(System.in);
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
            readCard();
            if (cardReader.getCard() != null) {
                pin = CustomerConsole.askPIN();
                if (pin != null) {
                    if (cardReader.getCard().getName().equals("Eduard") && pin == 1111) {
                        atm_is_on = false;
                        CustomerConsole.displayMessage("ATM SWITCH OFF");
                    } else {
                        CustomerConsole.displayMessage("Welcome To Main Menu " + cardReader.getCard().getName());
                        Actions selectedAction = CustomerConsole.chooseAction();
                        performSelectedAction(selectedAction);
                    }
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
        }
    }

    protected Cash getAccountEntry() {
        return null;
    }

    protected void addCash(Cash cash) {
        System.out.println("Adding cash" + cash.toString());
        cashDispenser.addCash(cash);
    }

    protected void displayCash() {
        System.out.println(cashDispenser.getCash().toString());
    }


    protected boolean checkIfCashAvailable(Cash cash) {
        return false;
    }

    protected void performSelectedAction(Actions action) {
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

    protected void dispenseCash(Cash cash) {

    }

    protected void checkBalance() {
        System.out.println("checkBalance");
    }

    protected void withdrawal() {
        System.out.println("withdrawal");
    }

    protected void deposit() {
        System.out.println("deposit");
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

