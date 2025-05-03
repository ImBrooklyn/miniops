package uk.org.brooklyn.miniops.confer.model.response;

import lombok.Data;
import uk.org.brooklyn.miniops.confer.model.common.PropertyItem;
import uk.org.brooklyn.miniops.confer.model.common.ResourceModel;

import java.io.Serializable;
import java.util.List;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Data
public class AppConfiguration implements Serializable {

    private String appName;

    private String namespace;

    private String deployment;

    private List<PropertyItem> configProperties;

    private List<ResourceModel> resources;
}
