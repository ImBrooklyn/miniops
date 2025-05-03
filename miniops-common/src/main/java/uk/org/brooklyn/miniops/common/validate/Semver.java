package uk.org.brooklyn.miniops.common.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Documented
@Constraint(validatedBy = SemverValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Semver {
    String message() default "invalid semantic version";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
