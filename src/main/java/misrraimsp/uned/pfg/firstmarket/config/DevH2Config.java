package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Gender;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Language;
import misrraimsp.uned.pfg.firstmarket.core.data.OrderRepository;
import misrraimsp.uned.pfg.firstmarket.core.data.ProfileRepository;
import misrraimsp.uned.pfg.firstmarket.core.model.*;
import misrraimsp.uned.pfg.firstmarket.core.service.*;
import misrraimsp.uned.pfg.firstmarket.util.adt.dto.BookForm;
import misrraimsp.uned.pfg.firstmarket.util.adt.dto.UserForm;
import misrraimsp.uned.pfg.firstmarket.util.converter.ConversionManager;
import misrraimsp.uned.pfg.firstmarket.util.exception.NoRootCategoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Year;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Profile("dev-h2") //un-comment @Lob on image.data
@Configuration
public class DevH2Config {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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
                                        PublisherServer publisherServer,
                                        OrderRepository orderRepository,
                                        ProfileRepository profileRepository,
                                        ConversionManager conversionManager) {

        return args -> {
            LOGGER.warn("CommandLineRunner on dev-h2");

            //Images

            Image img0 = new Image();
            Path path0 = Paths.get("/home/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/src/main/resources/static/images/logo.jpg");
            img0.setDefault(true);
            img0.setData(Files.readAllBytes(path0));
            img0.setName(path0.getFileName().toString());
            img0.setMimeType(Files.probeContentType(path0));
            imageServer.persist(img0);

            Image img1 = new Image();
            Path path1 = Paths.get("/home/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/src/main/resources/static/images/momo.jpg");
            img1.setDefault(false);
            img1.setData(Files.readAllBytes(path1));
            img1.setName(path1.getFileName().toString());
            img1.setMimeType(Files.probeContentType(path1));
            imageServer.persist(img1);

            Image img2 = new Image();
            Path path2 = Paths.get("/home/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/src/main/resources/static/images/haskell.png");
            img2.setDefault(false);
            img2.setData(Files.readAllBytes(path2));
            img2.setName(path2.getFileName().toString());
            img2.setMimeType(Files.probeContentType(path2));
            imageServer.persist(img2);

            Image img3 = new Image();
            Path path3 = Paths.get("/home/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/src/main/resources/static/images/istanbul.jpg");
            img3.setDefault(false);
            img3.setData(Files.readAllBytes(path3));
            img3.setName(path3.getFileName().toString());
            img3.setMimeType(Files.probeContentType(path3));
            imageServer.persist(img3);

            Image img4 = new Image();
            Path path4 = Paths.get("/home/andreagrau/Desktop/EmbajadaMisrra/pfg/firstmarket/src/main/resources/static/images/java.jpg");
            img4.setDefault(false);
            img4.setData(Files.readAllBytes(path4));
            img4.setName(path4.getFileName().toString());
            img4.setMimeType(Files.probeContentType(path4));
            imageServer.persist(img4);

            /*
            //load defaultImageId

            imageServer.loadDefaultImageId();

             */

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

            try {
                catServer.loadCategories();
            }
            catch (NoRootCategoryException e) {
                LOGGER.error("Root category not found", e);
                return;
            }

            /*Authors

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

             */

            /*Publishers

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

             */

            //FormBooks
            //System.out.println("start persisting FormBook's");

            BookForm bookForm1 = new BookForm();
            bookForm1.setIsbn("3-04-013341-1");
            bookForm1.setTitle("Computer Basics");
            bookForm1.setCategoryId(computers.getId());
            bookForm1.setImage(img1);
            bookForm1.setStoredImageId(null);
            bookForm1.setAuthorsFirstName(List.of("Julio", "", "Julio"));
            bookForm1.setAuthorsLastName(List.of("Verne", "", "Verne"));
            bookForm1.setPublisherName("Anaya");
            bookForm1.setLanguage(Language.SPANISH);
            bookForm1.setPages(123);
            bookForm1.setDescription("Este es un libro que trata acerca de los fundamentos " +
                    "en los que se basa las construcción de ordenadores modernos");
            bookForm1.setPrice("19.99");
            bookForm1.setStock(5);
            bookForm1.setYear(Year.of(2020));
            //System.out.println("formBook1: " + formBook1);
            Book book1 = conversionManager.convertBookFormToBook(bookForm1);
            book1 = bookServer.persist(book1);
            //System.out.println(book1);


            BookForm bookForm2 = new BookForm();
            bookForm2.setIsbn("ISBN-13: 978-0-596-52068-7");
            bookForm2.setTitle("Traditional Music of Spain");
            bookForm2.setCategoryId(music.getId());
            bookForm2.setImage(img2);
            bookForm2.setStoredImageId(null);
            bookForm2.setAuthorsFirstName(List.of("Julio", "Miguel"));
            bookForm2.setAuthorsLastName(List.of("Verne", "Delibes"));
            bookForm2.setPublisherName("Manning");
            bookForm2.setLanguage(Language.ENGLISH);
            bookForm2.setPages(100);
            bookForm2.setDescription("En este libro se recogen las principales " +
                    "características de la música tradicional española");
            bookForm2.setPrice("59.89");
            bookForm2.setStock(5);
            bookForm2.setYear(Year.of(1990));
            //System.out.println("formBook2: " + formBook2);
            Book book2 = conversionManager.convertBookFormToBook(bookForm2);
            book2 = bookServer.persist(book2);
            //System.out.println(book2);


            BookForm bookForm3 = new BookForm();
            bookForm3.setIsbn("ISBN-10 0-596-52068-9");
            bookForm3.setTitle("Compilers");
            bookForm3.setCategoryId(computers.getId());
            bookForm3.setImage(img3);
            bookForm3.setStoredImageId(null);
            bookForm3.setAuthorsFirstName(List.of("Miguel", "Pablo", "Julio"));
            bookForm3.setAuthorsLastName(List.of("Delibes", "Iglesias", "Verne"));
            bookForm3.setPublisherName("Cabildo de Gran Canaria");
            bookForm3.setLanguage(Language.GERMAN);
            bookForm3.setPages(422);
            bookForm3.setDescription("Recorrido por los principales aspectos del análisis, " +
                    "diseño y construcción de compiladores modernos");
            bookForm3.setPrice("0.5");
            bookForm3.setStock(5);
            bookForm3.setYear(Year.of(1998));
            //System.out.println("formBook3: " + formBook3);
            Book book3 = conversionManager.convertBookFormToBook(bookForm3);
            book3 = bookServer.persist(book3);
            //System.out.println(book3);

            BookForm bookForm4 = new BookForm();
            bookForm4.setIsbn("043942089X");
            bookForm4.setTitle("Computer Networking");
            bookForm4.setCategoryId(computers.getId());
            bookForm4.setImage(img4);
            bookForm4.setStoredImageId(null);
            bookForm4.setAuthorsFirstName(List.of("", "", "", "", "Antonio"));
            bookForm4.setAuthorsLastName(List.of("", "", "", "", "Escohotado"));
            bookForm4.setPublisherName("Planeta");
            bookForm4.setLanguage(Language.FRENCH);
            bookForm4.setPages(111);
            bookForm4.setDescription("Breve comentario sobre los principales " +
                    "temas relacionados con la conectividad de computadores");
            bookForm4.setPrice("0.51");
            bookForm4.setStock(5);
            bookForm4.setYear(Year.of(2007));
            //System.out.println("formBook4: " + formBook4);
            Book book4 = conversionManager.convertBookFormToBook(bookForm4);
            book4 = bookServer.persist(book4);
            //System.out.println(book4);


            BookForm bookForm5 = new BookForm();
            bookForm5.setIsbn("9788417761967");
            bookForm5.setTitle("EL VAGON DE LAS MUJERES");
            bookForm5.setCategoryId(art.getId());
            bookForm5.setImage(null);
            bookForm5.setStoredImageId(img1.getId());
            bookForm5.setAuthorsFirstName(List.of("Julio", "", "Julio"));
            bookForm5.setAuthorsLastName(List.of("Verne", "", "Cesar"));
            bookForm5.setPublisherName("Cambridge");
            bookForm5.setLanguage(Language.PORTUGUESE);
            bookForm5.setPages(128);
            bookForm5.setDescription("");
            bookForm5.setPrice("19.99");
            bookForm5.setStock(5);
            bookForm5.setYear(Year.of(2014));
            //System.out.println("formBook5: " + formBook5);
            Book book5 = conversionManager.convertBookFormToBook(bookForm5);
            book5 = bookServer.persist(book5);
            //System.out.println(book5);


            BookForm bookForm6 = new BookForm();
            bookForm6.setIsbn("ISBN-13: 9788498385472");
            bookForm6.setTitle("COMETAS EN EL CIELO");
            bookForm6.setCategoryId(tech.getId());
            bookForm6.setImage(null);
            bookForm6.setStoredImageId(img3.getId());
            bookForm6.setAuthorsFirstName(List.of(""));
            bookForm6.setAuthorsLastName(List.of(""));
            bookForm6.setPublisherName("Uned");
            bookForm6.setLanguage(Language.ESTONIAN);
            bookForm6.setPages(123);
            bookForm6.setDescription("");
            bookForm6.setPrice("55");
            bookForm6.setStock(0);
            bookForm6.setYear(Year.of(1987));
            //System.out.println("formBook6: " + formBook6);
            Book book6 = conversionManager.convertBookFormToBook(bookForm6);
            book6 = bookServer.persist(book6);
            //System.out.println(book6);


            BookForm bookForm7 = new BookForm();
            bookForm7.setIsbn("9788420675657");
            bookForm7.setTitle("LA HIJA DE LA CRIADA");
            bookForm7.setCategoryId(computers.getId());
            bookForm7.setImage(null);
            bookForm7.setStoredImageId(img2.getId());
            bookForm7.setAuthorsFirstName(List.of("Misrraim", "Andrea"));
            bookForm7.setAuthorsLastName(List.of("Suárez", "Grau"));
            bookForm7.setPublisherName("Espasa");
            bookForm7.setLanguage(Language.CZECH);
            bookForm7.setPages(599);
            bookForm7.setDescription("");
            bookForm7.setPrice("14.99");
            bookForm7.setStock(5);
            bookForm7.setYear(Year.of(2019));
            //System.out.println("formBook7: " + formBook7);
            Book book7 = conversionManager.convertBookFormToBook(bookForm7);
            book7 = bookServer.persist(book7);
            //System.out.println(book7);


            BookForm bookForm8 = new BookForm();
            bookForm8.setIsbn("9788415532767");
            bookForm8.setTitle("LA ISLA DE LAS MARIPOSAS");
            bookForm8.setCategoryId(tech.getId());
            bookForm8.setImage(img4);
            bookForm8.setStoredImageId(null);
            bookForm8.setAuthorsFirstName(List.of("", "   ", "Cristiano"));
            bookForm8.setAuthorsLastName(List.of("Messi", "            ", "Ronaldo"));
            bookForm8.setPublisherName("Alianza");
            bookForm8.setLanguage(Language.POLISH);
            bookForm8.setPages(344);
            bookForm8.setDescription("");
            bookForm8.setPrice("99.99");
            bookForm8.setStock(0);
            bookForm8.setYear(Year.of(2002));
            //System.out.println("formBook8: " + formBook8);
            Book book8 = conversionManager.convertBookFormToBook(bookForm8);
            book8 = bookServer.persist(book8);
            //System.out.println(book8);


            BookForm bookForm9 = new BookForm();
            bookForm9.setIsbn("9788417708344");
            bookForm9.setTitle("LA HERENCIA DE AGNETA");
            bookForm9.setCategoryId(art.getId());
            bookForm9.setImage(img1);
            bookForm9.setStoredImageId(null);
            bookForm9.setAuthorsFirstName(List.of("Julio"));
            bookForm9.setAuthorsLastName(List.of("Verne"));
            bookForm9.setPublisherName("Anaya");
            bookForm9.setLanguage(Language.BASQUE);
            bookForm9.setPages(12);
            bookForm9.setDescription("Estocolmo, 1913. Agneta, la descendiente de una familia" +
                    " que se dedica desde hace varias generaciones a la cría de caballos, finalmente ha logrado su gran sueño.");
            bookForm9.setPrice("199.95");
            bookForm9.setStock(5);
            bookForm9.setYear(Year.of(2020));
            //System.out.println("formBook9: " + formBook9);
            Book book9 = conversionManager.convertBookFormToBook(bookForm9);
            book9 = bookServer.persist(book9);
            //System.out.println(book9);

            BookForm bookForm10 = new BookForm();
            bookForm10.setIsbn("9788413184449");
            bookForm10.setTitle("");
            bookForm10.setCategoryId(art.getId());
            bookForm10.setImage(img1);
            bookForm10.setStoredImageId(null);
            bookForm10.setAuthorsFirstName(List.of(""));
            bookForm10.setAuthorsLastName(List.of(""));
            bookForm10.setPublisherName("");
            bookForm10.setLanguage(Language.BASQUE);
            bookForm10.setPages(12);
            bookForm10.setDescription("Estocolmo, 1913. Agneta, la descendiente de una familia" +
                    " que se dedica desde hace varias generaciones a la cría de caballos, finalmente ha logrado su gran sueño.");
            bookForm10.setPrice("199.95");
            bookForm10.setStock(5);
            bookForm10.setYear(Year.of(2000));
            //System.out.println("formBook10: " + formBook10);
            Book book10 = conversionManager.convertBookFormToBook(bookForm10);
            book10 = bookServer.persist(book10);
            //System.out.println(book10);


            //Items

            Item item1 = new Item();
            item1.setBook(book1);
            item1.setQuantity(1);
            itemServer.persist(item1);

            Item item2 = new Item();
            item2.setBook(book2);
            item2.setQuantity(2);
            itemServer.persist(item2);

            Item item3 = new Item();
            item3.setBook(book3);
            item3.setQuantity(3);
            itemServer.persist(item3);

            Item item4 = new Item();
            item4.setBook(book2);
            item4.setQuantity(2);
            itemServer.persist(item4);

            Item item5 = new Item();
            item5.setBook(book3);
            item5.setQuantity(3);
            itemServer.persist(item5);

            //Carts

            Cart cart1 = new Cart();
            cartServer.persist(cart1);

            /*
            Cart cart2 = new Cart();
            cart2.setLastModified(LocalDateTime.now());
            cartServer.persist(cart2);
             */

            Cart cart3 = new Cart();
            cart3.setItems(Set.of(item1, item2, item3));
            cartServer.persist(cart3);

            Cart cart4 = new Cart();
            cart4.setItems(Set.of(item4, item5));
            cartServer.persist(cart4);

            //Roles

            Role role1 = new Role();
            role1.setName("ROLE_ADMIN");
            roleServer.persist(role1);

            Role role2 = new Role();
            role2.setName("ROLE_USER");
            roleServer.persist(role2);

            //Users

            UserForm userForm1 = new UserForm();
            userForm1.setEmail("admin@fm.com");
            userForm1.setPassword("admin");
            userForm1.setMatchingPassword("admin");
            User admin = userServer.persist(userForm1, passwordEncoder, Collections.singleton(role1), cart1);
            misrraimsp.uned.pfg.firstmarket.core.model.Profile adminProfile = admin.getProfile();
            adminProfile.setFirstName("adminFirstName");
            adminProfile.setLastName("adminLastName");
            adminProfile.setPhone("666 333 666");
            adminProfile.setBirthDate(LocalDate.ofYearDay(1969, 200));
            adminProfile.setGender(Gender.UNDEFINED);
            profileRepository.save(adminProfile);
            userServer.setCompletedState(admin.getId(),true);

            UserForm userForm2 = new UserForm();
            userForm2.setEmail("suarezperezmisrraim@gmail.com");
            userForm2.setPassword("misrra");
            userForm2.setMatchingPassword("misrra");
            User misrra = userServer.persist(userForm2, passwordEncoder, null, null);
            misrraimsp.uned.pfg.firstmarket.core.model.Profile misrraProfile = misrra.getProfile();
            misrraProfile.setFirstName("Misrraim");
            misrraProfile.setLastName("Suárez");
            misrraProfile.setPhone("111 222 333");
            misrraProfile.setBirthDate(LocalDate.ofYearDay(2000, 1));
            misrraProfile.setGender(Gender.MALE);
            profileRepository.save(misrraProfile);
            userServer.setCompletedState(misrra.getId(),true);

            UserForm userForm3 = new UserForm();
            userForm3.setEmail("andrea@fm.com");
            userForm3.setPassword("andrea");
            userForm3.setMatchingPassword("andrea");
            User andrea = userServer.persist(userForm3, passwordEncoder, Collections.singleton(role2), cart3);
            misrraimsp.uned.pfg.firstmarket.core.model.Profile andreaProfile = andrea.getProfile();
            andreaProfile.setFirstName("Andrea");
            andreaProfile.setLastName("Grau");
            andreaProfile.setPhone("123 456 789");
            andreaProfile.setBirthDate(LocalDate.ofYearDay(1990, 365));
            andreaProfile.setGender(Gender.FEMALE);
            profileRepository.save(andreaProfile);
            userServer.setCompletedState(andrea.getId(),true);

            UserForm userForm4 = new UserForm();
            userForm4.setEmail("eric@fm.com");
            userForm4.setPassword("eric");
            userForm4.setMatchingPassword("eric");
            User eric = userServer.persist(userForm4, passwordEncoder, Collections.singleton(role2), cart4);
            misrraimsp.uned.pfg.firstmarket.core.model.Profile ericProfile = eric.getProfile();
            ericProfile.setFirstName("Eric");
            ericProfile.setLastName("Forman");
            ericProfile.setPhone("123 456 789");
            ericProfile.setBirthDate(LocalDate.ofYearDay(1960, 111));
            ericProfile.setGender(Gender.MALE);
            profileRepository.save(ericProfile);
            userServer.setCompletedState(eric.getId(),true);

            //Orders
            /*Order order1 = new Order();
            order1.setItems(Set.of(item3));
            orderRepository.save(order1);*/

            //load book usage
            bookServer.incrementCartBookRegistry(userServer.getAllCartBookIds());
            LOGGER.debug("CommandLineRunner on dev-h2: CartBookRegistry loaded: {}", bookServer.getCartBookRegistry());

        };
    }

}
