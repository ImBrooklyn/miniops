package uk.org.brooklyn.miniops.common.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;
import java.util.Objects;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
public class ItemNotNullValidator implements ConstraintValidator<ItemNotNull, Collection<?>> {
    @Override
    public boolean isValid(Collection<?> collection, ConstraintValidatorContext context) {
        return collection != null
                && !collection.isEmpty()
                && collection.stream().allMatch(Objects::nonNull);
    }
}
