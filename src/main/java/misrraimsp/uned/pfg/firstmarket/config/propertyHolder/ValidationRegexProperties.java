package misrraimsp.uned.pfg.firstmarket.config.propertyHolder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "validation.regex")
public class ValidationRegexProperties {

    private final String defaultRegex = ".*";

    private String email = defaultRegex;

    private String password = defaultRegex;

    private String textBasic = defaultRegex;
    private String textLong = defaultRegex;
    private String textQuery = defaultRegex;

    private String isbnCode = defaultRegex;
    private String isbnFilter = defaultRegex;

    private String imageMimeType = defaultRegex;
    private String imageName = defaultRegex;

    private String year = defaultRegex;

    private String price = defaultRegex;

}
