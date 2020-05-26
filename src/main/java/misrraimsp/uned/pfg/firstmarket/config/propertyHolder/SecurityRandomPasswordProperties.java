package misrraimsp.uned.pfg.firstmarket.config.propertyHolder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "fm.security.random-password")
public class SecurityRandomPasswordProperties {

    private int numOfLowerCase = 2;
    private int numOfUpperCase = 2;
    private int numOfDigit = 2;
    private int size = 10;

}
