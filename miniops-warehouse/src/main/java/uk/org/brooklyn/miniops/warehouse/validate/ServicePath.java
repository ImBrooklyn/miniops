package uk.org.brooklyn.miniops.warehouse.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Documented
@Constraint(validatedBy = ServicePathValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ServicePath {
    String message() default "path must starts with '/' and not ends with '/'";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
