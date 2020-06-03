package misrraimsp.uned.pfg.firstmarket.config.propertyHolder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Data
@ConfigurationProperties(prefix = "fm.date")
public class DateProperties {

    private String format = "dd.MM.yy";

    public DateTimeFormatter getFormatter() {
        return DateTimeFormatter.ofPattern(format);
    }
}
