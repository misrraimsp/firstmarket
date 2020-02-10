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

    String ISBN_PATTERN = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
}
