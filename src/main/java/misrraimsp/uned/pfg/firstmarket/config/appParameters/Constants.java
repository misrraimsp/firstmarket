package misrraimsp.uned.pfg.firstmarket.config.appParameters;

import java.util.Map;

public interface Constants {

    String EMAIL = "^[_A-Za-z0-9-+]+(.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(.[A-Za-z0-9]+)*(.[A-Za-z]{2,})$";

    /**
     * The password must be at least 8 characters, with at least one lowercase, one uppercase and one number
     */
    String PASSWORD = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$";

    /**
     * This field must have zero to thirty characters between:
     * middle hyphen (-), space, underscore (_), letters or numbers
     */
    String TEXT_BASIC = "^[ \\w-]{0,30}$";

    String TEXT_LONG = "^[\\s\\w-.,:;\"?¿¡!()ñáéíóú]{0,250}$";

    String ISBN = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";

    String ISBN_FILTER = "[- ]|^ISBN(?:-1[03])?:?";

    String IMAGE_MIME_TYPE = "^image\\/.*$";

    String IMAGE_NAME = "^[ \\w-._]{1,30}$";

    int MIN_NUM_AUTHORS = 1;
    int MAX_NUM_AUTHORS = 5;

    int MIN_NUM_PAGES = 1;
    int MAX_NUM_PAGES = 5000;

    int MAX_INTEGER_PRICE = 9;  //maximum number of integral digits accepted for price numbers
    int MAX_FRACTION_PRICE = 2; //maximum number of fractional digits accepted for price numbers
    String PRICE = "^[\\d]{0," + MAX_INTEGER_PRICE + "}([.][\\d]{1," + MAX_FRACTION_PRICE + "})*$";

    //front end
    int NUM_TOP_AUTHORS = 5;
    int NUM_TOP_PUBLISHERS = 10;
    int NUM_TOP_LANGUAGES = 5;
    String DEFAULT_PAGE_SIZE = "30";
    String DEFAULT_PAGE_NUMBER = "0";
    String DEFAULT_CATEGORY_ID = "1";



    int MIN_NUM_STOCK = 0;
    int MAX_NUM_STOCK = 100000000;

    String YEAR = "^[\\d]{1,4}$";

    Map<String, String> patterns = Map.of(
            "EMAIL", EMAIL,
            "PASSWORD", PASSWORD,
            "TEXT_BASIC", TEXT_BASIC,
            "TEXT_LONG", TEXT_LONG,
            "ISBN", ISBN,
            "ISBN_FILTER", ISBN_FILTER,
            "IMAGE_MIME_TYPE", IMAGE_MIME_TYPE,
            "IMAGE_NAME", IMAGE_NAME,
            "PRICE", PRICE,
            "YEAR", YEAR
    );

    Map<String, Integer> numbers = Map.of(
            "MAX_NUM_AUTHORS", MAX_NUM_AUTHORS,
            "MIN_NUM_PAGES", MIN_NUM_PAGES,
            "MAX_NUM_PAGES", MAX_NUM_PAGES,
            "MAX_INTEGER_PRICE", MAX_INTEGER_PRICE,
            "MAX_FRACTION_PRICE", MAX_FRACTION_PRICE,
            "MIN_NUM_STOCK", MIN_NUM_STOCK,
            "MAX_NUM_STOCK", MAX_NUM_STOCK
    );
}
