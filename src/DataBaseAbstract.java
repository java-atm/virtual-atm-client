import java.util.ArrayList;

public class DataBaseAbstract implements DataBaseInterface{
    private ArrayList<Customer> customers;
    private final int INITIAL_CUSTOMER_COUNT = 10;

    public DataBaseAbstract() {
        customers = new ArrayList<>(INITIAL_CUSTOMER_COUNT);
    }

    public void addCustomer(Customer newCustomer) {
        customers.add(newCustomer);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
    }

    @Override
    public Customer getCustomerByCard(Card card) {
        for (Customer customer:customers) {
            Card currentCard = customer.getCardByCardNumber(card.getCardNumber());
            if (currentCard != null) {
                return customer;
            }
        }
        return null;
    }
}
