package misrraimsp.uned.pfg.firstmarket.converter.spring;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Language;
import misrraimsp.uned.pfg.firstmarket.exception.NoLanguageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLanguageConverter implements Converter<String, Language> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public Language convert(String s) {
        if (Language.values().length == 0) {
            throw new NoLanguageException();
        }
        int maxIndex = Language.values().length - 1;
        int index;
        try {
            index = Integer.parseInt(s);
            if (index > maxIndex) {
                LOGGER.error("Language index string out of bound. Defaults to index=0");
                index = 0;
            }
        }
        catch (NumberFormatException e) {
            LOGGER.error("Language index string can not be converted due to format exception. Defaults to index=0", e);
            index = 0;
        }
        return Language.values()[index];
    }
}
