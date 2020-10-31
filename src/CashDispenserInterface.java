public interface CashDispenserInterface {


    public void setInitialCash(Cash initialCash);

    public boolean checkCash(Cash cash);

    public boolean dispenseCash(Cash cash);

    public void addCash(Cash cash);


}
