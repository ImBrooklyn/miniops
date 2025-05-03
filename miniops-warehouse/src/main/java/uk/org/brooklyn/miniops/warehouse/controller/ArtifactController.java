package uk.org.brooklyn.miniops.warehouse.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.org.brooklyn.miniops.warehouse.model.request.artifact.CreateArtifactReq;
import uk.org.brooklyn.miniops.warehouse.model.response.artifact.ArtifactInfo;
import uk.org.brooklyn.miniops.warehouse.service.ArtifactService;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@RestController
@RequestMapping("artifact")
public class ArtifactController {

    private final ArtifactService artifactService;

    public ArtifactController(ArtifactService artifactService) {
        this.artifactService = artifactService;
    }

    @PostMapping
    public ResponseEntity<ArtifactInfo> createArtifact(@Valid @RequestBody CreateArtifactReq body) {
        ArtifactInfo artifact = artifactService.createArtifact(body);
        return ResponseEntity.ok(artifact);
    }

    @GetMapping("/{appName}/{version}")
    public ResponseEntity<ArtifactInfo> queryArtifact(@PathVariable String appName,
                                                      @PathVariable String version) {
        ArtifactInfo artifact = artifactService.queryArtifact(appName, version);
        return ResponseEntity.ok(artifact);
    }
}
