public class SavingsAccount extends Account{
    public SavingsAccount(String accountNumber, AccountCurrency accountCurrency) {
        super(accountNumber, AccountType.SAVINGS, accountCurrency);
    }
}
