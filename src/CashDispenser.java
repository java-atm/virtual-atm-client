public class CashDispenser {
    Cash cash;

    public void setInitialCash(Cash initialCash) {
        this.cash = initialCash;
    }

    public Boolean checkCash(Cash amount) {
        return this.cash.greaterThan(amount);
    }

    public void dispenseCash(Cash amount) {
        if (this.checkCash(amount)) {
            this.cash.subtract(amount);
        }
        else {
            System.out.println("Cash not enough. Please call maintenance.");
        }
    }

    public void addCash(Cash amount) {
        this.cash.add(amount);
    }
}
