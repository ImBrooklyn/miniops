package uk.org.brooklyn.miniops.confer.model.request.resource;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.collections4.SetUtils;
import org.hibernate.validator.constraints.Length;
import uk.org.brooklyn.miniops.common.exception.ClientXxxException;
import uk.org.brooklyn.miniops.common.validate.ItemNotNull;
import uk.org.brooklyn.miniops.confer.common.enums.ResourceType;
import uk.org.brooklyn.miniops.confer.model.common.PropertyItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Data
public class CreateResourceReq implements Serializable {

    @NotBlank
    @Length(max = 64)
    private String name;

    @NotBlank
    @Length(max = 32)
    private String namespace;

    @NotNull
    @Valid
    private ResourceType type;

    @NotEmpty
    @ItemNotNull
    @Valid
    private List<PropertyItem> resourceProperties;

    public void validate() {
        List<String> propKeys = this.getResourceProperties().stream()
                .map(PropertyItem::getPropKey)
                .toList();

        if (!SetUtils.isEqualSet(propKeys, type.propKeys())) {
            String msg = String.format("illegal resource propKeys, valid prop keys: %s", type.propKeys());
            throw new ClientXxxException(400, msg);
        }

    }
}
