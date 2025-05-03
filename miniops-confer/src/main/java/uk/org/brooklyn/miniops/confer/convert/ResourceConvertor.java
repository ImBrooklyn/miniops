package uk.org.brooklyn.miniops.confer.convert;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import uk.org.brooklyn.miniops.confer.dal.model.Resource;
import uk.org.brooklyn.miniops.confer.dal.model.ResourceProperty;
import uk.org.brooklyn.miniops.confer.model.common.PropertyItem;
import uk.org.brooklyn.miniops.confer.model.common.ResourceModel;
import uk.org.brooklyn.miniops.confer.model.request.resource.CreateResourceReq;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Mapper(componentModel = "spring")
public abstract class ResourceConvertor {

    @Mapping(target = "resourceProperties", ignore = true)
    public abstract ResourceModel toResourceModel(Resource resource);

    @AfterMapping
    protected void toResourceModel(Resource resource, @MappingTarget ResourceModel resourceModel) {
        List<PropertyItem> resourcePropertyItems = Stream.ofNullable(resource.getResourceProperties())
                .flatMap(Collection::stream)
                .map(this::toPropertyItem)
                .toList();
        resourceModel.setResourceProperties(resourcePropertyItems);
    }

    public abstract PropertyItem toPropertyItem(ResourceProperty resourceProperty);

    @Mapping(target = "resourceProperties", ignore = true)
    public abstract Resource toResourceBase(CreateResourceReq createResourceReq);

    public Resource toResource(CreateResourceReq createResourceReq) {
        Resource resource = toResourceBase(createResourceReq);

        List<ResourceProperty> resourceProperties = createResourceReq.getResourceProperties().stream()
                .map(this::toResourceProperty)
                .peek(resourceProperty -> resourceProperty.setResource(resource))
                .toList();

        resource.setResourceProperties(resourceProperties);
        return resource;
    }

    public abstract ResourceProperty toResourceProperty(PropertyItem propertyItem);
}
