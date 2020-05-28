package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.exception.NoDefaultImageException;
import misrraimsp.uned.pfg.firstmarket.exception.NoRootCategoryException;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.nio.file.Files;
import java.nio.file.Paths;

@Profile("dev-postgresql") //comment @Lob on image.data
@Configuration
public class DevPostgresqlConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Bean
    public CommandLineRunner dataLoader(CatServer catServer,
                                        ImageServer imageServer) {

        return args -> {
            LOGGER.warn("CommandLineRunner on dev-postgresql");

            try {
                //set default image data
                imageServer.setDefaultData(Files.readAllBytes(Paths.get("src/main/resources/static/images/logo.png")));
                //load categories
                catServer.loadCategories();
            }
            catch (NoRootCategoryException e) {
                LOGGER.error("Root category not found: ", e);
            }
            catch (NoDefaultImageException e) {
                LOGGER.error("Default image not found: ", e);
            }
        };
    }
}
