package uk.org.brooklyn.miniops.warehouse.model.common;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import uk.org.brooklyn.miniops.warehouse.validate.ServicePath;

import java.io.Serializable;
import java.util.Map;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Getter
@Setter
public class ExposureModel implements Serializable {

    @ServicePath
    private String path;

    @NotNull
    @Range(min = 1000, max = 65535)
    private Integer port;

    private Map<String, Object> params;
}
