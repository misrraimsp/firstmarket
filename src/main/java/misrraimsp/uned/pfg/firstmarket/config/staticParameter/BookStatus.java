package misrraimsp.uned.pfg.firstmarket.config.staticParameter;

public enum BookStatus {

    OK("Ok"),
    OUT_OF_STOCK("Sold out"),
    DISABLED("Disabled");

    private final String text;

    BookStatus(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
