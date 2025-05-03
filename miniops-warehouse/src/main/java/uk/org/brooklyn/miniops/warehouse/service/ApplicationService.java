package uk.org.brooklyn.miniops.warehouse.service;

import uk.org.brooklyn.miniops.warehouse.model.request.app.CreateAppReq;
import uk.org.brooklyn.miniops.warehouse.model.request.app.QueryAppReq;
import uk.org.brooklyn.miniops.warehouse.model.response.PageResp;
import uk.org.brooklyn.miniops.warehouse.model.response.app.AppInfo;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
public interface ApplicationService {

    /**
     * create app
     *
     * @param param request form
     * @return app id
     */
    Long createApp(CreateAppReq param);

    PageResp<AppInfo> queryApps(QueryAppReq params);

    AppInfo queryAppByName(String name);
}
