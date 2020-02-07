package misrraimsp.uned.pfg.firstmarket.config;

public interface Patterns {

    String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

    /**
     * The password must be at least 8 characters, with at least one lowercase, one uppercase and one number
     */
    String PASSWORD_PATTERN = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    /**
     * This field must have zero to thirty characters between:
     * middle hyphen (-), space, underscore (_), letters or numbers
     */
    String W_0_30_$ = "^[ \\w-]{0,30}$";

    /**
     * provisional isbn pattern
     */
    String ISBN_PATTERN = W_0_30_$;
}
