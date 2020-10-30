import java.util.Objects;

public abstract class CashAbstract implements CashInterface {

    private double amount;

    public CashAbstract(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public void add(Cash amount) {
        this.amount += amount.getAmount();
    }

    @Override
    public void subtract(Cash amount) {
        this.amount -= amount.getAmount();
    }

    @Override
    public Boolean greaterThan(Cash amount) {
        return this.amount < amount.getAmount();
    }

    @Override
    public Boolean lessThan(Cash amount) {
        return this.amount > amount.getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CashAbstract)) return false;
        CashAbstract that = (CashAbstract) o;
        return Double.compare(that.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return "CashAbstract{" +
                "amount=" + amount +
                '}';
    }
}
