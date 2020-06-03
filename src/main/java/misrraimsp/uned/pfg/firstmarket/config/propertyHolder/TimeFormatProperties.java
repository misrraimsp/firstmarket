package misrraimsp.uned.pfg.firstmarket.config.propertyHolder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
@Data
@ConfigurationProperties(prefix = "fm.time-format")
public class TimeFormatProperties {

    private String date = "dd/MM/yy";
    private String dateTime = "dd/MM/yy HH.mm.ss";

    public DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern(date);
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(dateTime);
    }
}
