package misrraimsp.uned.pfg.firstmarket.util.converter.spring;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.ProductStatus;
import misrraimsp.uned.pfg.firstmarket.core.model.Book;
import misrraimsp.uned.pfg.firstmarket.util.exception.NoEntityStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToProductStatusConverter implements Converter<String, ProductStatus> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public ProductStatus convert(String s) {
        if (ProductStatus.values().length == 0) {
            throw new NoEntityStatusException(Book.class.getSimpleName());
        }
        int maxIndex = ProductStatus.values().length - 1;
        int index;
        try {
            index = Integer.parseInt(s);
            if (index > maxIndex) {
                LOGGER.error("BookStatus index string out of bound. Defaults to index=0");
                index = 0;
            }
        }
        catch (NumberFormatException e) {
            LOGGER.error("BookStatus index string can not be converted due to format exception. Defaults to index=0", e);
            index = 0;
        }
        return ProductStatus.values()[index];
    }
}
