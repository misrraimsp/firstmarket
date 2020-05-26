package misrraimsp.uned.pfg.firstmarket.config.propertyHolder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "fm.validation.numeric")
public class ValidationNumericProperties {

    private int[] pages = {1,2000}; // min, max
    private int[] stock = {0,1000000}; // min, max
    private int imageMaxSize = 500;

}
