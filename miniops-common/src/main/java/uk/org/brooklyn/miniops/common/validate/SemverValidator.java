package uk.org.brooklyn.miniops.common.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uk.org.brooklyn.miniops.common.util.SemanticVersion;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
public class SemverValidator implements ConstraintValidator<Semver, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return  SemanticVersion.isValid(value);
    }
}
