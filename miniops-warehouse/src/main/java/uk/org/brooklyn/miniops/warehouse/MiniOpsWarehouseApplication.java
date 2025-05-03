package uk.org.brooklyn.miniops.warehouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MiniOpsWarehouseApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MiniOpsWarehouseApplication.class);
        application.run(args);
    }

}
