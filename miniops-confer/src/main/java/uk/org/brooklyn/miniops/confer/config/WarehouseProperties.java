package uk.org.brooklyn.miniops.confer.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@ConfigurationProperties(prefix = "warehouse")
@Configuration
@Data
public class WarehouseProperties {

    private String url;
}
