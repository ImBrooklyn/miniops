package uk.org.brooklyn.miniops.confer.model.request.config;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Data
public class CreateConfigReq implements Serializable {
    @NotBlank
    private String appName;

    @NotBlank
    @Length(max = 32)
    private String namespace;

    @Nullable
    @Length(max = 255)
    private String deployment;
}
