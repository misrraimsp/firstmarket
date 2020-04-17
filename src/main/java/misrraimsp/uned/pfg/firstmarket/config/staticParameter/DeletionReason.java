package misrraimsp.uned.pfg.firstmarket.config.staticParameter;


public enum DeletionReason {

    DUPLICATE ("I have a duplicate account"),
    MANY_EMAILS ("I'm getting too many emails"),
    NOT_VALUE ("I'm not getting any value from my membership"),
    PRIVACY ("I have a privacy concern"),
    UNWANTED_CONTACT ("I'm receiving unwanted contact"),
    OTHER ("Other");


    private final String text;

    DeletionReason(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
