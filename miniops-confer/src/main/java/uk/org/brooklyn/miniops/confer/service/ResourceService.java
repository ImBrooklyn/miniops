package uk.org.brooklyn.miniops.confer.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import uk.org.brooklyn.miniops.confer.model.request.resource.CreateResourceReq;
import uk.org.brooklyn.miniops.confer.model.request.resource.EditResourceReq;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
public interface ResourceService {

    Long createResource(CreateResourceReq params);

    Boolean deleteResource(Long resourceId);

    Boolean editResource(Long resourceId, EditResourceReq editResourceReq);
}
