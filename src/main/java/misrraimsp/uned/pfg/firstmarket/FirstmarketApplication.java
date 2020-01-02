package misrraimsp.uned.pfg.firstmarket;

import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.data.CategoryRepository;
import misrraimsp.uned.pfg.firstmarket.model.Book;
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
    public CommandLineRunner dataLoader(BookRepository bookRepository, CategoryRepository categoryRepository) {
        Book book1 = new Book();
        book1.setIsbn("isbn001");
        book1.setTitle("Libro-1");

        Book book2 = new Book();
        book2.setIsbn("isbn002");
        book2.setTitle("Libro-2");

        Category cat1 = new Category();
        cat1.setName("cat1");

        Category cat2 = new Category();
        cat2.setName("cat2");

        return args -> {
            bookRepository.save(book1);
            bookRepository.save(book2);

            categoryRepository.save(cat1);
            categoryRepository.save(cat2);
        };
    }

}
