package uk.org.brooklyn.miniops.confer.model.common;

import lombok.Data;
import uk.org.brooklyn.miniops.confer.common.enums.ResourceType;


import java.io.Serializable;
import java.util.List;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Data
public class ResourceModel implements Serializable {

    private String name;
    private String namespace;
    private ResourceType type;
    private List<PropertyItem> resourceProperties;
}
