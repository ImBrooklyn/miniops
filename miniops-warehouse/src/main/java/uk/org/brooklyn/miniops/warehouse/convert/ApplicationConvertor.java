package uk.org.brooklyn.miniops.warehouse.convert;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import uk.org.brooklyn.miniops.warehouse.dal.model.AppExposure;
import uk.org.brooklyn.miniops.warehouse.dal.model.Application;
import uk.org.brooklyn.miniops.warehouse.dal.model.Deployment;
import uk.org.brooklyn.miniops.warehouse.enums.ExposureType;
import uk.org.brooklyn.miniops.warehouse.model.common.ExposureModel;
import uk.org.brooklyn.miniops.warehouse.model.request.app.CreateAppReq;
import uk.org.brooklyn.miniops.warehouse.model.request.app.QueryAppReq;
import uk.org.brooklyn.miniops.warehouse.model.response.app.AppInfo;
import uk.org.brooklyn.miniops.warehouse.model.response.deployment.DeploymentInfo;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */

@Mapper(componentModel = "spring")
public abstract class ApplicationConvertor {

    @Resource
    private DeploymentConvertor deploymentConvertor;

    @Mapping(target = "type", source = "portType")
    @Mapping(target = "port", source = "exposureModel.port")
    @Mapping(target = "path", source = "exposureModel.path")
    @Mapping(target = "params", ignore = true)
    public abstract AppExposure toAppExposure(ExposureType portType,
                                              ExposureModel exposureModel);

    @AfterMapping
    protected void toAppExposure(ExposureType portType,
                                 ExposureModel exposureModel,
                                 @MappingTarget AppExposure target) {
        Map<String, Object> params = exposureModel.getParams();
        if (params != null) {
            target.setParams(JSON.toJSONString(params));
        }
    }

    @Mapping(target = "params", ignore = true)
    public abstract ExposureModel toExposureModel(AppExposure appExposure);

    @AfterMapping
    protected void toExposureModel(AppExposure appExposure, @MappingTarget ExposureModel target) {
        if (StringUtils.isNotBlank(appExposure.getParams())) {
            target.setParams(JSON.parseObject(appExposure.getParams()));
        }
    }

    public abstract Application toApplication(QueryAppReq queryAppReq);

    @Mapping(target = "deployments", ignore = true)
    public abstract AppInfo toAppInfo(Application application);

    public void attachAppExposures(AppInfo appInfo, List<AppExposure> exposures) {
        if (appInfo == null || CollectionUtils.isEmpty(exposures)) {
            return;
        }

        Map<ExposureType, List<AppExposure>> exposureGroup = exposures.stream()
                .collect(Collectors.groupingBy(AppExposure::getType));

        Stream.ofNullable(exposureGroup.get(ExposureType.LIVENESS))
                .flatMap(Collection::stream)
                .map(this::toExposureModel)
                .findFirst()
                .ifPresent(appInfo::setLiveness);

        Stream.ofNullable(exposureGroup.get(ExposureType.READINESS))
                .flatMap(Collection::stream)
                .map(this::toExposureModel)
                .findFirst()
                .ifPresent(appInfo::setReadiness);

        List<ExposureModel> services = Stream.ofNullable(exposureGroup.get(ExposureType.SERVICE))
                .flatMap(Collection::stream)
                .map(this::toExposureModel)
                .toList();

        appInfo.setServices(services);
    }

    public void attachDeployments(AppInfo appInfo, List<Deployment> deployments) {
        if (appInfo == null) {
            return;
        }

        List<DeploymentInfo> deploymentInfos = Stream.ofNullable(deployments)
                .flatMap(Collection::stream)
                .map(deploymentConvertor::toDeploymentInfo)
                .toList();

        appInfo.setDeployments(deploymentInfos);
    }


    public Application toApplication(CreateAppReq createAppReq) {
        Application application = new Application();
        application.setName(createAppReq.getName());
        List<AppExposure> appExposures = new ArrayList<>();
        application.setAppExposures(appExposures);

        ExposureModel liveness = createAppReq.getLiveness();
        ExposureModel readiness = createAppReq.getReadiness();
        List<ExposureModel> services = createAppReq.getServices();

        appExposures.add(toAppExposure(ExposureType.LIVENESS, liveness));
        appExposures.add(toAppExposure(ExposureType.READINESS, readiness));

        appExposures.addAll(
                services.stream()
                        .map(svc -> toAppExposure(ExposureType.SERVICE, svc))
                        .peek(svc -> svc.setApplication(application))
                        .toList()
        );

        return application;
    }

    public Example<Application> toExample(QueryAppReq queryAppReq) {
        Objects.requireNonNull(queryAppReq);
        return Example.of(
                toApplication(queryAppReq),
                ExampleMatcher.matching()
                        .withMatcher("name",
                                ExampleMatcher.GenericPropertyMatchers.startsWith())
        );
    }

}
