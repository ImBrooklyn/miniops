package uk.org.brooklyn.miniops.warehouse.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.org.brooklyn.miniops.common.exception.ClientXxxException;
import uk.org.brooklyn.miniops.warehouse.convert.ApplicationConvertor;
import uk.org.brooklyn.miniops.warehouse.convert.DalConvertor;
import uk.org.brooklyn.miniops.warehouse.dal.model.Application;
import uk.org.brooklyn.miniops.warehouse.dal.repository.ApplicationRepository;
import uk.org.brooklyn.miniops.warehouse.model.request.app.CreateAppReq;
import uk.org.brooklyn.miniops.warehouse.model.request.app.QueryAppReq;
import uk.org.brooklyn.miniops.warehouse.model.response.PageResp;
import uk.org.brooklyn.miniops.warehouse.model.response.app.AppInfo;
import uk.org.brooklyn.miniops.warehouse.service.ApplicationService;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final ApplicationConvertor applicationConvertor;

    private final DalConvertor dalConvertor;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository,
                                  ApplicationConvertor applicationConvertor,
                                  DalConvertor dalConvertor) {
        this.applicationRepository = applicationRepository;
        this.applicationConvertor = applicationConvertor;
        this.dalConvertor = dalConvertor;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Long createApp(CreateAppReq param) {
        String appName = param.getName();

        if (applicationRepository.existsByName(appName)) {
            throw ClientXxxException.itemAlreadyExists("app");
        }

        Application application = applicationConvertor.toApplication(param);
        Application saved = applicationRepository.save(application);
        return saved.getId();
    }

    @Override
    public PageResp<AppInfo> queryApps(QueryAppReq params) {
        Page<Application> page = applicationRepository.findAll(
                applicationConvertor.toExample(params),
                dalConvertor.toPageable(params)
        );

        return dalConvertor.toPageResp(page, applicationConvertor::toAppInfo);
    }

    @Override
    @Transactional(readOnly = true)
    public AppInfo queryAppByName(String name) {
        Application application = applicationRepository.findByName(name)
                .orElseThrow(() -> ClientXxxException.itemNotFound("app"));

        AppInfo appInfo = applicationConvertor.toAppInfo(application);
        applicationConvertor.attachAppExposures(appInfo, application.getAppExposures());
        applicationConvertor.attachDeployments(appInfo, application.getDeployments());

        return appInfo;
    }


}
