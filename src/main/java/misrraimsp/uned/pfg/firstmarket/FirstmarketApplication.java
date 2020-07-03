package misrraimsp.uned.pfg.firstmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FirstmarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(FirstmarketApplication.class, args);
    }

}
