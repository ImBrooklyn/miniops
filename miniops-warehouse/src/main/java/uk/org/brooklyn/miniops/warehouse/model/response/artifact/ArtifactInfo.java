package uk.org.brooklyn.miniops.warehouse.model.response.artifact;

import lombok.Data;

import java.io.Serializable;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Data
public class ArtifactInfo implements Serializable {

    private Long artifactId;

    private String appName;

    private String version;

    private Integer major;

    private Integer minor;

    private Integer patch;

    private String prerelease;

    private String buildMetadata;
}
