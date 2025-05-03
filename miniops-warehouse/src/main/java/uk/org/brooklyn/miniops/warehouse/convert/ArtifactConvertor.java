package uk.org.brooklyn.miniops.warehouse.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.org.brooklyn.miniops.warehouse.dal.model.Artifact;
import uk.org.brooklyn.miniops.warehouse.model.request.artifact.CreateArtifactReq;
import uk.org.brooklyn.miniops.warehouse.model.response.artifact.ArtifactInfo;
import uk.org.brooklyn.miniops.common.util.SemanticVersion;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Mapper(componentModel = "spring")
public abstract class ArtifactConvertor {

    public Artifact toArtifact(CreateArtifactReq createArtifactReq) {
        String version = createArtifactReq.getVersion();
        SemanticVersion semanticVersion = SemanticVersion.parse(version);

        Artifact artifact = toArtifact(semanticVersion);
        artifact.setVersion(version);

        return artifact;
    }

    public abstract Artifact toArtifact(SemanticVersion semanticVersion);

    @Mapping(target = "artifactId", source = "artifact.id")
    @Mapping(target = "appName", source = "artifact.application.name")
    public abstract ArtifactInfo toArtifactInfo(Artifact artifact);
}
