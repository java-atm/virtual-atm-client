import com.atm.ATM;
import com.utils.cash.RealCash;

public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM("HAT_INECOBANK_ATM_021", new RealCash(1000000.00));
        atm.startATM();
    }
}
