package misrraimsp.uned.pfg.firstmarket.config.propertyHolder;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "fm.security.lock")
public class SecurityLockProperties {

    private int numOfAttempts = 5;
    private int lockingMinutes = 10;
}
