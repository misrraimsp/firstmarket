package misrraimsp.uned.pfg.firstmarket.converter.spring;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.BookStatus;
import misrraimsp.uned.pfg.firstmarket.exception.NoEntityStatusException;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBookStatusConverter implements Converter<String, BookStatus> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public BookStatus convert(String s) {
        if (BookStatus.values().length == 0) {
            throw new NoEntityStatusException(Book.class.getSimpleName());
        }
        int maxIndex = BookStatus.values().length - 1;
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
        return BookStatus.values()[index];
    }
}
