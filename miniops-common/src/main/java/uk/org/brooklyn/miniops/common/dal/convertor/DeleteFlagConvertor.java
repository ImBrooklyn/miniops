package uk.org.brooklyn.miniops.common.dal.convertor;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Converter
public class DeleteFlagConvertor implements AttributeConverter<Boolean, Long> {
    @Override
    public Long convertToDatabaseColumn(Boolean attribute) {
        return attribute ? System.currentTimeMillis() : 0L;
    }

    @Override
    public Boolean convertToEntityAttribute(Long dbData) {
        return dbData != null && dbData != 0;
    }
}
