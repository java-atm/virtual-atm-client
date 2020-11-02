import java.util.InputMismatchException;
import java.util.Scanner;

public abstract class ATMAbstract implements ATMInterface {

    private final String ATM_ID;
    private Card card;
    private CashDispenser cashDispenser;

    public ATMAbstract(final String ATM_ID) {
        this.ATM_ID = ATM_ID;
        cashDispenser = new CashDispenser();
        card = null;
    }

    public String getATM_ID() {
        return ATM_ID;
    }

    @Override
    public void startATM() {

    }

    protected boolean verifyCard() {
        //send card.getID and card.getPIN to Bank (that is DB) to verify
        return false;
    }


    protected Card readCard() {

        Scanner in = new Scanner(System.in);
        String card_id;
        Integer pin;

        int attempt_count = 0;
        final int MAX_ATTEMPT = 3;
        while (attempt_count < MAX_ATTEMPT) {
            attempt_count += 1;
            card_id = readID();
            pin = readPIN();
            if (card_id != null && pin != null) {
                return new Card(card_id, pin);
            }
        }

        return null;
    }

    protected String readID() {
        Scanner in = new Scanner(System.in);
        System.out.print("Insert your card ID: ");
        String card_id = in.nextLine();
        if (cardIdIsValid(card_id)) {
            return card_id;
        }
        return null;
    }

    private boolean cardIdIsValid(String ID) {
        return ID.length() > Card.ID_MIN_LENGTH &&
                ID.length() < Card.ID_MAX_LENGTH;
    }

    protected Integer readPIN() {
        Scanner in = new Scanner(System.in);
        System.out.print("Insert your pin: ");
        Integer pin;
        try {
            pin = in.nextInt();
        } catch (InputMismatchException ex) {
            System.out.println(ex.toString());
            return null;
        }

        if(pinIsValid(pin)) {
            return pin;
        }
        return null;
    }

    private boolean pinIsValid(Integer pin) {
        return pin.toString().length() == Card.PIN_LENGTH;
    }


    protected void ejectCard() {
        System.out.println("Ejecting your card " + card.getID());
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


    protected void dispenseCash(Cash cash) {

    }
}