package uk.org.brooklyn.miniops.confer.model.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Data
public class PropertyItem implements Serializable {
    @NotBlank
    @Length(max = 255)
    private String propKey;

    @NotBlank
    @Length(max = 1024)
    private String propValue;

    @NotNull
    private Boolean isSecret;
}
