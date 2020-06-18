package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
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
import java.util.HashMap;
import java.util.Map;

@Profile("dev-postgresql") //comment @Lob on image.data
@Configuration
public class DevPostgresqlConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
            "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
            "when an unknown printer took a galley of type and scrambled it to make a type specimen book. " +
            "It has survived not only five centuries, but also the leap into electronic typesetting, " +
            "remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, " +
            "and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";


    @Bean
    public CommandLineRunner dataLoader(CatServer catServer,
                                        ImageServer imageServer,
                                        OrderServer orderServer,
                                        UserServer userServer,
                                        BookServer bookServer,
                                        BookRepository bookRepository) {

        return args -> {
            LOGGER.debug("CommandLineRunner on dev-postgresql: started");

            //detect database reset
            if (imageServer.getDefaultImage().getSize() == 0) {
            //if (true) {
                //set image data
                imageServer.setImageData(imageServer.getDefaultImage().getId(), Paths.get("src/main/resources/static/images/logo.jpg"));
                imageServer.setImageData(1L, Paths.get("src/main/resources/static/images/1.png"));
                imageServer.setImageData(2L, Paths.get("src/main/resources/static/images/2.png"));
                imageServer.setImageData(3L, Paths.get("src/main/resources/static/images/3.png"));
                imageServer.setImageData(4L, Paths.get("src/main/resources/static/images/4.png"));
                imageServer.setImageData(5L, Paths.get("src/main/resources/static/images/5.png"));
                imageServer.setImageData(6L, Paths.get("src/main/resources/static/images/6.png"));
                imageServer.setImageData(7L, Paths.get("src/main/resources/static/images/7.png"));
                imageServer.setImageData(8L, Paths.get("src/main/resources/static/images/8.png"));
                imageServer.setImageData(9L, Paths.get("src/main/resources/static/images/9.png"));
                imageServer.setImageData(10L, Paths.get("src/main/resources/static/images/negro.jpg"));
                imageServer.setImageData(11L, Paths.get("src/main/resources/static/images/reinaRoja.jpg"));
                imageServer.setImageData(12L, Paths.get("src/main/resources/static/images/compilers.jpeg"));
                imageServer.setImageData(13L, Paths.get("src/main/resources/static/images/computer_networking.jpg"));
                imageServer.setImageData(14L, Paths.get("src/main/resources/static/images/momo.jpg"));
                imageServer.setImageData(15L, Paths.get("src/main/resources/static/images/unendliche.jpg"));
                imageServer.setImageData(16L, Paths.get("src/main/resources/static/images/ninguno.jpg"));
                LOGGER.debug("CommandLineRunner on dev-postgresql: images loaded");

                //load book titles
                Map<Long,String> titles = new HashMap<>();
                titles.put(1L,"MILES DAVIS FOR SOLO GUITAR");
                titles.put(2L,"Leonardos's Notebooks - Writing and Art of the Great Master");
                titles.put(3L,"DR. SPOCK'S BABY AND CHILD CARE");
                titles.put(4L,"ART/WORK - Everything You Need to Know (and Do) As You Pursue Your Art Career");
                titles.put(5L,"THE FIRST-TIME MANAGER");
                titles.put(6L,"HOW TO WIN FRIENDS & INFLUENCE PEOPLE");
                titles.put(7L,"PRACTICAL ELECTRONICS FOR INVENTORS");
                titles.put(8L,"UGLY'S ELECTRICAL REFERENCES");
                titles.put(9L,"Java - A Beginner's Guide");
                titles.put(10L,"The Clockmaker's Daughter");
                titles.put(11L,"REINA ROJA");
                titles.put(12L,"Compilers - Principles, Techniques & Tools");
                titles.put(13L,"Computer Networking - A Top-Down Approach");
                titles.put(14L,"Momo");
                titles.put(15L,"DIE UNENDLICHE GESCHICHTE");
                titles.put(16L,"Historias de Ninguno");
                bookRepository.findAll().forEach(book -> {
                    if (!book.getImage().isDefault()) {
                        book.setTitle(titles.get(book.getImage().getId()));
                    }
                    book.setDescription(description);
                    bookRepository.save(book);
                });
                LOGGER.debug("CommandLineRunner on dev-postgresql: book titles and description loaded");

                //set payments amount
                orderServer.findAll().forEach(order -> {
                    Payment payment = order.getPayment();
                    payment.setAmount(order
                            .getSales()
                            .stream()
                            .mapToLong(sale -> sale.getCompoundPrice().multiply(BigDecimal.valueOf(100)).longValue())
                            .reduce(0, Long::sum)
                    );
                    orderServer.persistPayment(payment);
                });
                LOGGER.debug("CommandLineRunner on dev-postgresql: payments amount set");
            }

            //load categories
            catServer.loadCategories();
            LOGGER.debug("CommandLineRunner on dev-postgresql: categories loaded");

            //load book usage
            bookServer.incrementCartBookRegistry(userServer.getAllCartBookIds());
            LOGGER.debug("CommandLineRunner on dev-postgresql: CartBookRegistry loaded");
            LOGGER.trace("CartBookRegistry: {}", bookServer.getCartBookRegistry());

            LOGGER.debug("CommandLineRunner on dev-postgresql: ended");
        };
    }
}
