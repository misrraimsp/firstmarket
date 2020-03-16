package misrraimsp.uned.pfg.firstmarket.config.sql;

public class IdSequenceHolder {

    private int id;

    public IdSequenceHolder(){
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


