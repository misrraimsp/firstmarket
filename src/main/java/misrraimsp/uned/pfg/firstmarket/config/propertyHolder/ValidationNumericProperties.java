package misrraimsp.uned.pfg.firstmarket.config.propertyHolder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "validation.numeric")
public class ValidationNumericProperties {


    private int pagesMin = 1;
    private int pagesMax = 5000;

    private int stockMin = 0;
    private int stockMax = 100000000;

    private int imageMaxSize = 500;

}
