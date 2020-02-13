package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.model.*;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

@Profile("dev-h2")
@Configuration
public class DevelopmentH2Config {

    @Bean
    public CommandLineRunner dataLoader(BookServer bookServer,
                                        CatServer catServer,
                                        RoleServer roleServer,
                                        UserServer userServer,
                                        PasswordEncoder passwordEncoder,
                                        ImageServer imageServer,
                                        ItemServer itemServer,
                                        CartServer cartServer,
                                        AuthorServer authorServer,
                                        PublisherServer publisherServer) {

        return args -> {
            System.out.println("CommandLineRunner on dev-h2");

            //Images

            Image img0 = new Image();
            Path path0 = Paths.get("img/fm.png");
            img0.setDefault(true);
            img0.setData(Files.readAllBytes(path0));
            img0.setName(path0.getFileName().toString());
            img0.setMimeType(Files.probeContentType(path0));
            imageServer.persist(img0);

            Image img1 = new Image();
            Path path1 = Paths.get("img/rojo.jpg");
            img1.setDefault(false);
            img1.setData(Files.readAllBytes(path1));
            img1.setName(path1.getFileName().toString());
            img1.setMimeType(Files.probeContentType(path1));
            imageServer.persist(img1);

            Image img2 = new Image();
            Path path2 = Paths.get("img/amarillo.jpg");
            img2.setDefault(false);
            img2.setData(Files.readAllBytes(path2));
            img2.setName(path2.getFileName().toString());
            img2.setMimeType(Files.probeContentType(path2));
            imageServer.persist(img2);

            //load defaultImageId

            imageServer.loadDefaultImageId();

            //Categories

            Category fm = new Category();
            fm.setName("firstmarket");
            fm.setParent(fm); //root category: self-parenthood
            catServer.save(fm);

            Category tech = new Category();
            tech.setName("tech");
            tech.setParent(fm);
            catServer.save(tech);

            Category art = new Category();
            art.setName("art");
            art.setParent(fm);
            catServer.save(art);

            Category computers = new Category();
            computers.setName("computers");
            computers.setParent(tech);
            catServer.save(computers);

            Category music = new Category();
            music.setName("music");
            music.setParent(art);
            catServer.save(music);

            //CatPaths

            Catpath cp0_0 = new Catpath();
            cp0_0.setAncestor(fm);
            cp0_0.setDescendant(fm);
            cp0_0.setSize(0);
            catServer.save(cp0_0);

            Catpath cp0_1 = new Catpath();
            cp0_1.setAncestor(fm);
            cp0_1.setDescendant(tech);
            cp0_1.setSize(1);
            catServer.save(cp0_1);

            Catpath cp0_2 = new Catpath();
            cp0_2.setAncestor(fm);
            cp0_2.setDescendant(art);
            cp0_2.setSize(1);
            catServer.save(cp0_2);

            Catpath cp0_11 = new Catpath();
            cp0_11.setAncestor(fm);
            cp0_11.setDescendant(computers);
            cp0_11.setSize(2);
            catServer.save(cp0_11);

            Catpath cp0_22 = new Catpath();
            cp0_22.setAncestor(fm);
            cp0_22.setDescendant(music);
            cp0_22.setSize(2);
            catServer.save(cp0_22);

            Catpath cp1_1 = new Catpath();
            cp1_1.setAncestor(tech);
            cp1_1.setDescendant(tech);
            cp1_1.setSize(0);
            catServer.save(cp1_1);

            Catpath cp1_11 = new Catpath();
            cp1_11.setAncestor(tech);
            cp1_11.setDescendant(computers);
            cp1_11.setSize(1);
            catServer.save(cp1_11);

            Catpath cp2_2 = new Catpath();
            cp2_2.setAncestor(art);
            cp2_2.setDescendant(art);
            cp2_2.setSize(0);
            catServer.save(cp2_2);

            Catpath cp2_22 = new Catpath();
            cp2_22.setAncestor(art);
            cp2_22.setDescendant(music);
            cp2_22.setSize(1);
            catServer.save(cp2_22);

            Catpath cp11_11 = new Catpath();
            cp11_11.setAncestor(computers);
            cp11_11.setDescendant(computers);
            cp11_11.setSize(0);
            catServer.save(cp11_11);

            Catpath cp22_22 = new Catpath();
            cp22_22.setAncestor(music);
            cp22_22.setDescendant(music);
            cp22_22.setSize(0);
            catServer.save(cp22_22);

            //load categories

            catServer.loadCategories();

            //Authors

            Author author1 = new Author();
            author1.setFirstName("Julio");
            author1.setLastName("Verne");
            authorServer.persist(author1);

            Author author2 = new Author();
            author2.setFirstName("Miguel");
            author2.setLastName("Delibes");
            authorServer.persist(author2);

            Author author3 = new Author();
            author3.setFirstName("Pablo");
            author3.setLastName("Iglesias");
            authorServer.persist(author3);

            Author author4 = new Author();
            author4.setFirstName("Antonio");
            author4.setLastName("Escohotado");
            authorServer.persist(author4);

            //Publishers

            Publisher publisher1 = new Publisher();
            publisher1.setName("Anaya");
            publisherServer.persist(publisher1);

            Publisher publisher2 = new Publisher();
            publisher2.setName("Manning");
            publisherServer.persist(publisher2);

            Publisher publisher3 = new Publisher();
            publisher3.setName("Cabildo de Gran Canaria");
            publisherServer.persist(publisher3);

            Publisher publisher4 = new Publisher();
            publisher4.setName("Planeta");
            publisherServer.persist(publisher4);

            //Books

            Book book1 = new Book();
            book1.setIsbn("3-04-013341-1");
            book1.setTitle("Computer Basics");
            book1.setCategory(computers);
            book1.setImage(img1);
            book1.setAuthors(Arrays.asList(author1));
            book1.setPublisher(publisher1);
            book1.setLanguage(Language.ESPAÑOL);
            book1.setNumPages(123);
            book1.setSummary("Este es un libro que trata acerca de los fundamentos" +
                    "en los que se basa las construcción de ordenadores modernos");
            book1.setPrice(19.99);
            book1.setStock(100);
            bookServer.persist(book1);

            Book book2 = new Book();
            book2.setIsbn("ISBN-13: 978-0-596-52068-7");
            book2.setTitle("Traditional Music of Spain");
            book2.setCategory(music);
            book2.setImage(img2);
            book2.setAuthors(Arrays.asList(author1, author2));
            book2.setPublisher(publisher2);
            book2.setLanguage(Language.ENGLISH);
            book2.setNumPages(100);
            book2.setSummary("En este libro se recogen las principales" +
                    "características de la música tradicional española");
            book2.setPrice(59.89);
            book2.setStock(100);
            bookServer.persist(book2);

            Book book3 = new Book();
            book3.setIsbn("ISBN-10 0-596-52068-9");
            book3.setTitle("Compilers");
            book3.setCategory(computers);
            book3.setImage(img1);
            book3.setAuthors(Arrays.asList(author2, author3));
            book3.setPublisher(publisher3);
            book3.setLanguage(Language.DEUTSCH);
            book3.setNumPages(422);
            book3.setSummary("Recorrido por los principales aspectos del análisis, " +
                    "diseño y construcción de compiladores modernos");
            book3.setPrice(0);
            book3.setStock(100);
            bookServer.persist(book3);

            Book book4 = new Book();
            book4.setIsbn("043942089X");
            book4.setTitle("Computer Networking");
            book4.setCategory(computers);
            book4.setImage(img1);
            book4.setAuthors(Arrays.asList(author4));
            book4.setPublisher(publisher4);
            book4.setLanguage(Language.FRANÇAISE);
            book4.setNumPages(111);
            book4.setSummary("Breve comentario sobre los principales" +
                    "temas relacionados con la conectividad de computadores");
            book4.setPrice(129.99);
            book4.setStock(100);
            bookServer.persist(book4);

            Book book5 = new Book();
            book5.setIsbn("9788417761967");
            book5.setTitle("EL VAGON DE LAS MUJERES");
            book5.setCategory(computers);
            book5.setImage(img1);
            book5.setAuthors(Arrays.asList(author1));
            book5.setPublisher(publisher1);
            book5.setLanguage(Language.ESPAÑOL);
            book5.setNumPages(128);
            book5.setSummary("Este es un libro que trata acerca de los fundamentos" +
                    "en los que se basa las construcción de ordenadores modernos");
            book5.setPrice(19.99);
            book5.setStock(100);
            bookServer.persist(book5);

            Book book6 = new Book();
            book6.setIsbn("ISBN-13: 9788498385472");
            book6.setTitle("COMETAS EN EL CIELO");
            book6.setCategory(music);
            book6.setImage(img2);
            book6.setAuthors(Arrays.asList(author1, author2));
            book6.setPublisher(publisher2);
            book6.setLanguage(Language.ENGLISH);
            book6.setNumPages(100);
            book6.setSummary("En este libro se recogen las principales" +
                    "características de la música tradicional española");
            book6.setPrice(59.89);
            book6.setStock(100);
            bookServer.persist(book6);

            Book book7 = new Book();
            book7.setIsbn("9788420675657");
            book7.setTitle("LA HIJA DE LA CRIADA");
            book7.setCategory(computers);
            book7.setImage(img1);
            book7.setAuthors(Arrays.asList(author2, author3));
            book7.setPublisher(publisher3);
            book7.setLanguage(Language.DEUTSCH);
            book7.setNumPages(422);
            book7.setSummary("Recorrido por los principales aspectos del análisis, " +
                    "diseño y construcción de compiladores modernos");
            book7.setPrice(0);
            book7.setStock(100);
            bookServer.persist(book7);

            Book book8 = new Book();
            book8.setIsbn("9788415532767");
            book8.setTitle("LA ISLA DE LAS MARIPOSAS");
            book8.setCategory(computers);
            book8.setImage(img1);
            book8.setAuthors(Arrays.asList(author4));
            book8.setPublisher(publisher4);
            book8.setLanguage(Language.FRANÇAISE);
            book8.setNumPages(111);
            book8.setSummary("El mismo día en que descubre que su marido le es infiel, la joven abogada Diana recibe la noticia de que su adorada tía abuela Emmely está muy enferma. Sin pensárselo dos veces, Diana toma el primer vuelo a Inglaterra para despedirse de ella.");
            book8.setPrice(129.99);
            book8.setStock(100);
            bookServer.persist(book8);

            Book book9 = new Book();
            book9.setIsbn("9788417708344");
            book9.setTitle("LA HERENCIA DE AGNETA");
            book9.setCategory(computers);
            book9.setImage(img1);
            book9.setAuthors(Arrays.asList(author4));
            book9.setPublisher(publisher4);
            book9.setLanguage(Language.FRANÇAISE);
            book9.setNumPages(111);
            book9.setSummary("Estocolmo, 1913. Agneta, la descendiente de una familia que se dedica desde hace varias generaciones a la cría de caballos, finalmente ha logrado su gran sueño.");
            book9.setPrice(129.99);
            book9.setStock(100);
            bookServer.persist(book9);

            //Items

            Item item1 = new Item();
            item1.setBook(book1);
            item1.setQuantity(1);
            itemServer.persist(item1);

            Item item2 = new Item();
            item2.setBook(book4);
            item2.setQuantity(2);
            itemServer.persist(item2);

            //Carts

            Cart cart1 = new Cart();
            cart1.setLastModified(LocalDateTime.now());
            cartServer.persist(cart1);

            /*
            Cart cart2 = new Cart();
            cart2.setLastModified(LocalDateTime.now());
            cartServer.persist(cart2);
             */

            Cart cart3 = new Cart();
            cart3.setItems(Arrays.asList(item1, item2));
            cart3.setLastModified(LocalDateTime.now());
            cartServer.persist(cart3);

            //Roles

            Role role1 = new Role();
            role1.setName("ROLE_ADMIN");
            roleServer.persist(role1);

            Role role2 = new Role();
            role2.setName("ROLE_USER");
            roleServer.persist(role2);

            //Users

            FormUser formUser1 = new FormUser();
            formUser1.setEmail("admin@fm.com");
            formUser1.setPassword("admin");
            formUser1.setMatchingPassword("admin");
            formUser1.setFirstName("ad");
            formUser1.setLastName("ad");
            userServer.persist(formUser1, passwordEncoder, Arrays.asList(role1), cart1);

            FormUser formUser2 = new FormUser();
            formUser2.setEmail("misrra@fm.com");
            formUser2.setPassword("misrra");
            formUser2.setMatchingPassword("misrra");
            formUser2.setFirstName("mis");
            formUser2.setLastName("mis");
            userServer.persist(formUser2, passwordEncoder, null, null);

            FormUser formUser3 = new FormUser();
            formUser3.setEmail("andrea@fm.com");
            formUser3.setPassword("andrea");
            formUser3.setMatchingPassword("andrea");
            formUser3.setFirstName("andre");
            formUser3.setLastName("andre");
            userServer.persist(formUser3, passwordEncoder, Arrays.asList(role2), cart3);


        };
    }

}
