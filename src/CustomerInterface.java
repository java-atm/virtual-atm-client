public interface CustomerInterface {
    Account getAccountByAccountNumber(String accountNumber);
    Card getCardByCardNumber(String cardNumber);
}
