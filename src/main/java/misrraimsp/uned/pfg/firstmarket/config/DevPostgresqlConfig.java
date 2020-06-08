package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.model.Payment;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.nio.file.Paths;

@Profile("dev-postgresql") //comment @Lob on image.data
@Configuration
public class DevPostgresqlConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());


    @Bean
    public CommandLineRunner dataLoader(CatServer catServer,
                                        ImageServer imageServer,
                                        OrderServer orderServer,
                                        UserServer userServer,
                                        BookServer bookServer) {

        return args -> {
            LOGGER.debug("CommandLineRunner on dev-postgresql: start");

            //set image data
            imageServer.setImageData(imageServer.getDefaultImage().getId(), Paths.get("src/main/resources/static/images/logo.png"));
            imageServer.setImageData(1L, Paths.get("src/main/resources/static/images/1.png"));
            imageServer.setImageData(2L, Paths.get("src/main/resources/static/images/2.png"));
            imageServer.setImageData(3L, Paths.get("src/main/resources/static/images/3.png"));
            imageServer.setImageData(4L, Paths.get("src/main/resources/static/images/4.png"));
            imageServer.setImageData(5L, Paths.get("src/main/resources/static/images/5.png"));
            imageServer.setImageData(6L, Paths.get("src/main/resources/static/images/6.png"));
            imageServer.setImageData(7L, Paths.get("src/main/resources/static/images/7.png"));
            imageServer.setImageData(8L, Paths.get("src/main/resources/static/images/8.png"));
            imageServer.setImageData(9L, Paths.get("src/main/resources/static/images/9.png"));
            imageServer.setImageData(10L, Paths.get("src/main/resources/static/images/10.png"));
            imageServer.setImageData(11L, Paths.get("src/main/resources/static/images/amarillo.jpg"));
            imageServer.setImageData(12L, Paths.get("src/main/resources/static/images/azul.jpg"));
            imageServer.setImageData(13L, Paths.get("src/main/resources/static/images/marron.jpg"));
            imageServer.setImageData(14L, Paths.get("src/main/resources/static/images/negro.jpg"));
            imageServer.setImageData(15L, Paths.get("src/main/resources/static/images/reinaRoja.jpg"));
            imageServer.setImageData(16L, Paths.get("src/main/resources/static/images/rojo.jpg"));
            LOGGER.debug("CommandLineRunner on dev-postgresql: images loaded");

            //load categories
            catServer.loadCategories();
            LOGGER.debug("CommandLineRunner on dev-postgresql: categories loaded");

            //set payments amount
            orderServer.findAll().forEach(order -> {
                Payment payment = order.getPayment();
                payment.setAmount(order
                        .getItems()
                        .stream()
                        .mapToLong(item -> item.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                        .reduce(0, Long::sum)
                );
                orderServer.persistPayment(payment);
            });
            LOGGER.debug("CommandLineRunner on dev-postgresql: payments amount set");

            //load book usage
            bookServer.incrementCartBookRegistry(userServer.getAllCartBookIds());
            LOGGER.debug("CommandLineRunner on dev-postgresql: CartBookRegistry loaded: {}", bookServer.getCartBookRegistry());
        };
    }
}
