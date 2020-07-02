package misrraimsp.uned.pfg.firstmarket.config.sqlBuilder;

public class Random {

    public int getDiscreteRandomNumber(int low_limit, int up_limit){
        return (int) (low_limit + Math.round(Math.random() * (up_limit - low_limit)));
    }

    public double getContinuousRandomNumber(double low_limit, double up_limit){
        return low_limit + Math.random() * (up_limit - low_limit);
    }
}
