package uk.org.brooklyn.miniops.warehouse.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.org.brooklyn.miniops.warehouse.model.request.deployment.CreateDeploymentReq;
import uk.org.brooklyn.miniops.warehouse.model.response.deployment.DeploymentInfo;
import uk.org.brooklyn.miniops.warehouse.service.DeploymentService;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@RestController
@RequestMapping("deployment")
public class DeploymentController {

    private final DeploymentService deploymentService;

    public DeploymentController(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    @PostMapping
    public ResponseEntity<String> createDeployment(@Valid @RequestBody CreateDeploymentReq createDeploymentReq) {
        String deploymentName = deploymentService.createDeployment(createDeploymentReq);
        return ResponseEntity.ok(deploymentName);
    }

    @GetMapping("/{name}")
    public ResponseEntity<DeploymentInfo> queryDeploymentByName(@PathVariable String name) {
        DeploymentInfo deploymentInfo = deploymentService.queryDeploymentByName(name);
        return ResponseEntity.ok(deploymentInfo);
    }
}
