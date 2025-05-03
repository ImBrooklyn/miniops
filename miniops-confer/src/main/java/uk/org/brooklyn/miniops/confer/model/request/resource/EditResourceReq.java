package uk.org.brooklyn.miniops.confer.model.request.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
public class EditResourceReq implements Serializable {
    @NotEmpty
    @ItemNotNull
    @Valid
    private List<PropertyItem> resourceProperties;
}
