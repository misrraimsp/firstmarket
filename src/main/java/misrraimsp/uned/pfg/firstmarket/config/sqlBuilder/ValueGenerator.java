package misrraimsp.uned.pfg.firstmarket.config.sqlBuilder;


import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Language;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.OrderStatus;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.ProductStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * app properties are overwritten in this dev-class
 */
public class ValueGenerator extends Random {

    private static final int MIN_LANGUAGE = 0;
    private static final int MAX_LANGUAGE = 27;

    private static final int MIN_YEAR = 1950;
    private static final int MAX_YEAR = 2020;

    private static final double MIN_PRICE = .5;
    private static final double MAX_PRICE = 100;

    private static final int MIN_ID = 1;
    private static final int MIN_CART_ID = 2; // admin cart useless
    private static final int MAX_CART_ID = 64;
    private static final int MIN_USER_ID = 2; // admin dont order
    private static final int MAX_USER_ID = 64;
    private static final int MAX_CATEGORY_ID = 310;
    private static final int MAX_IMAGE_ID = 29;
    private static final int MAX_PUBLISHER_ID = 128;
    private static final int MAX_AUTHOR_ID = 128;
    private static final int MAX_BOOK_ID = 1000; // link with BookBuilder's numBooks parameter
    private static final int MAX_ADDRESS_ID = 6;

    private static final int MIN_NUM_AUTHORS = 1;
    private static final int MAX_NUM_AUTHORS = 3;

    private static final int MIN_NUM_PAGES = 1;
    private static final int MAX_NUM_PAGES = 5000;

    private static final int MAX_FRACTION_PRICE = 2;

    private static final int MIN_STATUS = 1;
    private static final int MAX_STATUS = 10;

    private static final int MIN_NUM_STOCK = 1;
    private static final int MAX_NUM_STOCK = 10;

    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 4;

    private static final int MIN_SALES_PEDIDO = 1;
    private static final int MAX_SALES_PEDIDO = 3;

    private static final int MIN_PAST_TIME_UNITS = 0;
    private static final int MAX_PAST_TIME_UNITS = 10000;



    public String getRandomLanguage(){
        return Language.values()[getDiscreteRandomNumber(MIN_LANGUAGE, MAX_LANGUAGE)].name();
    }

    public String getRandomNumPages(){
        return String.valueOf(getDiscreteRandomNumber(MIN_NUM_PAGES, MAX_NUM_PAGES));
    }

    public String getRandomPrice(){
        BigDecimal bd = BigDecimal.valueOf(getContinuousRandomNumber(MIN_PRICE, MAX_PRICE)).setScale(MAX_FRACTION_PRICE, RoundingMode.HALF_UP);
        return bd.toString();
    }

    public String getRandomBookStatus(){
        int randomNumber = getDiscreteRandomNumber(MIN_STATUS, MAX_STATUS);
        switch (randomNumber) {
            case 1:
                return ProductStatus.DISABLED.name();
            case 2:
            case 3:
                return ProductStatus.OUT_OF_STOCK.name();
            default:
                return ProductStatus.OK.name();
        }
    }

    public String getRandomStock(){
        return String.valueOf(getDiscreteRandomNumber(MIN_NUM_STOCK, MAX_NUM_STOCK));
    }

    public String getRandomYear(){
        return String.valueOf(getDiscreteRandomNumber(MIN_YEAR, MAX_YEAR));
    }

    public String getRandomCategoryId(){
        return String.valueOf(getDiscreteRandomNumber(MIN_ID, MAX_CATEGORY_ID));
    }

    public String getRandomImageId(){
        return String.valueOf(getDiscreteRandomNumber(MIN_ID, MAX_IMAGE_ID));
    }

    public String getRandomPublisherId(){
        return String.valueOf(getDiscreteRandomNumber(MIN_ID, MAX_PUBLISHER_ID));
    }

    public int getRandomNumOfAuthors() {
        return getDiscreteRandomNumber(MIN_NUM_AUTHORS, MAX_NUM_AUTHORS);
    }

    public String getRandomAuthorId() {
        return String.valueOf(getDiscreteRandomNumber(MIN_ID, MAX_AUTHOR_ID));
    }

    public String getRandomQuantity(){
        return String.valueOf(getDiscreteRandomNumber(MIN_QUANTITY, MAX_QUANTITY));
    }

    public String getRandomBookId(){
        return String.valueOf(getDiscreteRandomNumber(MIN_ID, MAX_BOOK_ID));
    }

    public String getRandomCartId() {
        return String.valueOf(getDiscreteRandomNumber(MIN_CART_ID, MAX_CART_ID));
    }

    public String getRandomAddressId() {
        return String.valueOf(getDiscreteRandomNumber(MIN_ID, MAX_ADDRESS_ID));
    }

    public String getRandomDateTime(){
        return LocalDateTime
                .now()
                .minusDays(getDiscreteRandomNumber(MIN_PAST_TIME_UNITS, MAX_PAST_TIME_UNITS))
                .minusSeconds(getDiscreteRandomNumber(MIN_PAST_TIME_UNITS, MAX_PAST_TIME_UNITS))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getRandomUserId() {
        return String.valueOf(getDiscreteRandomNumber(MIN_USER_ID, MAX_USER_ID));
    }

    public String getRandomNumOfSales(){
        return String.valueOf(getDiscreteRandomNumber(MIN_SALES_PEDIDO, MAX_SALES_PEDIDO));
    }

    public String getRandomPedidoStatus() {
        int randomNumber = getDiscreteRandomNumber(MIN_STATUS, MAX_STATUS);
        switch (randomNumber) {
            case 1:
                return OrderStatus.PROCESSING.name();
            case 2:
            case 3:
                return OrderStatus.SHIPPING.name();
            default:
                return OrderStatus.COMPLETED.name();
        }
    }
}
