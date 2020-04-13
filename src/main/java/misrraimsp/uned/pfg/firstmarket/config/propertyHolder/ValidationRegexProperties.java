package misrraimsp.uned.pfg.firstmarket.config.propertyHolder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "validation.regex")
public class ValidationRegexProperties {

    private String email = "";
    private String password = "";
    private String textBasic = "";
    private String textLong = "";
    private String textQuery = "";
    private String isbnCode = "";
    private String isbnFilter = "";
    private String imageMimeType = "";
    private String imageName = "";
    private String year = "";
    private String price = "";
}
