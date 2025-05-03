package uk.org.brooklyn.miniops.confer.service;

import jakarta.validation.Valid;
import uk.org.brooklyn.miniops.confer.model.request.config.AddConfigPropertiesReq;
import uk.org.brooklyn.miniops.confer.model.request.config.AppConfigReq;
import uk.org.brooklyn.miniops.confer.model.request.config.CreateConfigReq;
import uk.org.brooklyn.miniops.confer.model.request.config.EditConfigPropertyReq;
import uk.org.brooklyn.miniops.confer.model.response.AppConfiguration;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
public interface ConfigurationService {

    AppConfiguration appConfig(AppConfigReq params);

    Long createConfig(CreateConfigReq params);

    Boolean addConfigProperties(AddConfigPropertiesReq params);

    Boolean editConfigProperty(EditConfigPropertyReq params);

    Boolean deleteConfigProperty(Long configId, String propKey);

    Boolean bindResource(Long configId, Long resourceId);

    Boolean unbindResource(Long configId, Long resourceId);
}
