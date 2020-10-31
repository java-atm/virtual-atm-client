public class CashDispenserAbstract implements CashDispenserInterface{


    private Cash cash;

    public CashDispenserAbstract(Cash initial) {
        setInitialCash(initial);
    }

    public CashDispenserAbstract() {
        Cash initial = new Cash();
        setInitialCash(initial);
    }

    public Cash getCash() {
        return cash;
    }

    public void setCash(Cash cash) {
        this.cash = cash;
    }

    public void setInitialCash(Cash initialCash) {
        if (cash == null) {
            cash = initialCash;
        } else {
            System.out.println("Initial cash is already set, call addCash to add cash.");
        }
    }

    public boolean checkCash(Cash amount) {
        return cash.greaterThan(amount);
    }

    public boolean dispenseCash(Cash amount) {
        boolean dispenseResult = true;
        if (checkCash(amount)) {
            cash.subtract(amount);
        } else {
            System.out.println("Cash not enough. Please call maintenance.");
            dispenseResult = false;
        }
        return dispenseResult;
    }

    public void addCash(Cash amount) {
        if (cash != null) {
            cash.add(amount);
        } else {
            System.out.println("Initial cash is not set.");
        }
    }


}
