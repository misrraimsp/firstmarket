package misrraimsp.uned.pfg.firstmarket.config;

import misrraimsp.uned.pfg.firstmarket.model.*;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormBook;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormUser;
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
import java.time.Year;
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

            Image img3 = new Image();
            Path path3 = Paths.get("img/marron.jpg");
            img3.setDefault(false);
            img3.setData(Files.readAllBytes(path3));
            img3.setName(path3.getFileName().toString());
            img3.setMimeType(Files.probeContentType(path3));
            imageServer.persist(img3);

            Image img4 = new Image();
            Path path4 = Paths.get("img/negro.jpg");
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

            catServer.loadCategories();

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

            FormBook formBook1 = new FormBook();
            formBook1.setIsbn("3-04-013341-1");
            formBook1.setTitle("Computer Basics");
            formBook1.setCategoryId(computers.getId());
            formBook1.setImage(img1);
            formBook1.setStoredImageId(null);
            formBook1.setAuthorFirstName1("Julio");
            formBook1.setAuthorLastName1("Verne");
            formBook1.setAuthorFirstName2("");
            formBook1.setAuthorLastName2("");
            formBook1.setAuthorFirstName3("Julio");
            formBook1.setAuthorLastName3("Verne");
            formBook1.setAuthorFirstName4("");
            formBook1.setAuthorLastName4("");
            formBook1.setAuthorFirstName5("");
            formBook1.setAuthorLastName5("");
            formBook1.setPublisherName("Anaya");
            formBook1.setLanguage(Languages.ESPAÑOL);
            formBook1.setNumPages(123);
            formBook1.setDescription("Este es un libro que trata acerca de los fundamentos " +
                    "en los que se basa las construcción de ordenadores modernos");
            formBook1.setPrice("19.99");
            formBook1.setStock(100);
            formBook1.setYear(Year.of(2020));
            //System.out.println("formBook1: " + formBook1);
            Book book1 = bookServer.convertFormBookToBook(formBook1);
            book1 = bookServer.persist(book1);
            //System.out.println(book1);


            FormBook formBook2 = new FormBook();
            formBook2.setIsbn("ISBN-13: 978-0-596-52068-7");
            formBook2.setTitle("Traditional Music of Spain");
            formBook2.setCategoryId(music.getId());
            formBook2.setImage(img2);
            formBook2.setStoredImageId(null);
            formBook2.setAuthorFirstName1("Julio");
            formBook2.setAuthorLastName1("Verne");
            formBook2.setAuthorFirstName2("Miguel");
            formBook2.setAuthorLastName2("Delibes");
            formBook2.setAuthorFirstName3("");
            formBook2.setAuthorLastName3("");
            formBook2.setAuthorFirstName4("");
            formBook2.setAuthorLastName4("");
            formBook2.setAuthorFirstName5("");
            formBook2.setAuthorLastName5("");
            formBook2.setPublisherName("Manning");
            formBook2.setLanguage(Languages.ENGLISH);
            formBook2.setNumPages(100);
            formBook2.setDescription("En este libro se recogen las principales " +
                    "características de la música tradicional española");
            formBook2.setPrice("59.89");
            formBook2.setStock(100);
            formBook2.setYear(Year.of(1990));
            //System.out.println("formBook2: " + formBook2);
            Book book2 = bookServer.convertFormBookToBook(formBook2);
            book2 = bookServer.persist(book2);
            //System.out.println(book2);


            FormBook formBook3 = new FormBook();
            formBook3.setIsbn("ISBN-10 0-596-52068-9");
            formBook3.setTitle("Compilers");
            formBook3.setCategoryId(computers.getId());
            formBook3.setImage(img3);
            formBook3.setStoredImageId(null);
            formBook3.setAuthorFirstName1("Miguel");
            formBook3.setAuthorLastName1("Delibes");
            formBook3.setAuthorFirstName2("Pablo");
            formBook3.setAuthorLastName2("Iglesias");
            formBook3.setAuthorFirstName3("Julio");
            formBook3.setAuthorLastName3("Verne");
            formBook3.setAuthorFirstName4("");
            formBook3.setAuthorLastName4("");
            formBook3.setAuthorFirstName5("");
            formBook3.setAuthorLastName5("");
            formBook3.setPublisherName("Cabildo de Gran Canaria");
            formBook3.setLanguage(Languages.DEUTSCH);
            formBook3.setNumPages(422);
            formBook3.setDescription("Recorrido por los principales aspectos del análisis, " +
                    "diseño y construcción de compiladores modernos");
            formBook3.setPrice("0");
            formBook3.setStock(100);
            formBook3.setYear(Year.of(1998));
            //System.out.println("formBook3: " + formBook3);
            Book book3 = bookServer.convertFormBookToBook(formBook3);
            book3 = bookServer.persist(book3);
            //System.out.println(book3);

            FormBook formBook4 = new FormBook();
            formBook4.setIsbn("043942089X");
            formBook4.setTitle("Computer Networking");
            formBook4.setCategoryId(computers.getId());
            formBook4.setImage(img4);
            formBook4.setStoredImageId(null);
            formBook4.setAuthorFirstName1("");
            formBook4.setAuthorLastName1("");
            formBook4.setAuthorFirstName2("");
            formBook4.setAuthorLastName2("");
            formBook4.setAuthorFirstName3("");
            formBook4.setAuthorLastName3("");
            formBook4.setAuthorFirstName4("");
            formBook4.setAuthorLastName4("");
            formBook4.setAuthorFirstName5("Antonio");
            formBook4.setAuthorLastName5("Escohotado");
            formBook4.setPublisherName("Planeta");
            formBook4.setLanguage(Languages.FRANÇAISE);
            formBook4.setNumPages(111);
            formBook4.setDescription("Breve comentario sobre los principales " +
                    "temas relacionados con la conectividad de computadores");
            formBook4.setPrice("0.01");
            formBook4.setStock(100);
            formBook4.setYear(Year.of(2007));
            //System.out.println("formBook4: " + formBook4);
            Book book4 = bookServer.convertFormBookToBook(formBook4);
            book4 = bookServer.persist(book4);
            //System.out.println(book4);


            FormBook formBook5 = new FormBook();
            formBook5.setIsbn("9788417761967");
            formBook5.setTitle("EL VAGON DE LAS MUJERES");
            formBook5.setCategoryId(art.getId());
            formBook5.setImage(null);
            formBook5.setStoredImageId(img1.getId());
            formBook5.setAuthorFirstName1("Julio");
            formBook5.setAuthorLastName1("Verne");
            formBook5.setAuthorFirstName2("");
            formBook5.setAuthorLastName2("");
            formBook5.setAuthorFirstName3("Julio");
            formBook5.setAuthorLastName3("Cesar");
            formBook5.setAuthorFirstName4("");
            formBook5.setAuthorLastName4("");
            formBook5.setAuthorFirstName5("");
            formBook5.setAuthorLastName5("");
            formBook5.setPublisherName("Cambridge");
            formBook5.setLanguage(Languages.PORTUGUÊS);
            formBook5.setNumPages(128);
            formBook5.setDescription("");
            formBook5.setPrice("19.99");
            formBook5.setStock(100);
            formBook5.setYear(Year.of(2014));
            //System.out.println("formBook5: " + formBook5);
            Book book5 = bookServer.convertFormBookToBook(formBook5);
            book5 = bookServer.persist(book5);
            //System.out.println(book5);


            FormBook formBook6 = new FormBook();
            formBook6.setIsbn("ISBN-13: 9788498385472");
            formBook6.setTitle("COMETAS EN EL CIELO");
            formBook6.setCategoryId(tech.getId());
            formBook6.setImage(null);
            formBook6.setStoredImageId(img3.getId());
            formBook6.setAuthorFirstName1("");
            formBook6.setAuthorLastName1("");
            formBook6.setAuthorFirstName2("");
            formBook6.setAuthorLastName2("");
            formBook6.setAuthorFirstName3("");
            formBook6.setAuthorLastName3("");
            formBook6.setAuthorFirstName4("");
            formBook6.setAuthorLastName4("");
            formBook6.setAuthorFirstName5("");
            formBook6.setAuthorLastName5("");
            formBook6.setPublisherName("Uned");
            formBook6.setLanguage(Languages.ESPAÑOL);
            formBook6.setNumPages(123);
            formBook6.setDescription("");
            formBook6.setPrice("55");
            formBook6.setStock(100);
            formBook6.setYear(Year.of(1987));
            //System.out.println("formBook6: " + formBook6);
            Book book6 = bookServer.convertFormBookToBook(formBook6);
            book6 = bookServer.persist(book6);
            //System.out.println(book6);


            FormBook formBook7 = new FormBook();
            formBook7.setIsbn("9788420675657");
            formBook7.setTitle("LA HIJA DE LA CRIADA");
            formBook7.setCategoryId(computers.getId());
            formBook7.setImage(null);
            formBook7.setStoredImageId(img2.getId());
            formBook7.setAuthorFirstName1("Misrraim");
            formBook7.setAuthorLastName1("Suarez");
            formBook7.setAuthorFirstName2("");
            formBook7.setAuthorLastName2("");
            formBook7.setAuthorFirstName3("Andrea");
            formBook7.setAuthorLastName3("Grau");
            formBook7.setAuthorFirstName4("");
            formBook7.setAuthorLastName4("");
            formBook7.setAuthorFirstName5("");
            formBook7.setAuthorLastName5("");
            formBook7.setPublisherName("Espasa");
            formBook7.setLanguage(Languages.ESPAÑOL);
            formBook7.setNumPages(599);
            formBook7.setDescription("");
            formBook7.setPrice("14.99");
            formBook7.setStock(1000);
            formBook7.setYear(Year.of(2019));
            //System.out.println("formBook7: " + formBook7);
            Book book7 = bookServer.convertFormBookToBook(formBook7);
            book7 = bookServer.persist(book7);
            //System.out.println(book7);


            FormBook formBook8 = new FormBook();
            formBook8.setIsbn("9788415532767");
            formBook8.setTitle("LA ISLA DE LAS MARIPOSAS");
            formBook8.setCategoryId(tech.getId());
            formBook8.setImage(img4);
            formBook8.setStoredImageId(null);
            formBook8.setAuthorFirstName1("");
            formBook8.setAuthorLastName1("Messi");
            formBook8.setAuthorFirstName2("   ");
            formBook8.setAuthorLastName2("            ");
            formBook8.setAuthorFirstName3("Cristiano");
            formBook8.setAuthorLastName3("Ronaldo");
            formBook8.setAuthorFirstName4("");
            formBook8.setAuthorLastName4("");
            formBook8.setAuthorFirstName5("");
            formBook8.setAuthorLastName5("");
            formBook8.setPublisherName("Alianza");
            formBook8.setLanguage(Languages.PORTUGUÊS);
            formBook8.setNumPages(344);
            formBook8.setDescription("");
            formBook8.setPrice("99.99");
            formBook8.setStock(100);
            formBook8.setYear(Year.of(2002));
            //System.out.println("formBook8: " + formBook8);
            Book book8 = bookServer.convertFormBookToBook(formBook8);
            book8 = bookServer.persist(book8);
            //System.out.println(book8);


            FormBook formBook9 = new FormBook();
            formBook9.setIsbn("9788417708344");
            formBook9.setTitle("LA HERENCIA DE AGNETA");
            formBook9.setCategoryId(art.getId());
            formBook9.setImage(img1);
            formBook9.setStoredImageId(null);
            formBook9.setAuthorFirstName1("");
            formBook9.setAuthorLastName1("");
            formBook9.setAuthorFirstName2("");
            formBook9.setAuthorLastName2("");
            formBook9.setAuthorFirstName3("Julio");
            formBook9.setAuthorLastName3("Verne");
            formBook9.setAuthorFirstName4("");
            formBook9.setAuthorLastName4("");
            formBook9.setAuthorFirstName5("");
            formBook9.setAuthorLastName5("");
            formBook9.setPublisherName("Anaya");
            formBook9.setLanguage(Languages.ESPAÑOL);
            formBook9.setNumPages(12);
            formBook9.setDescription("Estocolmo, 1913. Agneta, la descendiente de una familia" +
                    " que se dedica desde hace varias generaciones a la cría de caballos, finalmente ha logrado su gran sueño.");
            formBook9.setPrice("199.95");
            formBook9.setStock(100);
            formBook9.setYear(Year.of(2020));
            //System.out.println("formBook9: " + formBook9);
            Book book9 = bookServer.convertFormBookToBook(formBook9);
            book9 = bookServer.persist(book9);
            //System.out.println(book9);

            /*
            //Books

            Book book1 = new Book();
            book1.setIsbn("3-04-013341-1");
            book1.setTitle("Computer Basics");
            book1.setCategory(computers);
            book1.setImage(img1);
            book1.setAuthors(Arrays.asList(author1));
            book1.setPublisher(publisher1);
            book1.setLanguage(Languages.ESPAÑOL);
            book1.setNumPages(123);
            book1.setDescription("Este es un libro que trata acerca de los fundamentos " +
                    "en los que se basa las construcción de ordenadores modernos");
            book1.setPrice(BigDecimal.valueOf(19.99));
            book1.setStock(100);
            book1.setYear(2020);
            bookServer.persist(book1);

            Book book2 = new Book();
            book2.setIsbn("ISBN-13: 978-0-596-52068-7");
            book2.setTitle("Traditional Music of Spain");
            book2.setCategory(music);
            book2.setImage(img2);
            book2.setAuthors(Arrays.asList(author1, author2));
            book2.setPublisher(publisher2);
            book2.setLanguage(Languages.ENGLISH);
            book2.setNumPages(100);
            book2.setDescription("En este libro se recogen las principales " +
                    "características de la música tradicional española");
            book2.setPrice(BigDecimal.valueOf(59.89));
            book2.setStock(100);
            book2.setYear(1990);
            bookServer.persist(book2);

            Book book3 = new Book();
            book3.setIsbn("ISBN-10 0-596-52068-9");
            book3.setTitle("Compilers");
            book3.setCategory(computers);
            book3.setImage(img3);
            book3.setAuthors(Arrays.asList(author2, author3));
            book3.setPublisher(publisher3);
            book3.setLanguage(Languages.DEUTSCH);
            book3.setNumPages(422);
            book3.setDescription("Recorrido por los principales aspectos del análisis, " +
                    "diseño y construcción de compiladores modernos");
            book3.setPrice(BigDecimal.valueOf(0));
            book3.setStock(100);
            book3.setYear(1998);
            bookServer.persist(book3);

            Book book4 = new Book();
            book4.setIsbn("043942089X");
            book4.setTitle("Computer Networking");
            book4.setCategory(computers);
            book4.setImage(img4);
            book4.setAuthors(Arrays.asList(author4));
            book4.setPublisher(publisher4);
            book4.setLanguage(Languages.FRANÇAISE);
            book4.setNumPages(111);
            book4.setDescription("Breve comentario sobre los principales " +
                    "temas relacionados con la conectividad de computadores");
            book4.setPrice(BigDecimal.valueOf(0.01));
            book4.setStock(100);
            book4.setYear(2007);
            bookServer.persist(book4);

            Book book5 = new Book();
            book5.setIsbn("9788417761967");
            book5.setTitle("EL VAGON DE LAS MUJERES");
            book5.setCategory(computers);
            book5.setImage(img0);
            book5.setAuthors(Arrays.asList(author1));
            book5.setPublisher(publisher1);
            book5.setLanguage(Languages.ESPAÑOL);
            book5.setNumPages(128);
            book5.setDescription("Este es un libro que trata acerca de los fundamentos " +
                    "en los que se basa las construcción de ordenadores modernos");
            book5.setPrice(BigDecimal.valueOf(19.99));
            book5.setStock(100);
            book5.setYear(2014);
            bookServer.persist(book5);

            Book book6 = new Book();
            book6.setIsbn("ISBN-13: 9788498385472");
            book6.setTitle("COMETAS EN EL CIELO");
            book6.setCategory(music);
            book6.setImage(img2);
            book6.setAuthors(Arrays.asList(author1, author2));
            book6.setPublisher(publisher2);
            book6.setLanguage(Languages.ENGLISH);
            book6.setNumPages(100);
            book6.setDescription("En este libro se recogen las principales " +
                    "características de la música tradicional española");
            book6.setPrice(BigDecimal.valueOf(55));
            book6.setStock(100);
            book6.setYear(1987);
            bookServer.persist(book6);

            Book book7 = new Book();
            book7.setIsbn("9788420675657");
            book7.setTitle("LA HIJA DE LA CRIADA");
            book7.setCategory(computers);
            book7.setImage(img1);
            book7.setAuthors(Arrays.asList(author2, author3));
            book7.setPublisher(publisher3);
            book7.setLanguage(Languages.DEUTSCH);
            book7.setNumPages(422);
            book7.setDescription("Recorrido por los principales aspectos del análisis, " +
                    "diseño y construcción de compiladores modernos");
            book7.setPrice(BigDecimal.valueOf(100.05));
            book7.setStock(100);
            book7.setYear(2019);
            bookServer.persist(book7);

            Book book8 = new Book();
            book8.setIsbn("9788415532767");
            book8.setTitle("LA ISLA DE LAS MARIPOSAS");
            book8.setCategory(computers);
            book8.setImage(img1);
            book8.setAuthors(Arrays.asList(author4));
            book8.setPublisher(publisher4);
            book8.setLanguage(Languages.FRANÇAISE);
            book8.setNumPages(111);
            book8.setDescription("El mismo día en que descubre que su marido le es infiel, la joven abogada Diana recibe la noticia de que su adorada tía abuela Emmely está muy enferma. Sin pensárselo dos veces, Diana toma el primer vuelo a Inglaterra para despedirse de ella.");
            book8.setPrice(BigDecimal.valueOf(99.99));;
            book8.setStock(100);
            book8.setYear(2002);
            bookServer.persist(book8);

            Book book9 = new Book();
            book9.setIsbn("9788417708344");
            book9.setTitle("LA HERENCIA DE AGNETA");
            book9.setCategory(computers);
            book9.setImage(img1);
            book9.setAuthors(Arrays.asList(author4));
            book9.setPublisher(publisher4);
            book9.setLanguage(Languages.FRANÇAISE);
            book9.setNumPages(111);
            book9.setDescription("Estocolmo, 1913. Agneta, la descendiente de una familia que se dedica desde hace varias generaciones a la cría de caballos, finalmente ha logrado su gran sueño.");
            book9.setPrice(BigDecimal.valueOf(14.95));
            book9.setStock(100);
            book9.setYear(2020);
            bookServer.persist(book9);

             */

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
            formUser3.setFirstName("Andrea");
            formUser3.setLastName("andre");
            userServer.persist(formUser3, passwordEncoder, Arrays.asList(role2), cart3);


        };
    }

}
