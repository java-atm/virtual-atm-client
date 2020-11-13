import java.util.LinkedHashMap;

public class DataBase extends DataBaseAbstract{
    public DataBase() {
        super();
        LinkedHashMap<CardInfo, String> c1 = new LinkedHashMap<>();
        c1.put(CardInfo.NAME, "Artak");
        c1.put(CardInfo.SURNAME, "Kirakosyan");
        c1.put(CardInfo.CARD_NUMBER, "1234567891123111");
        Card card1 = new Card(c1);

        CardAccount acc1 = new CardAccount("2345543221", AccountCurrency.AMD, card1);
        CurrentAccount acc2 = new CurrentAccount("124375023", AccountCurrency.RUB);
        Customer customer1 = new Customer("ARTAK_01", "+37499099448", "artakkirakosyan96@mail.ru", "Artak", "Kirakosyan");
        customer1.addAccount(acc1);
        customer1.addAccount(acc2);

        LinkedHashMap<CardInfo, String> c2 = new LinkedHashMap<>();
        c2.put(CardInfo.NAME, "Edo");
        c2.put(CardInfo.SURNAME, "Matveev");
        c2.put(CardInfo.CARD_NUMBER, "1234567891123112");
        Card card2 = new Card(c2);

        CardAccount acc3 = new CardAccount("2345543222", AccountCurrency.AMD, card2);
        CurrentAccount acc4 = new CurrentAccount("124375024", AccountCurrency.RUB);
        SavingsAccount acc5 = new SavingsAccount("3838538", AccountCurrency.USD);
        Customer customer2 = new Customer("EDO_02", "+37455033354", "dev.edomatveev@gmail.com", "Edo", "Matveev");
        customer2.addAccount(acc3);
        customer2.addAccount(acc4);
        customer2.addAccount(acc5);

        LinkedHashMap<CardInfo, String> c3 = new LinkedHashMap<>();
        c2.put(CardInfo.NAME, "Tatev");
        c2.put(CardInfo.SURNAME, "Khachaturyan");
        c2.put(CardInfo.CARD_NUMBER, "9994567891123112");
        Card card3 = new Card(c3);

        CardAccount acc6 = new CardAccount("9999543222", AccountCurrency.AMD, card3);
        CurrentAccount acc7 = new CurrentAccount("999375024", AccountCurrency.RUB);
        SavingsAccount acc8 = new SavingsAccount("9998538", AccountCurrency.USD);
        Customer customer3 = new Customer("TATEV_03", "+37499999999", "tatev@tatev.com", "Tatev", "Khachaturyan");
        customer2.addAccount(acc6);
        customer2.addAccount(acc7);
        customer2.addAccount(acc8);

        this.addCustomer(customer1);
        this.addCustomer(customer2);
        this.addCustomer(customer3);

    }
}
