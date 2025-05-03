package uk.org.brooklyn.miniops.warehouse.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.org.brooklyn.miniops.common.exception.ClientXxxException;
import uk.org.brooklyn.miniops.warehouse.convert.ArtifactConvertor;
import uk.org.brooklyn.miniops.warehouse.dal.model.Application;
import uk.org.brooklyn.miniops.warehouse.dal.model.Artifact;
import uk.org.brooklyn.miniops.warehouse.dal.repository.ApplicationRepository;
import uk.org.brooklyn.miniops.warehouse.dal.repository.ArtifactRepository;
import uk.org.brooklyn.miniops.warehouse.model.request.artifact.CreateArtifactReq;
import uk.org.brooklyn.miniops.warehouse.model.response.artifact.ArtifactInfo;
import uk.org.brooklyn.miniops.warehouse.service.ArtifactService;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Service
@Slf4j
public class ArtifactServiceImpl implements ArtifactService {


    private final ApplicationRepository applicationRepository;

    private final ArtifactRepository artifactRepository;

    private final ArtifactConvertor artifactConvertor;

    public ArtifactServiceImpl(ApplicationRepository applicationRepository,
                               ArtifactRepository artifactRepository,
                               ArtifactConvertor artifactConvertor) {
        this.applicationRepository = applicationRepository;
        this.artifactRepository = artifactRepository;
        this.artifactConvertor = artifactConvertor;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ArtifactInfo createArtifact(CreateArtifactReq param) {
        Application application = applicationRepository.findByName(param.getAppName())
                .orElseThrow(() -> ClientXxxException.itemNotFound("app"));

        artifactRepository.findByApplicationAndVersion(application, param.getVersion())
                .ifPresent((i) -> {
                    throw ClientXxxException.itemAlreadyExists("artifact");
                });

        Artifact artifact = artifactConvertor.toArtifact(param);
        artifact.setApplication(application);

        artifactRepository.saveAndFlush(artifact);
        return artifactConvertor.toArtifactInfo(artifact);
    }

    @Override
    public ArtifactInfo queryArtifact(String appName, String version) {
        Application application = applicationRepository.findByName(appName)
                .orElseThrow(() -> ClientXxxException.itemNotFound("app"));

        Artifact artifact = artifactRepository.findByApplicationAndVersion(application, version)
                .orElseThrow(() -> ClientXxxException.itemNotFound("artifact"));
        return artifactConvertor.toArtifactInfo(artifact);
    }
}
