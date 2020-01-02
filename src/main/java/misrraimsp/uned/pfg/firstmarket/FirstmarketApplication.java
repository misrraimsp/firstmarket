package misrraimsp.uned.pfg.firstmarket;

import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.data.CatPathRepository;
import misrraimsp.uned.pfg.firstmarket.data.CategoryRepository;
import misrraimsp.uned.pfg.firstmarket.model.Book;
import misrraimsp.uned.pfg.firstmarket.model.CatPath;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FirstmarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(FirstmarketApplication.class, args);
    }

    @Bean
    public CommandLineRunner dataLoader(BookRepository bookRepository, CategoryRepository categoryRepository,
                                        CatPathRepository catPathRepository) {

        return args -> {

            //Books

            Book book1 = new Book();
            book1.setIsbn("isbn001");
            book1.setTitle("Libro-1");
            bookRepository.save(book1);

            Book book2 = new Book();
            book2.setIsbn("isbn002");
            book2.setTitle("Libro-2");
            bookRepository.save(book2);

            //Categories

            Category cat0 = new Category();
            cat0.setName("firstmarket");
            categoryRepository.save(cat0);

            Category cat1 = new Category();
            cat1.setName("cat1");
            categoryRepository.save(cat1);

            Category cat2 = new Category();
            cat2.setName("cat2");
            categoryRepository.save(cat2);

            Category cat11 = new Category();
            cat11.setName("cat11");
            categoryRepository.save(cat11);

            Category cat22 = new Category();
            cat22.setName("cat22");
            categoryRepository.save(cat22);

            //CatPaths

            CatPath cp0_0 = new CatPath();
            cp0_0.setAncestor(cat0);
            cp0_0.setDescendant(cat0);
            cp0_0.setPath_length(0);
            catPathRepository.save(cp0_0);

            CatPath cp0_1 = new CatPath();
            cp0_1.setAncestor(cat0);
            cp0_1.setDescendant(cat1);
            cp0_1.setPath_length(1);
            catPathRepository.save(cp0_1);

            CatPath cp0_2 = new CatPath();
            cp0_2.setAncestor(cat0);
            cp0_2.setDescendant(cat2);
            cp0_2.setPath_length(1);
            catPathRepository.save(cp0_2);

            CatPath cp0_11 = new CatPath();
            cp0_11.setAncestor(cat0);
            cp0_11.setDescendant(cat11);
            cp0_11.setPath_length(2);
            catPathRepository.save(cp0_11);

            CatPath cp0_22 = new CatPath();
            cp0_22.setAncestor(cat0);
            cp0_22.setDescendant(cat22);
            cp0_22.setPath_length(2);
            catPathRepository.save(cp0_22);

            CatPath cp1_1 = new CatPath();
            cp1_1.setAncestor(cat1);
            cp1_1.setDescendant(cat1);
            cp1_1.setPath_length(0);
            catPathRepository.save(cp1_1);

            CatPath cp1_11 = new CatPath();
            cp1_11.setAncestor(cat1);
            cp1_11.setDescendant(cat11);
            cp1_11.setPath_length(1);
            catPathRepository.save(cp1_11);

            CatPath cp2_2 = new CatPath();
            cp2_2.setAncestor(cat2);
            cp2_2.setDescendant(cat2);
            cp2_2.setPath_length(0);
            catPathRepository.save(cp2_2);

            CatPath cp2_22 = new CatPath();
            cp2_22.setAncestor(cat2);
            cp2_22.setDescendant(cat22);
            cp2_22.setPath_length(1);
            catPathRepository.save(cp2_22);

            CatPath cp11_11 = new CatPath();
            cp11_11.setAncestor(cat11);
            cp11_11.setDescendant(cat11);
            cp11_11.setPath_length(0);
            catPathRepository.save(cp11_11);

            CatPath cp22_22 = new CatPath();
            cp22_22.setAncestor(cat22);
            cp22_22.setDescendant(cat22);
            cp22_22.setPath_length(0);
            catPathRepository.save(cp22_22);
        };
    }

}
