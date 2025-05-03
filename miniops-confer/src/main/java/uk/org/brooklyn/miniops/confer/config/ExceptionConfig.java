package uk.org.brooklyn.miniops.confer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import uk.org.brooklyn.miniops.common.aop.GlobalExceptionHandler;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Configuration
@Import({
        GlobalExceptionHandler.class,
})
public class ExceptionConfig {
}
