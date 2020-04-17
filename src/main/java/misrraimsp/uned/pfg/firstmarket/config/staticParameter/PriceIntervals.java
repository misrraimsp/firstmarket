package misrraimsp.uned.pfg.firstmarket.config.staticParameter;

import java.math.BigDecimal;

public enum PriceIntervals {

    PRICE_0("€ 0-5", 0, 5),
    PRICE_1("€ 5-10", 5, 10),
    PRICE_2("€ 10-15", 10, 15),
    PRICE_3("€ 15-50", 15, 50),
    PRICE_4("€ 50-100", 50, 100),
    PRICE_5("€ 100 or more", 100, 1000000000);

    private final String text;
    private final int lowLimit;
    private final int highLimit;

    PriceIntervals(String text, int lowLimit, int highLimit) {
        this.text = text;
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
    }

    public String getText() {
        return text;
    }

    public BigDecimal getLowLimit() {
        return new BigDecimal(lowLimit);
    }

    public BigDecimal getHighLimit(){
        return new BigDecimal(highLimit);
    }
}
