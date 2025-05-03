package uk.org.brooklyn.miniops.warehouse.model.request.deployment;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import uk.org.brooklyn.miniops.warehouse.validate.AppName;
import uk.org.brooklyn.miniops.warehouse.validate.DeploymentSuffix;

import java.io.Serializable;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Data
public class CreateDeploymentReq implements Serializable {
    @NotBlank(message = "appName cannot be blank")
    @AppName
    private String appName;

    @NotBlank
    @Length(max = 32)
    private String namespace;

    @Nullable
    @Length(max = 32)
    @DeploymentSuffix
    private String suffix;

    @NotNull
    @PositiveOrZero
    private Integer replicas;
}
