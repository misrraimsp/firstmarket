package misrraimsp.uned.pfg.firstmarket.config.staticParameter;

public enum Gender {

    MALE ("Male"),
    FEMALE ("Female"),
    UNDEFINED ("I prefer not to specify");


    private final String text;

    Gender(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
