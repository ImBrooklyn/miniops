package uk.org.brooklyn.miniops.confer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.org.brooklyn.miniops.common.exception.ClientXxxException;
import uk.org.brooklyn.miniops.common.exception.ServerXxxException;
import uk.org.brooklyn.miniops.confer.convert.ResourceConvertor;
import uk.org.brooklyn.miniops.confer.dal.model.Configuration;
import uk.org.brooklyn.miniops.confer.dal.model.Resource;
import uk.org.brooklyn.miniops.confer.dal.model.ResourceProperty;
import uk.org.brooklyn.miniops.confer.dal.repository.ResourcePropertyRepository;
import uk.org.brooklyn.miniops.confer.dal.repository.ResourceRepository;
import uk.org.brooklyn.miniops.confer.model.common.PropertyItem;
import uk.org.brooklyn.miniops.confer.model.request.resource.CreateResourceReq;
import uk.org.brooklyn.miniops.confer.model.request.resource.EditResourceReq;
import uk.org.brooklyn.miniops.confer.service.ResourceService;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@Service
@Slf4j
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;

    private final ResourcePropertyRepository resourcePropertyRepository;

    private final ResourceConvertor resourceConvertor;

    public ResourceServiceImpl(ResourceRepository resourceRepository,
                               ResourcePropertyRepository resourcePropertyRepository,
                               ResourceConvertor resourceConvertor) {

        this.resourceRepository = resourceRepository;
        this.resourcePropertyRepository = resourcePropertyRepository;
        this.resourceConvertor = resourceConvertor;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Long createResource(CreateResourceReq params) {
        params.validate();
        if (resourceRepository.existsByNameAndNamespace(params.getName(), params.getNamespace())) {
            String message = String.format("resource of [%s - %s]",
                    params.getName(), params.getNamespace());
            throw ClientXxxException.itemAlreadyExists(message);
        }

        Resource resource = resourceConvertor.toResource(params);
        Resource saved = resourceRepository.save(resource);
        return saved.getId();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean deleteResource(Long resourceId) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> ClientXxxException.itemNotFound("resource"));

        List<Configuration> configurations = resource.getConfigurations();
        if (CollectionUtils.isNotEmpty(configurations)) {
            throw new ClientXxxException(400,
                    "resource is currently bound by active configurations and cannot be deleted");
        }

        resourceRepository.delete(resource);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Boolean editResource(Long resourceId, EditResourceReq editResourceReq) {
        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> ClientXxxException.itemNotFound("resource"));

        List<String> propKeys = editResourceReq.getResourceProperties().stream()
                .map(PropertyItem::getPropKey)
                .toList();

        if (!SetUtils.isEqualSet(propKeys, resource.getType().propKeys())) {
            String msg = String.format("illegal resource propKeys, valid prop keys: %s", resource.getType().propKeys());
            throw new ClientXxxException(400, msg);
        }

        List<ResourceProperty> resourceProperties = resource.getResourceProperties();

        List<String> recordPropKeys = Stream.ofNullable(resourceProperties)
                .flatMap(Collection::stream)
                .map(ResourceProperty::getPropKey)
                .toList();


        if (!SetUtils.isEqualSet(recordPropKeys, resource.getType().propKeys())) {
            log.error("invalid resource props, resourceId: {}", resourceId);
            throw new ServerXxxException(500, "internal data error of resource");
        }
        Map<String, String> propMap = editResourceReq.getResourceProperties().stream()
                .collect(Collectors.toMap(PropertyItem::getPropKey, PropertyItem::getPropValue));

        resourceProperties.forEach(rp -> rp.setPropValue(propMap.get(rp.getPropKey())));

        resourcePropertyRepository.saveAllAndFlush(resourceProperties);

        return true;
    }
}
