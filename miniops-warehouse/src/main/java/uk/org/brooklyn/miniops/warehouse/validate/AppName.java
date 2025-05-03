package uk.org.brooklyn.miniops.warehouse.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Documented
@Constraint(validatedBy = AppNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface AppName {
    String message() default "Application name must be 3-32 characters long, " +
            "start with a lowercase letter, and use only lowercase letters, numbers, " +
            "or hyphens (cannot end with a hyphen).";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
