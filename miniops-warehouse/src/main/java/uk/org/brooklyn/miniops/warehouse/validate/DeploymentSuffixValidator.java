package uk.org.brooklyn.miniops.warehouse.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
public class DeploymentSuffixValidator implements ConstraintValidator<DeploymentSuffix, String> {

    private static final String REGEXP = "^[a-z]{3,32}$";

    private static final Pattern PATTERN = Pattern.compile(REGEXP);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null
                && !StringUtils.containsWhitespace(value)
                && PATTERN.matcher(value).matches();
    }
}
