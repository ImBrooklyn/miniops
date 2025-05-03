package uk.org.brooklyn.miniops.warehouse.model.response.deployment;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ImBrooklyn
 * @since 01/05/2025
 */
@Data
public class DeploymentInfo implements Serializable {
    private String name;

    private String appName;

    private String namespace;

    private String suffix;

    private Integer replicas;
}
