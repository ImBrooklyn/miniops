package uk.org.brooklyn.miniops.warehouse.convert;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uk.org.brooklyn.miniops.warehouse.dal.model.Deployment;
import uk.org.brooklyn.miniops.warehouse.model.request.deployment.CreateDeploymentReq;
import uk.org.brooklyn.miniops.warehouse.model.response.deployment.DeploymentInfo;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Mapper(
        componentModel = "spring",
        imports = {
                StringUtils.class
        }
)
public abstract class DeploymentConvertor {

    @Mapping(target = "appName", source = "deployment.application.name")
    public abstract DeploymentInfo toDeploymentInfo(Deployment deployment);

    @Mapping(target = "suffix", defaultValue = "default")
    public abstract Deployment toDeployment(CreateDeploymentReq createDeploymentReq);

    @AfterMapping
    protected void toDeployment(CreateDeploymentReq createDeploymentReq, @MappingTarget Deployment target) {
        String deploymentName = String.format("%s-%s-%s",
                createDeploymentReq.getAppName(),
                target.getNamespace(),
                target.getSuffix()
        );

        target.setName(deploymentName);
    }

}
