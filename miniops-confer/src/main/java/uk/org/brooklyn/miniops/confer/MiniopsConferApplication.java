package uk.org.brooklyn.miniops.confer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiniopsConferApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MiniopsConferApplication.class);
        application.run(args);
    }

}
