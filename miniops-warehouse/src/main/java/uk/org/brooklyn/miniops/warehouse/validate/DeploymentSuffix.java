package uk.org.brooklyn.miniops.warehouse.validate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Documented
@Constraint(validatedBy = DeploymentSuffixValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface DeploymentSuffix {
    String message() default "Deployment suffix must be lowercase letters only, 3-32 characters long.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
