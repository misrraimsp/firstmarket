package misrraimsp.uned.pfg.firstmarket.converter;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PriceIntervals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class StringToPriceIntervalConverter implements Converter<String, PriceIntervals> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public PriceIntervals convert(String s) {
        if (PriceIntervals.values().length == 0) {
            LOGGER.error("There is no PriceInterval defined that can be used");
            return null;
        }
        int maxIndex = PriceIntervals.values().length - 1;
        int index;
        try {
            index = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            LOGGER.warn("PriceInterval index string can not be converted due to format exception. Defaults to index=0", e);
            index = 0;
        }
        if (index > maxIndex) {
            LOGGER.warn("PriceInterval index string out of bound. Defaults to index=0");
            index = 0;
        }
        return PriceIntervals.values()[index];
    }
}
