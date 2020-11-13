public class CurrentAccount extends Account{
    public CurrentAccount(String accountNumber, AccountCurrency accountCurrency) {
        super(accountNumber, AccountType.CURRENT, accountCurrency);
    }
}
