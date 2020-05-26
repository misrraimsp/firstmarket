package misrraimsp.uned.pfg.firstmarket.config.propertyHolder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "fm.front-end.search-criteria")
public class FrontEndProperties {

    private int numOfAuthors = 1;
    private int numOfPublishers = 1;
    private int numOfLanguages = 1;
}
