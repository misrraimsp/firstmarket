package misrraimsp.uned.pfg.firstmarket.config.staticParameter;

public enum ProductStatus {

    OK("Ok"),
    OUT_OF_STOCK("Sold out"),
    DISABLED("Disabled");

    private final String text;

    ProductStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
