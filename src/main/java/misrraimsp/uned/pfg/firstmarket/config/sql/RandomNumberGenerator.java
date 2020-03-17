package misrraimsp.uned.pfg.firstmarket.config.sql;

import misrraimsp.uned.pfg.firstmarket.config.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class RandomNumberGenerator extends Random implements Constants {

    private static final int MIN_LANGUAGE = 0;
    private static final int MAX_LANGUAGE = 4;

    private static final int MIN_YEAR = 1950;
    private static final int MAX_YEAR = 2020;

    private static final int MIN_PRICE = 0;
    private static final int MAX_PRICE = 100;

    private static final int MIN_ID = 1;
    private static final int MAX_CATEGORY_ID = 310;
    private static final int MAX_IMAGE_ID = 3;
    private static final int MAX_PUBLISHER_ID = 128;

    public String getRandomLanguage(){
        return String.valueOf(getDiscreteRandomNumber(MIN_LANGUAGE, MAX_LANGUAGE));
    }

    public String getRandomNumPages(){
        return String.valueOf(getDiscreteRandomNumber(MIN_NUM_PAGES, MAX_NUM_PAGES));
    }

    public String getRandomPrice(){
        BigDecimal bd = new BigDecimal(getContinuousRandomNumber(MIN_PRICE, MAX_PRICE)).setScale(MAX_FRACTION_PRICE, RoundingMode.HALF_UP);
        return bd.toString();
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

}
