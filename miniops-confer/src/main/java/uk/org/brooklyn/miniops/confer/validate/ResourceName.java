package uk.org.brooklyn.miniops.confer.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Documented
@Constraint(validatedBy = ResourceNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceName {
    String message() default "Configuration name must be 3-32 characters long, " +
            "start with a lowercase letter, and use only lowercase letters, numbers, " +
            "or dot (cannot end with a dot).";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
