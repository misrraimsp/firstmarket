package misrraimsp.uned.pfg.firstmarket;

import misrraimsp.uned.pfg.firstmarket.model.*;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class FirstmarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstmarketApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(BookServer bookServer,
                                        CatServer catServer,
                                        RoleServer roleServer,
                                        UserServer userServer,
                                        PasswordEncoder passwordEncoder,
                                        ImageServer imageServer,
                                        ItemServer itemServer,
                                        CartServer cartServer) {

        return args -> {

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
            Path path2 = Paths.get("img/azul.jpg");
            img2.setDefault(false);
            img2.setData(Files.readAllBytes(path2));
            img2.setName(path2.getFileName().toString());
            img2.setMimeType(Files.probeContentType(path2));
            imageServer.persist(img2);

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

            //Books

            Book book1 = new Book();
            book1.setIsbn("isbn001");
            book1.setTitle("Computer Basics");
            book1.setCategory(computers);
            book1.setImage(img1);
            bookServer.persist(book1);

            Book book2 = new Book();
            book2.setIsbn("isbn002");
            book2.setTitle("Traditional Music of Spain");
            book2.setCategory(music);
            book2.setImage(img2);
            bookServer.persist(book2);

            Book book3 = new Book();
            book3.setIsbn("isbn003");
            book3.setTitle("Compilers");
            book3.setCategory(computers);
            book3.setImage(img1);
            bookServer.persist(book3);

            Book book4 = new Book();
            book4.setIsbn("isbn004");
            book4.setTitle("Computer Networking");
            book4.setCategory(computers);
            book4.setImage(img1);
            bookServer.persist(book4);

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

            User user1 = new User();
            user1.setEmail("amail");
            user1.setPassword("admin");
            user1.setFirstName("adminName");
            user1.setLastName("adminAp");
            userServer.persist(user1, passwordEncoder, Arrays.asList(role1), cart1);

            User user2 = new User();
            user2.setEmail("mmail");
            user2.setPassword("misrra");
            user2.setFirstName("Misrra");
            user2.setLastName("ApellidoMisrra");
            userServer.persist(user2, passwordEncoder);

            User user3 = new User();
            user3.setEmail("cmail");
            user3.setPassword("andrea");
            user3.setFirstName("Andrea");
            user3.setLastName("ApellidoAndrea");
            userServer.persist(user3, passwordEncoder, Arrays.asList(role2), cart3);

        };
    }

}
