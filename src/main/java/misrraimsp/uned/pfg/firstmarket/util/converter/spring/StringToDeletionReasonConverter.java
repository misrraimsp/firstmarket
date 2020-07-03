package misrraimsp.uned.pfg.firstmarket.util.converter.spring;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.DeletionReason;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToDeletionReasonConverter implements Converter<String, DeletionReason> {

    @Override
    public DeletionReason convert(String s) {
        return DeletionReason.values()[Integer.parseInt(s)];
    }
}
