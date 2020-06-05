package misrraimsp.uned.pfg.firstmarket.converter.spring;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.BookSortCriteria;
import misrraimsp.uned.pfg.firstmarket.exception.NoSortCriteriaException;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToBookSortCriteriaConverter implements Converter<String, BookSortCriteria> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public BookSortCriteria convert(String s) {
        if (BookSortCriteria.values().length == 0) throw new NoSortCriteriaException(Book.class.getSimpleName());
        try {
            int index = Integer.parseInt(s);
            int maxIndex = BookSortCriteria.values().length - 1;
            if (index > maxIndex) {
                LOGGER.warn("SortCriteriaBook index({}) out-of-bound({}). Defaults to {}", index, maxIndex, BookSortCriteria.PRICE_ASC);
                return BookSortCriteria.PRICE_ASC;
            }
            return BookSortCriteria.values()[index];
        }
        catch (NumberFormatException e) {
            LOGGER.error("SortCriteriaBook index string({}) can not be converted due to format exception. Defaults to {}", s, BookSortCriteria.PRICE_ASC, e);
            return BookSortCriteria.PRICE_ASC;
        }
    }
}
