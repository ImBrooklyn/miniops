package uk.org.brooklyn.miniops.confer.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.org.brooklyn.miniops.confer.model.request.resource.CreateResourceReq;
import uk.org.brooklyn.miniops.confer.model.request.resource.EditResourceReq;
import uk.org.brooklyn.miniops.confer.service.ResourceService;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@RestController
@RequestMapping("resource")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @PostMapping
    public ResponseEntity<Long> createResource(@Valid @RequestBody CreateResourceReq createResourceReq) {
        Long resourceId = resourceService.createResource(createResourceReq);
        return ResponseEntity.ok(resourceId);
    }

    @DeleteMapping("{resourceId}")
    public ResponseEntity<Boolean> deleteResource(@Valid @PathVariable @Positive Long resourceId) {
        Boolean deleted = resourceService.deleteResource(resourceId);
        return ResponseEntity.ok(deleted);
    }

    @PutMapping("{resourceId}")
    public ResponseEntity<Boolean> editResource(@Valid @PathVariable @Positive Long resourceId,
                                                @Valid @RequestBody EditResourceReq editResourceReq) {
        Boolean edited = resourceService.editResource(resourceId, editResourceReq);
        return ResponseEntity.ok(edited);
    }

}
