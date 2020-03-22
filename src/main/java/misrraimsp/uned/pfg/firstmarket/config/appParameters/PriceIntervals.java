package misrraimsp.uned.pfg.firstmarket.config.appParameters;

public enum PriceIntervals {
    PRICE_0("€ 0-5"),
    PRICE_1("€ 5-10"),
    PRICE_2("€ 10-15"),
    PRICE_3("€ 15-50"),
    PRICE_4("€ 50-100"),
    PRICE_5("€ 100 or more");

    private final String text;

    PriceIntervals(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
