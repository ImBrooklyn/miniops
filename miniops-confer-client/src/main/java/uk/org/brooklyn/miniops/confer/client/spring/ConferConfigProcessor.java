package uk.org.brooklyn.miniops.confer.client.spring;

import jakarta.annotation.Nonnull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import uk.org.brooklyn.miniops.confer.client.env.ConferEnv;
import uk.org.brooklyn.miniops.confer.common.enums.ResourceType;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * @author ImBrooklyn
 * @since 03/05/2025
 */
public class ConferConfigProcessor implements EnvironmentAware, BeanDefinitionRegistryPostProcessor {

    private ConfigurableEnvironment environment;

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@Nonnull BeanDefinitionRegistry registry) throws BeansException {
        ConferEnv conferEnv = ConferEnv.getInstance();
        if (!conferEnv.isEnabled()) {
            return;
        }

        Map<String, String> configs = findLocalConfigs();

        Properties properties = new Properties();
        properties.putAll(configs);

        PropertiesPropertySource propertySource = new PropertiesPropertySource("conferPropertySource", properties);
        MutablePropertySources propertySources = environment.getPropertySources();
        propertySources.addFirst(propertySource);
    }

    private Map<String, String> findLocalConfigs() {
        ConferEnv conferEnv = ConferEnv.getInstance();
        String conferDir = conferEnv.getConferDir();
        if (!Files.exists(Path.of(conferDir))) {
            throw new RuntimeException(String.format("confer dir [%s] does not exits", conferDir));
        }
        Map<String, String> configs = new HashMap<>();

        // confer resource
        for (ResourceType resourceType : ResourceType.values()) {
            String resourcePath = String.format("%s/%s/%s.properties",
                    conferDir, conferEnv.getResourceDir(), resourceType.name().toLowerCase());
            Map<String, String> resource = loadFromPath(resourcePath);

            Map<String, String> propsMapping = resourceType.propsMapping();
            resource.entrySet().forEach(e -> Optional.of(e)
                    .map(Map.Entry::getKey)
                    .map(propsMapping::get)
                    .ifPresent(sp -> configs.put(sp, e.getValue())));
        }

        // config map
        String configMapPath = String.format("%s/%s/configmap.properties", conferDir, conferEnv.getConfigmapDir());
        configs.putAll(loadFromPath(configMapPath));
        // secret
        String secretPath = String.format("%s/%s/secret.properties", conferDir, conferEnv.getSecretDir());
        configs.putAll(loadFromPath(secretPath));

        return Collections.unmodifiableMap(configs);
    }


    private Map<String, String> loadFromPath(String path) {
        return loadFromResource(new FileSystemResource(path));
    }

    private Map<String, String> loadFromResource(Resource resource) {
        Map<String, String> map = new HashMap<>();
        if (!resource.exists()) {
            return Collections.emptyMap();
        }
        Properties properties = new Properties();
        try {
            InputStream inputStream = resource.getInputStream();
            properties.load(inputStream);
        } catch (IOException e) {
            return Collections.emptyMap();
        }

        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            if (entry.getKey() instanceof String k && entry.getValue() instanceof String v) {
                map.put(k, v);
            }
        }

        return Collections.unmodifiableMap(map);
    }
}
