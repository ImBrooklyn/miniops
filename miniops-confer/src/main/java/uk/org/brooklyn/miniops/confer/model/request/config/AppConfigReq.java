package uk.org.brooklyn.miniops.confer.model.request.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Data
public class AppConfigReq implements Serializable {
    @NotBlank
    private String appName;

    @NotBlank
    @Length(max = 32)
    private String namespace;

    @NotBlank
    @Length(max = 255)
    private String deployment;
}
