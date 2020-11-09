import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class ATMAbstract implements ATMInterface {

    private final String ATM_ID;
    private CashDispenser cashDispenser;
    private CustomerConsole customerConsole;
    private final Scanner scanner;
    private Card card;

    public ATMAbstract(final String ATM_ID) {
        this.ATM_ID = ATM_ID;
        cashDispenser = new CashDispenser();
        customerConsole = new CustomerConsole();
        scanner = new Scanner(System.in);
        card = null;
    }

    public String getATM_ID() {
        return ATM_ID;
    }

    @Override
    public void startATM() {
        boolean atm_is_on = true;
        System.out.println("ATM's Home Screen");
            while (atm_is_on) {

                card = readCard();
                if (card != null) {
                    if (card.getID().equals("EDUARD_MATVEEV") && card.getPin().equals(1111)) {
                        atm_is_on = false;
                        System.out.println("ATM SWITCH OFF");
                    } else {
                        System.out.println("Welcome To Main Menu");
                        displayActions();
                        int selected_action = chooseAction();
                        System.out.println("Now, we are in startATM");
                        executeSelectedAction(selected_action);
                        //Calling function depends on an action number
                    }

                }
                ejectCard();
            }
    }

    protected boolean verifyCard() {
        //send card.getID and card.getPIN to Bank (that is DB) to verify
        return false;
    }


    protected Card readCard() {

        String card_id;
        Integer pin;

        int attempts_count = 0;
        final int MAX_ATTEMPTS = 3;
        while (attempts_count < MAX_ATTEMPTS) {
            attempts_count += 1;
            card_id =  //readID();
            pin = readPIN();
            if (card_id != null && pin != null) {
                return new Card(card_id, pin);
            }
            System.out.println("Something went wrong, try again");
        }

        return null;
    }

    protected String readID() {

        System.out.print("Insert your card ID: ");
        String card_id = in.nextLine();
        if (cardIdIsValid(card_id)) {
            return card_id;
        }
        return null;
    }

    private boolean cardIdIsValid(String ID) {
        return ID.length() >= Card.ID_MIN_LENGTH &&
                ID.length() <= Card.ID_MAX_LENGTH;
    }

    protected Integer readPIN() {

        Integer pin = customerConsole.askPIN();
        if(pinIsValid(pin)) {
            return pin;
        }
        return null;
    }

    private boolean pinIsValid(Integer pin) {
        return pin.toString().length() == Card.PIN_LENGTH;
    }

    protected void ejectCard() {
        try {
            System.out.println("Ejecting your card " + card.getID());
        } catch(NullPointerException ex) {
            System.out.println(ex.getMessage());
            System.out.println("Ejecting your card. If you forgot your password do something");
        }

        card = null;
        //receiptPrinter();
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

    protected void displayActions() {
        System.out.println("Check Balance - " + Actions.CHECK_BALANCE.getAction());
        System.out.println("Withdrawal    - " + Actions.WITHDRAWAL.getAction());
        System.out.println("Deposit       - " + Actions.DEPOSIT.getAction());
        System.out.println("Change PIN    - " + Actions.PIN_CHANGE.getAction());
        System.out.println("Exit          - " + Actions.EXIT.getAction());
    }

    protected int chooseAction() {
        System.out.print("Please, select an action number: ");
        int selected_action = in.nextInt();
        try {
            return Actions.values()[selected_action].getAction();
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Your input is wrong");
        }
        return Actions.WRONG_ACTION.getAction();
    }

    protected void executeSelectedAction(int action) {
        switch (Actions.values()[action]) {
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
        System.out.println("exit");
    }
}