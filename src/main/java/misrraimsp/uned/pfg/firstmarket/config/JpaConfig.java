package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.data.AuditorAwareImplementation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImplementation();
    }

}