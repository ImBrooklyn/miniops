package uk.org.brooklyn.miniops.confer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.*;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import uk.org.brooklyn.miniops.common.exception.RemoteXxxException;
import uk.org.brooklyn.miniops.confer.client.WarehouseClient;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Configuration
public class WarehouseConfig {
    @Bean
    WarehouseClient warehouseClient(WarehouseProperties properties) {

        WebClient webClient = WebClient.builder()
                .baseUrl(properties.getUrl())
                .defaultStatusHandler(
                        status -> status == HttpStatus.BAD_REQUEST,
                        response -> response.bodyToMono(String.class)
                                .map(RemoteXxxException::new)
                )
                .build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        return httpServiceProxyFactory.createClient(WarehouseClient.class);
    }
}
