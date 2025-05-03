package uk.org.brooklyn.miniops.warehouse.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.org.brooklyn.miniops.common.exception.ClientXxxException;
import uk.org.brooklyn.miniops.warehouse.convert.DeploymentConvertor;
import uk.org.brooklyn.miniops.warehouse.dal.model.Application;
import uk.org.brooklyn.miniops.warehouse.dal.model.Deployment;
import uk.org.brooklyn.miniops.warehouse.dal.repository.ApplicationRepository;
import uk.org.brooklyn.miniops.warehouse.dal.repository.DeploymentRepository;
import uk.org.brooklyn.miniops.warehouse.model.request.deployment.CreateDeploymentReq;
import uk.org.brooklyn.miniops.warehouse.model.response.deployment.DeploymentInfo;
import uk.org.brooklyn.miniops.warehouse.service.DeploymentService;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Service
@Slf4j
public class DeploymentServiceImpl implements DeploymentService {

    private final ApplicationRepository applicationRepository;

    private final DeploymentRepository deploymentRepository;

    private final DeploymentConvertor deploymentConvertor;

    public DeploymentServiceImpl(ApplicationRepository applicationRepository,
                                 DeploymentRepository deploymentRepository,
                                 DeploymentConvertor deploymentConvertor) {

        this.applicationRepository = applicationRepository;
        this.deploymentRepository = deploymentRepository;
        this.deploymentConvertor = deploymentConvertor;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public String createDeployment(CreateDeploymentReq param) {
        Application application = applicationRepository.findByName(param.getAppName())
                .orElseThrow(() -> ClientXxxException.itemNotFound("app"));

        Deployment deployment = deploymentConvertor.toDeployment(param);
        deployment.setApplication(application);

        String deploymentName = deployment.getName();

        if (deploymentRepository.existsByName(deploymentName)) {
            throw ClientXxxException.itemAlreadyExists("deployment");
        }

        deploymentRepository.saveAndFlush(deployment);
        return deploymentName;
    }

    @Override
    public DeploymentInfo queryDeploymentByName(String name) {
        Deployment deployment = deploymentRepository.findByName(name)
                .orElseThrow(() -> ClientXxxException.itemNotFound("deployment"));
        return deploymentConvertor.toDeploymentInfo(deployment);
    }
}
