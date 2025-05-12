package uk.org.brooklyn.miniops.warehouse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @author ImBrooklyn
 * @since 06/05/2025
 */
@SpringBootTest
public class KafkaTest {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void test() {
        kafkaTemplate.send("greeting", "hello");
    }
}
