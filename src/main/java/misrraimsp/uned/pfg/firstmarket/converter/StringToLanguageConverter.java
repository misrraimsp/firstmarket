package misrraimsp.uned.pfg.firstmarket.converter;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Language;
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
            LOGGER.error("There is no Language defined that can be used");
            return null;
        }
        int maxIndex = Language.values().length - 1;
        int index;
        try {
            index = Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            LOGGER.warn("Language index string can not be converted due to format exception. Defaults to index=0", e);
            index = 0;
        }
        if (index > maxIndex) {
            LOGGER.warn("Language index string out of bound. Defaults to index=0");
            index = 0;
        }
        return Language.values()[index];
    }
}
