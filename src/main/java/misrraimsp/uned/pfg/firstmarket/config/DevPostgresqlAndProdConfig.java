package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.core.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.core.model.Payment;
import misrraimsp.uned.pfg.firstmarket.core.service.*;
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

@Profile({"dev-postgresql", "prod"}) //comment @Lob on image.data
@Configuration
public class DevPostgresqlAndProdConfig {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
            "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
            "when an unknown printer took a galley of type and scrambled it to make a type specimen book. " +
            "It has survived not only five centuries, but also the leap into electronic typesetting, " +
            "remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, " +
            "and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

    private static final String imgAddress = "src/main/resources/static/images/";

    @Bean
    public CommandLineRunner dataLoader(CatServer catServer,
                                        ImageServer imageServer,
                                        OrderServer orderServer,
                                        UserServer userServer,
                                        BookServer bookServer,
                                        BookRepository bookRepository) {

        return args -> {
            LOGGER.debug("CommandLineRunner (postgresql): started");

            //detect database reset
            if (imageServer.getDefaultImage().getSize() == 0) {
            //if (true) {
                //set image data
                imageServer.setImageData(imageServer.getDefaultImage().getId(), Paths.get(imgAddress + "logo.jpg"));
                imageServer.setImageData(1L, Paths.get(imgAddress + "aformuladeus.jpg"));
                imageServer.setImageData(2L, Paths.get(imgAddress + "alicewonder.png"));
                imageServer.setImageData(3L, Paths.get(imgAddress + "alquimista.jpg"));
                imageServer.setImageData(4L, Paths.get(imgAddress + "automata.jpg"));
                imageServer.setImageData(5L, Paths.get(imgAddress + "calculo.jpg"));
                imageServer.setImageData(6L, Paths.get(imgAddress + "cenicienta.jpg"));
                imageServer.setImageData(7L, Paths.get(imgAddress + "centrotierra.jpg"));
                imageServer.setImageData(8L, Paths.get(imgAddress + "chocolate.jpg"));
                imageServer.setImageData(9L, Paths.get(imgAddress + "crime.jpg"));
                imageServer.setImageData(10L, Paths.get(imgAddress + "crime2.jpg"));
                imageServer.setImageData(11L, Paths.get(imgAddress + "reinaroja.jpg"));
                imageServer.setImageData(12L, Paths.get(imgAddress + "compilers.jpeg"));
                imageServer.setImageData(13L, Paths.get(imgAddress + "cnetworking.jpg"));
                imageServer.setImageData(14L, Paths.get(imgAddress + "momo.jpg"));
                imageServer.setImageData(15L, Paths.get(imgAddress + "unendliche.jpg"));
                imageServer.setImageData(16L, Paths.get(imgAddress + "ninguno.jpg"));
                imageServer.setImageData(17L, Paths.get(imgAddress + "drspock.png"));
                imageServer.setImageData(18L, Paths.get(imgAddress + "fluidos.jpeg"));
                imageServer.setImageData(19L, Paths.get(imgAddress + "haskell.png"));
                imageServer.setImageData(20L, Paths.get(imgAddress + "istanbul.jpg"));
                imageServer.setImageData(21L, Paths.get(imgAddress + "java.jpg"));
                imageServer.setImageData(22L, Paths.get(imgAddress + "ppan.jpg"));
                imageServer.setImageData(23L, Paths.get(imgAddress + "propaganda.jpg"));
                imageServer.setImageData(24L, Paths.get(imgAddress + "raices.jpg"));
                imageServer.setImageData(25L, Paths.get(imgAddress + "revoluciones.jpg"));
                imageServer.setImageData(26L, Paths.get(imgAddress + "selva.jpg"));
                imageServer.setImageData(27L, Paths.get(imgAddress + "sombraviento.jpg"));
                imageServer.setImageData(28L, Paths.get(imgAddress + "spring.png"));
                imageServer.setImageData(29L, Paths.get(imgAddress + "walden.jpg"));
                LOGGER.debug("CommandLineRunner (postgresql): images loaded");

                //load book titles
                Map<Long,String> titles = new HashMap<>();
                titles.put(1L,"a FÓRMULA de DEUS");
                titles.put(2L,"ALICE'S ADVENTURES IN WONDERLAND");
                titles.put(3L,"O ALQUIMISTA");
                titles.put(4L,"Automata Theory, Languages, and Computation");
                titles.put(5L,"Cálculo Vectorial");
                titles.put(6L,"Cinderella");
                titles.put(7L,"Viagem ao Centro da Terra");
                titles.put(8L,"Como Água para Chocolate");
                titles.put(9L,"CRIME AND PUNISHMENT");
                titles.put(10L,"CRIME AND PUNISHMENT");
                titles.put(11L,"REINA ROJA");
                titles.put(12L,"Compilers - Principles, Techniques & Tools");
                titles.put(13L,"Computer Networking - A Top-Down Approach");
                titles.put(14L,"Momo");
                titles.put(15L,"DIE UNENDLICHE GESCHICHTE");
                titles.put(16L,"Historias de Ninguno");
                titles.put(17L,"DR. SPOCK'S BABY AND CHILD CARE");
                titles.put(18L,"mecánica de fluidos");
                titles.put(19L,"Haskell Programming from first principles");
                titles.put(20L,"Istanbul - Memories and the City");
                titles.put(21L,"Java - A Beginner's Guide");
                titles.put(22L,"Peter Pan");
                titles.put(23L,"Propaganda");
                titles.put(24L,"RAICES");
                titles.put(25L,"REVOLUCIONES IMAGINARIAS - LOS CAMBIOS POLÍTICOS EN LA ESPAÑA CONTEMPORÁNEA");
                titles.put(26L,"El LIBRO de la SELVA");
                titles.put(27L,"LA SOMBRA DEL VIENTO");
                titles.put(28L,"Spring IN ACTION");
                titles.put(29L,"Walden");

                bookRepository.findAll().forEach(book -> {
                    if (!book.getImage().isDefault()) {
                        book.setTitle(titles.get(book.getImage().getId()));
                    }
                    book.setDescription(description);
                    bookRepository.save(book);
                });
                LOGGER.debug("CommandLineRunner (postgresql): book titles and description loaded");

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
                LOGGER.debug("CommandLineRunner (postgresql): payments amount set");
            }

            //load categories
            catServer.loadCategories();
            LOGGER.debug("CommandLineRunner (postgresql): categories loaded");

            //load book usage
            bookServer.incrementCartBookRegistry(userServer.getAllCartBookIds());
            LOGGER.debug("CommandLineRunner (postgresql): CartBookRegistry loaded");
            LOGGER.trace("CartBookRegistry: {}", bookServer.getCartBookRegistry());

            LOGGER.debug("CommandLineRunner (postgresql): ended");
        };
    }
}
