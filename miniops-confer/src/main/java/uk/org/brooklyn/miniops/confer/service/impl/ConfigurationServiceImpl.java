package uk.org.brooklyn.miniops.confer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.org.brooklyn.miniops.common.exception.ClientXxxException;
import uk.org.brooklyn.miniops.confer.client.WarehouseClient;
import uk.org.brooklyn.miniops.confer.convert.ConfigurationConvertor;
import uk.org.brooklyn.miniops.confer.dal.model.ConfigProperty;
import uk.org.brooklyn.miniops.confer.dal.model.Configuration;
import uk.org.brooklyn.miniops.confer.dal.model.Resource;
import uk.org.brooklyn.miniops.confer.dal.repository.ConfigPropertyRepository;
import uk.org.brooklyn.miniops.confer.dal.repository.ConfigurationRepository;
import uk.org.brooklyn.miniops.confer.dal.repository.ResourceRepository;
import uk.org.brooklyn.miniops.confer.model.common.PropertyItem;
import uk.org.brooklyn.miniops.confer.model.request.config.AddConfigPropertiesReq;
import uk.org.brooklyn.miniops.confer.model.request.config.AppConfigReq;
import uk.org.brooklyn.miniops.confer.model.request.config.CreateConfigReq;
import uk.org.brooklyn.miniops.confer.model.request.config.EditConfigPropertyReq;
import uk.org.brooklyn.miniops.confer.model.response.AppConfiguration;
import uk.org.brooklyn.miniops.confer.service.ConfigurationService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Service
@Slf4j
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    private final ConfigPropertyRepository configPropertyRepository;

    private final ResourceRepository resourceRepository;

    private final WarehouseClient warehouseClient;

    private final ConfigurationConvertor configurationConvertor;


    public ConfigurationServiceImpl(ConfigurationRepository configurationRepository,
                                    ConfigPropertyRepository configPropertyRepository,
                                    ResourceRepository resourceRepository,
                                    WarehouseClient warehouseClient,
                                    ConfigurationConvertor configurationConvertor) {
        this.configurationRepository = configurationRepository;
        this.configPropertyRepository = configPropertyRepository;
        this.resourceRepository = resourceRepository;
        this.warehouseClient = warehouseClient;
        this.configurationConvertor = configurationConvertor;
    }

    @Override
    @Transactional(readOnly = true)
    public AppConfiguration appConfig(AppConfigReq params) {
        Optional<Configuration> deploymentConfig = configurationRepository.findByAppNameAndNamespaceAndDeployment(
                params.getAppName(),
                params.getNamespace(),
                params.getDeployment()
        );

        Configuration configuration = deploymentConfig.orElseGet(() ->
                configurationRepository.findByAppNameAndNamespaceAndDeployment(
                        params.getAppName(),
                        params.getNamespace(),
                        ""
                ).orElseThrow(() -> ClientXxxException.itemNotFound("configuration")));

        return configurationConvertor.toAppConfiguration(configuration);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Long createConfig(CreateConfigReq params) {
        String appName = params.getAppName();
        warehouseClient.getApp(appName);
        if (StringUtils.isNotBlank(params.getDeployment())) {
            warehouseClient.getDeployment(params.getDeployment());
        }
        Configuration configuration = configurationConvertor.toConfiguration(params);

        if (configurationRepository.existsByAppNameAndNamespaceAndDeployment(
                configuration.getAppName(),
                configuration.getNamespace(),
                configuration.getDeployment()
        )) {
            throw ClientXxxException.itemAlreadyExists("config");
        }

        Configuration saved = configurationRepository.save(configuration);
        return saved.getId();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean addConfigProperties(AddConfigPropertiesReq params) {
        Configuration configuration = configurationRepository.findById(params.getConfigId())
                .orElseThrow(() -> ClientXxxException.itemNotFound("config"));

        List<PropertyItem> configProperties = params.getConfigProperties();
        List<String> propKeys = configProperties.stream().map(PropertyItem::getPropKey)
                .toList();

        boolean keyExists = configPropertyRepository.existsByConfigurationAndPropKeyIn(configuration, propKeys);
        if (keyExists) {
            throw ClientXxxException.itemAlreadyExists("config key(s)");
        }

        List<ConfigProperty> properties = configProperties.stream()
                .map(item -> configurationConvertor.toConfigProperty(configuration.getId(), item))
                .toList();

        configPropertyRepository.saveAllAndFlush(properties);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean editConfigProperty(EditConfigPropertyReq params) {
        Configuration configuration = configurationRepository.findById(params.getConfigId())
                .orElseThrow(() -> ClientXxxException.itemNotFound("config"));

        ConfigProperty configProperty = configPropertyRepository.findByConfigurationAndPropKey(configuration, params.getConfigProperty().getPropKey())
                .orElseThrow(() -> ClientXxxException.itemNotFound("propKey"));

        if (configProperty.getPropValue().equals(params.getConfigProperty().getPropValue())
                && configProperty.getIsSecret().equals(params.getConfigProperty().getIsSecret())) {
            return false;
        }

        configProperty.setPropValue(params.getConfigProperty().getPropValue());
        configProperty.setIsSecret(params.getConfigProperty().getIsSecret());
        configPropertyRepository.saveAndFlush(configProperty);

        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean deleteConfigProperty(Long configId, String propKey) {
        Configuration configuration = configurationRepository.findById(configId)
                .orElseThrow(() -> ClientXxxException.itemNotFound("config"));
        ConfigProperty configProperty = configPropertyRepository.findByConfigurationAndPropKey(configuration, propKey)
                .orElseThrow(() -> ClientXxxException.itemNotFound("propKey"));

        configPropertyRepository.delete(configProperty);
        return true;
    }

    @Override
    public Boolean bindResource(Long configId, Long resourceId) {
        Configuration configuration = configurationRepository.findById(configId)
                .orElseThrow(() -> ClientXxxException.itemNotFound("config"));

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> ClientXxxException.itemNotFound("resource"));

        List<Resource> resources = configuration.getResources();
        if (Stream.ofNullable(resources)
                .flatMap(Collection::stream)
                .anyMatch(res -> res.getType().equals(resource.getType()))) {
            throw ClientXxxException.itemAlreadyExists(String.format("resource of type %s", resource.getType()));
        }

        configuration.getResources().add(resource);
        configurationRepository.saveAndFlush(configuration);
        return true;
    }

    @Override
    public Boolean unbindResource(Long configId, Long resourceId) {
        Configuration configuration = configurationRepository.findById(configId)
                .orElseThrow(() -> ClientXxxException.itemNotFound("config"));

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> ClientXxxException.itemNotFound("resource"));

        List<Resource> resources = configuration.getResources();
        if (!resources.remove(resource)) {
            return false;
        }

        resource.getConfigurations().remove(configuration);
        configurationRepository.saveAndFlush(configuration);

        return true;
    }
}
