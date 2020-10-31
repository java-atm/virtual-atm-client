public abstract class CashDispenserAbstract implements CashDispenserInterface{

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

    public boolean checkCash(Cash cash) {
        return this.cash.greaterThan(cash);
    }

    public boolean dispenseCash(Cash cash) {
        boolean dispenseResult = true;
        if (checkCash(cash)) {
            this.cash.subtract(cash);
        } else {
            System.out.println("Cash not enough. Please call maintenance.");
            dispenseResult = false;
        }
        return dispenseResult;
    }

    public void addCash(Cash cash) {
        if (this.cash != null) {
            this.cash.add(cash);
        } else {
            System.out.println("Initial cash is not set.");
        }
    }
}
