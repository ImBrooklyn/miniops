package uk.org.brooklyn.miniops.warehouse.service;

import uk.org.brooklyn.miniops.warehouse.model.request.deployment.CreateDeploymentReq;
import uk.org.brooklyn.miniops.warehouse.model.response.deployment.DeploymentInfo;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
public interface DeploymentService {

    /**
     * create deployment
     *
     * @param param request form
     * @return deployment name
     */
    String createDeployment(CreateDeploymentReq param);

    DeploymentInfo queryDeploymentByName(String name);
}
