public interface CashDispenserInterface {


    public void setInitialCash(Cash initialCash);

    public boolean checkCash(Cash amount);

    public boolean dispenseCash(Cash amount);

    public void addCash(Cash amount);


}
