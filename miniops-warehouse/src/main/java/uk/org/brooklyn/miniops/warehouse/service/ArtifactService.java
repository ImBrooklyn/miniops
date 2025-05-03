package uk.org.brooklyn.miniops.warehouse.service;

import uk.org.brooklyn.miniops.warehouse.model.request.artifact.CreateArtifactReq;
import uk.org.brooklyn.miniops.warehouse.model.response.artifact.ArtifactInfo;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
public interface ArtifactService {

    ArtifactInfo createArtifact(CreateArtifactReq param);

    ArtifactInfo queryArtifact(String appName, String version);
}
