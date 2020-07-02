package misrraimsp.uned.pfg.firstmarket.config.sqlBuilder;

public class CategoryLevelHolder {

    private int level;

    public CategoryLevelHolder(){
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
