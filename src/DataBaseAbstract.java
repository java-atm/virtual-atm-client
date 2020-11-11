import java.util.ArrayList;

public abstract class DataBaseAbstract implements DataBaseInterface{
    private final ArrayList<Customer> customers;
    private static final int INITIAL_CUSTOMER_COUNT = 10;

    public DataBaseAbstract() {
        customers = new ArrayList<>(INITIAL_CUSTOMER_COUNT);
    }

    public void addCustomer(Customer newCustomer) {
        customers.add(newCustomer);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

}
