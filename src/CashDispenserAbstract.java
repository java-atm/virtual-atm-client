public class CashDispenserAbstract {


    private Cash cash;

    public CashDispenserAbstract(Cash initial) {
        this.setInitialCash(initial);
    }

    public CashDispenserAbstract() {
        Cash initial = new Cash(0);
        this.setInitialCash(initial);
    }

    public Cash getCash() {
        return cash;
    }

    public void setCash(Cash cash) {
        this.cash = cash;
    }

    public void setInitialCash(Cash initialCash) {
        if (this.cash == null) {
            this.cash = initialCash;
        } else {
            System.out.println("Initial cash is already set, call addCash to add cash.");
        }
    }

    public Boolean checkCash(Cash amount) {
        return this.cash.greaterThan(amount);
    }

    public void dispenseCash(Cash amount) {
        if (this.checkCash(amount)) {
            this.cash.subtract(amount);
        } else {
            System.out.println("Cash not enough. Please call maintenance.");
        }
    }

    public void addCash(Cash amount) {
        if (this.cash != null) {
            this.cash.add(amount);
        } else {
            System.out.println("Initial cash is not set.");
        }
    }


}
