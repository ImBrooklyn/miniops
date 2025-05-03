package uk.org.brooklyn.miniops.confer.model.request.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import uk.org.brooklyn.miniops.confer.model.common.PropertyItem;

import java.io.Serializable;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Data
public class EditConfigPropertyReq implements Serializable {
    @NotNull
    @Positive
    private Long configId;

    @NotNull
    @Valid
    private PropertyItem configProperty;

}
