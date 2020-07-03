package misrraimsp.uned.pfg.firstmarket.util.converter.spring;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PageSize;
import misrraimsp.uned.pfg.firstmarket.util.exception.NoPageSizeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToPageSizeConverter implements Converter<String, PageSize> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public PageSize convert(String s) {
        if (PageSize.values().length == 0) throw new NoPageSizeException();
        try {
            int index = Integer.parseInt(s);
            int maxIndex = PageSize.values().length - 1;
            if (index > maxIndex) {
                LOGGER.warn("PageSize index({}) out-of-bound({}). Defaults to {}", index, maxIndex, PageSize.M);
                return PageSize.M;
            }
            return PageSize.values()[index];
        }
        catch (NumberFormatException e) {
            LOGGER.error("PageSize index string({}) can not be converted due to format exception. Defaults to {}", s, PageSize.M, e);
            return PageSize.M;
        }
    }
}
