package misrraimsp.uned.pfg.firstmarket.config.staticParameter;

public enum Language {

    BULGARIAN ("Bulgarian"),
    CROATIAN ("Croatian"),
    CZECH ("Czech"),
    DANISH ("Danish"),
    DUTCH ("Dutch"),
    ENGLISH ("English"),
    ESTONIAN ("Estonian"),
    FINNISH ("Finnish"),
    FRENCH ("French"),
    GERMAN ("German"),
    GREEK ("Greek"),
    HUNGARIAN ("Hungarian"),
    IRISH ("Irish"),
    ITALIAN ("Italian"),
    LATVIAN ("Latvian"),
    LITHUANIAN ("Lithuanian"),
    MALTESE ("Maltese"),
    POLISH ("Polish"),
    PORTUGUESE ("Portuguese"),
    ROMANIAN ("Romanian"),
    SLOVAK ("Slovak"),
    SLOVENE ("Slovene"),
    SPANISH ("Spanish"),
    SWEDISH ("Swedish"),
    CATALAN ("Catalan"),
    VALENCIAN ("Valencian"),
    GALICIAN ("Galician"),
    BASQUE ("Basque");

    private final String text;

    Language(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
