public interface CashDispenserInterface {


    public void setInitialCash(Cash initialCash);

    public Boolean checkCash(Cash amount);

    public void dispenseCash(Cash amount);

    public void addCash(Cash amount);


}
