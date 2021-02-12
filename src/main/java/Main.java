import com.atm.ATM;
import com.utils.cash.RealCash;
import com.utils.exceptions.InvalidConfigFileException;

public class Main {
    public static void main(String[] args) throws InvalidConfigFileException {

        ATM atm = new ATM("src/main/resources/atm_config.json", "src/main/resources/cash_holder.json");
//        atm.startATM();
//        RealCash r = new RealCash(111000);
//        System.out.println(r.toJson());
    }
}
