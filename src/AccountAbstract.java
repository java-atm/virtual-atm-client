public abstract class AccountAbstract implements AccountInterface{
    private final String accountNumber;
    private final AccountType accountType;
    private final AccountCurrency accountCurrency;

    public AccountAbstract(String accountNumber, AccountType accountType, AccountCurrency accountCurrency) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountCurrency = accountCurrency;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Enum<AccountType> getAccountType() {
        return accountType;
    }

    public Enum<AccountCurrency> getAccountCurrency() {
        return accountCurrency;
    }
}
