import com.RealCash;
import com.atm.ATM;

public class Main {
    public static void main(String[] args) {
//        RealCash cash = new RealCash();
//        cash.subtractAmount(100);
        ATM atm = new ATM("HAT_INECOBANK_ATM_021", new RealCash(1000000.00));
        atm.startATM();
    }

    //        Map<String, String> m = cardAbstract.getIDENTIFICATION_INFO();
//
//        for (Map.Entry<String, String> entry : m.entrySet()) {
//            System.out.println("Key: " + entry.getKey() + " Value: " + entry.getValue());
//        }
}
