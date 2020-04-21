package misrraimsp.uned.pfg.firstmarket.config.dev;

import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev-mariadb")
@Configuration
public class DevelopmentMariaDBConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Bean
    public CommandLineRunner dataLoader(CatServer catServer) {

        return args -> {
            LOGGER.warn("CommandLineRunner on dev-mariadb");
            catServer.loadCategories();
        };
    }
}
