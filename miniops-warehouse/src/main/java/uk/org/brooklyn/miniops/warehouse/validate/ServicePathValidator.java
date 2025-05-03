package uk.org.brooklyn.miniops.warehouse.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
public class ServicePathValidator implements ConstraintValidator<ServicePath, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null
                && !StringUtils.containsWhitespace(value)
                && value.startsWith("/")
                && ("/".equals(value) || !value.endsWith("/"));
    }
}
