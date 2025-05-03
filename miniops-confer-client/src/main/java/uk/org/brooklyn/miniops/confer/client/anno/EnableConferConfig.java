package uk.org.brooklyn.miniops.confer.client.anno;

import org.springframework.context.annotation.Import;
import uk.org.brooklyn.miniops.confer.client.spring.ConferConfigRegistrar;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(ConferConfigRegistrar.class)
public @interface EnableConferConfig {
}
