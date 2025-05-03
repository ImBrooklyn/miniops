package uk.org.brooklyn.miniops.confer.convert;

import jakarta.annotation.Resource;
import org.mapstruct.*;
import uk.org.brooklyn.miniops.confer.dal.model.ConfigProperty;
import uk.org.brooklyn.miniops.confer.dal.model.Configuration;
import uk.org.brooklyn.miniops.confer.model.common.PropertyItem;
import uk.org.brooklyn.miniops.confer.model.common.ResourceModel;
import uk.org.brooklyn.miniops.confer.model.request.config.CreateConfigReq;
import uk.org.brooklyn.miniops.confer.model.response.AppConfiguration;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Mapper(componentModel = "spring")
public abstract class ConfigurationConvertor {

    @Resource
    private ResourceConvertor resourceConvertor;

    @Mapping(
            target = "deployment",
            defaultExpression = "java(\"\")"
    )
    public abstract Configuration toConfiguration(CreateConfigReq createConfigReq);

    public abstract PropertyItem toPropertyItem(ConfigProperty configProperty);

    @Mapping(target = "configuration", ignore = true)
    public abstract ConfigProperty toConfigProperty(Long configId ,PropertyItem propertyItem);

    @AfterMapping
    protected void  toConfigProperty(Long configId , PropertyItem propertyItem, @MappingTarget ConfigProperty target) {
        Configuration configuration = new Configuration();
        configuration.setId(configId);
        target.setConfiguration(configuration);
    }

    @Mapping(target = "configProperties", ignore = true)
    @Mapping(target = "resources", ignore = true)
    public abstract AppConfiguration toAppConfiguration(Configuration configuration);

    @AfterMapping
    protected void toAppConfiguration(Configuration configuration, @MappingTarget AppConfiguration target) {
        List<PropertyItem> propertyItems = Stream.ofNullable(configuration.getConfigProperties())
                .flatMap(Collection::stream)
                .map(this::toPropertyItem)
                .toList();
        target.setConfigProperties(propertyItems);

        List<ResourceModel> resourceModels = Stream.ofNullable(configuration.getResources())
                .flatMap(Collection::stream)
                .map(resourceConvertor::toResourceModel)
                .toList();

        target.setResources(resourceModels);
    }

}
