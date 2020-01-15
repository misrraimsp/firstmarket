package misrraimsp.uned.pfg.firstmarket;

import misrraimsp.uned.pfg.firstmarket.data.*;
import misrraimsp.uned.pfg.firstmarket.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class FirstmarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstmarketApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(BookRepository bookRepository,
                                        CategoryRepository categoryRepository,
                                        CatPathRepository catPathRepository,
                                        RoleRepository roleRepository,
                                        UserRepository userRepository) {

        return args -> {

            //Categories

            Category fm = new Category();
            fm.setName("firstmarket");
            fm.setParent(fm); //root category: self-parenthood
            categoryRepository.save(fm);

            Category tech = new Category();
            tech.setName("tech");
            tech.setParent(fm);
            categoryRepository.save(tech);

            Category art = new Category();
            art.setName("art");
            art.setParent(fm);
            categoryRepository.save(art);

            Category computers = new Category();
            computers.setName("computers");
            computers.setParent(tech);
            categoryRepository.save(computers);

            Category music = new Category();
            music.setName("music");
            music.setParent(art);
            categoryRepository.save(music);

            //CatPaths

            CatPath cp0_0 = new CatPath();
            cp0_0.setAncestor(fm);
            cp0_0.setDescendant(fm);
            cp0_0.setPath_length(0);
            catPathRepository.save(cp0_0);

            CatPath cp0_1 = new CatPath();
            cp0_1.setAncestor(fm);
            cp0_1.setDescendant(tech);
            cp0_1.setPath_length(1);
            catPathRepository.save(cp0_1);

            CatPath cp0_2 = new CatPath();
            cp0_2.setAncestor(fm);
            cp0_2.setDescendant(art);
            cp0_2.setPath_length(1);
            catPathRepository.save(cp0_2);

            CatPath cp0_11 = new CatPath();
            cp0_11.setAncestor(fm);
            cp0_11.setDescendant(computers);
            cp0_11.setPath_length(2);
            catPathRepository.save(cp0_11);

            CatPath cp0_22 = new CatPath();
            cp0_22.setAncestor(fm);
            cp0_22.setDescendant(music);
            cp0_22.setPath_length(2);
            catPathRepository.save(cp0_22);

            CatPath cp1_1 = new CatPath();
            cp1_1.setAncestor(tech);
            cp1_1.setDescendant(tech);
            cp1_1.setPath_length(0);
            catPathRepository.save(cp1_1);

            CatPath cp1_11 = new CatPath();
            cp1_11.setAncestor(tech);
            cp1_11.setDescendant(computers);
            cp1_11.setPath_length(1);
            catPathRepository.save(cp1_11);

            CatPath cp2_2 = new CatPath();
            cp2_2.setAncestor(art);
            cp2_2.setDescendant(art);
            cp2_2.setPath_length(0);
            catPathRepository.save(cp2_2);

            CatPath cp2_22 = new CatPath();
            cp2_22.setAncestor(art);
            cp2_22.setDescendant(music);
            cp2_22.setPath_length(1);
            catPathRepository.save(cp2_22);

            CatPath cp11_11 = new CatPath();
            cp11_11.setAncestor(computers);
            cp11_11.setDescendant(computers);
            cp11_11.setPath_length(0);
            catPathRepository.save(cp11_11);

            CatPath cp22_22 = new CatPath();
            cp22_22.setAncestor(music);
            cp22_22.setDescendant(music);
            cp22_22.setPath_length(0);
            catPathRepository.save(cp22_22);

            //Books

            Book book1 = new Book();
            book1.setIsbn("isbn001");
            book1.setTitle("Computer Basics");
            book1.setCategory(computers);
            bookRepository.save(book1);

            Book book2 = new Book();
            book2.setIsbn("isbn002");
            book2.setTitle("Traditional Music of Spain");
            book2.setCategory(music);
            bookRepository.save(book2);

            //Roles

            Role role1 = new Role();
            role1.setName("ROLE_ADMIN");
            roleRepository.save(role1);

            Role role2 = new Role();
            role2.setName("ROLE_USER");
            roleRepository.save(role2);

            //Users

            User user1 = new User();
            user1.setUsername("admin");
            user1.setPassword("admin");
            user1.setFirstName("adminName");
            user1.setLastName("adminAp");
            user1.setEmail("admin@gmail.com");
            user1.setRoles(Arrays.asList(role1, role2));
            userRepository.save(user1);

            User user2 = new User();
            user2.setUsername("misrra");
            user2.setPassword("misrra");
            user2.setFirstName("Misrra");
            user2.setLastName("ApellidoMisrra");
            user2.setEmail("misrra@gmail.com");
            user2.setRoles(Arrays.asList(role2));
            userRepository.save(user2);

            User user3 = new User();
            user3.setUsername("andrea");
            user3.setPassword("andrea");
            user3.setFirstName("Andrea");
            user3.setLastName("ApellidoAndrea");
            user3.setEmail("andrea@gmail.com");
            user3.setRoles(Arrays.asList(role2));
            userRepository.save(user3);

        };
    }

}
