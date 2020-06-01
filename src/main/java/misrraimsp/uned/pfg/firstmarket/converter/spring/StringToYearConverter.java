package misrraimsp.uned.pfg.firstmarket.converter.spring;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class StringToYearConverter implements Converter<String, Year> {

    @Override
    public Year convert(String s) {
        if (s.isBlank()) {
            return Year.now();
        }
        return Year.of(Integer.parseInt(s));
    }
}
