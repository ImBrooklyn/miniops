package uk.org.brooklyn.miniops.confer.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
public class ResourceNameValidator implements ConstraintValidator<ResourceName, String> {
    private static final String REGEXP = "^[a-z][a-z0-9.]{1,30}[a-z0-9]$";

    private static final Pattern PATTERN = Pattern.compile(REGEXP);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null
                && !StringUtils.containsWhitespace(value)
                && PATTERN.matcher(value).matches();
    }
}
