package uk.org.brooklyn.miniops.warehouse.model.response.app;

import lombok.Data;
import uk.org.brooklyn.miniops.warehouse.model.common.ExposureModel;
import uk.org.brooklyn.miniops.warehouse.model.response.deployment.DeploymentInfo;

import java.io.Serializable;
import java.util.List;

/**
 * @author ImBrooklyn
 * @since 30/04/2025
 */
@Data
public class AppInfo implements Serializable {

    private Long id;

    private String name;

    private ExposureModel liveness;

    private ExposureModel readiness;

    private List<ExposureModel> services;

    private List<DeploymentInfo> deployments;


}
