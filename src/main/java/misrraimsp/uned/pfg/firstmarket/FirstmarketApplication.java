package misrraimsp.uned.pfg.firstmarket;

import misrraimsp.uned.pfg.firstmarket.data.BookRepository;
import misrraimsp.uned.pfg.firstmarket.model.Book;
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
    public CommandLineRunner dataLoader(BookRepository bookRepository) {
        //System.out.println("Hola Misrra");
        return args -> {
            bookRepository.save(new Book(001L, "isbn001", "Libro-1"));
            bookRepository.save(new Book(002L, "isbn002", "Libro-2"));
            bookRepository.save(new Book(003L, "isbn003", "Libro-3"));
            bookRepository.save(new Book(004L, "isbn004", "Libro-4"));
        };
    }

}
