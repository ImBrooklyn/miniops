package uk.org.brooklyn.miniops.warehouse.model.request.app;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uk.org.brooklyn.miniops.warehouse.model.common.ExposureModel;
import uk.org.brooklyn.miniops.warehouse.validate.AppName;

import java.io.Serializable;
import java.util.List;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Data
public class CreateAppReq implements Serializable {
    @NotBlank(message = "appName cannot be blank")
    @AppName
    private String name;

    @Valid
    @NotNull
    private ExposureModel liveness;

    @Valid
    @NotNull
    private ExposureModel readiness;

    @Valid
    @NotEmpty
    private List<ExposureModel> services;

}
