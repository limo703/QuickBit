package quickbit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PetProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetProjectApplication.class, args);
    }

}
