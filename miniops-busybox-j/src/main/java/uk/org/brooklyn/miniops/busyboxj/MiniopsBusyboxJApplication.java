package uk.org.brooklyn.miniops.busyboxj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.org.brooklyn.miniops.confer.client.anno.EnableConferConfig;

@SpringBootApplication
@EnableConferConfig
public class MiniopsBusyboxJApplication {

    public static void main(String[] args) {
        SpringApplication.run(MiniopsBusyboxJApplication.class, args);
    }

}
