public class DataBase extends DataBaseAbstract{
    public DataBase() {
        super();
        Card card1 = new Card("1234567812345678");
        Card card2 = new Card("1234567812345679");

        CardAccount acc1 = new CardAccount("2345543221", AccountCurrency.AMD);
        acc1.addCard(card1);
        acc1.addCard(card2);

        CurrentAccount acc2 = new CurrentAccount("124375023", AccountCurrency.RUB);
        Customer customer1 = new Customer("ARTAK_01", "+37499099448", "artakkirakosyan96@mail.ru", "Artak", "Kirakosyan");
        customer1.addAccount(acc1);
        customer1.addAccount(acc2);
        Card card3 = new Card("1234567812345688");
        Card card4 = new Card("1234567812345689");

        CardAccount acc3 = new CardAccount("2345543222", AccountCurrency.AMD);
        acc3.addCard(card3);
        acc3.addCard(card4);

        CurrentAccount acc4 = new CurrentAccount("124375024", AccountCurrency.RUB);
        SavingsAccount acc5 = new SavingsAccount("3838538", AccountCurrency.USD);
        Customer customer2 = new Customer("EDO_02", "+37455033354", "dev.edomatveev@gmail.com", "Edo", "Matveev");
        customer2.addAccount(acc3);
        customer2.addAccount(acc4);
        customer2.addAccount(acc5);

        this.addCustomer(customer1);
        this.addCustomer(customer2);
    }
}
