package misrraimsp.uned.pfg.firstmarket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FirstmarketApplication {

    public static void main(String[] args) {
        //el contenedor de IoC se encarga de crear los objetos y
        //de resolver sus interdependencias
        SpringApplication.run(FirstmarketApplication.class, args);
    }

}
