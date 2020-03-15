package misrraimsp.uned.pfg.firstmarket.config.sqlCatConfig;

public class LevelManager {

    private int level;

    public LevelManager(){
        this.reset();
    }

    public void increment(){
        level++;
    }

    public void decrement(){
        level--;
    }

    public int getLevel(){
        return level;
    }

    public void reset(){
        level = 0;
    }
}
