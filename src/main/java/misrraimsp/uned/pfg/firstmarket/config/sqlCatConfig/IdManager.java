package misrraimsp.uned.pfg.firstmarket.config.sqlCatConfig;

public class IdManager {

    private int id;

    public IdManager(){
        this.reset();
    }

    public void increment(){
        id++;
    }

    public int getId(){
        return id;
    }

    public void reset(){
        id = 1;
    }
}


