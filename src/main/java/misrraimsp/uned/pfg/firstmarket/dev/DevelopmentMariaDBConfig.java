package misrraimsp.uned.pfg.firstmarket.dev;

import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev-mariadb")
@Configuration
public class DevelopmentMariaDBConfig {

    @Bean
    public CommandLineRunner dataLoader(CatServer catServer) {

        return args -> {
            System.out.println("CommandLineRunner on dev-mariadb");
            //load categories
            catServer.loadCategories();
        };
    }
}
