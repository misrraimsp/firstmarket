package misrraimsp.uned.pfg.firstmarket.config.propertyHolder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
@Data
@ConfigurationProperties(prefix = "fm.payment.stripe")
public class PaymentProperties {

    private Map<String, String> key = new HashMap<>();
    private int piMinutes = 7;
    private int limitOfNaps = 2;
    private Set<String> ips = new HashSet<>();

}
