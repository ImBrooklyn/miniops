package uk.org.brooklyn.miniops.warehouse.model.request.artifact;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import uk.org.brooklyn.miniops.common.validate.Semver;

import java.io.Serializable;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Data
public class CreateArtifactReq implements Serializable {

    @NotBlank
    private String appName;

    @NotBlank
    @Length(max = 255)
    @Semver
    private String version;
}
