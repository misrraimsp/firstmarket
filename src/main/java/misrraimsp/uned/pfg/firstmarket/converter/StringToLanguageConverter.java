package misrraimsp.uned.pfg.firstmarket.converter;

import misrraimsp.uned.pfg.firstmarket.config.appParameters.Languages;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLanguageConverter  implements Converter<String, Languages> {

    @Override
    public Languages convert(String s) {
        return Languages.values()[Integer.parseInt(s)];
    }
}
