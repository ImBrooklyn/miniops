package uk.org.brooklyn.miniops.confer.model.request.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import uk.org.brooklyn.miniops.common.validate.ItemNotNull;
import uk.org.brooklyn.miniops.confer.model.common.PropertyItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Data
public class AddConfigPropertiesReq implements Serializable {
    @NotNull
    @Positive
    private Long configId;

    @NotEmpty
    @ItemNotNull
    @Valid
    private List<PropertyItem> configProperties;

}
