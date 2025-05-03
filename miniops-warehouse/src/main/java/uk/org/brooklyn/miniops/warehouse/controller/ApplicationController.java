package uk.org.brooklyn.miniops.warehouse.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.org.brooklyn.miniops.warehouse.model.request.app.CreateAppReq;
import uk.org.brooklyn.miniops.warehouse.model.request.app.QueryAppReq;
import uk.org.brooklyn.miniops.warehouse.model.response.PageResp;
import uk.org.brooklyn.miniops.warehouse.model.response.app.AppInfo;
import uk.org.brooklyn.miniops.warehouse.service.ApplicationService;

/**
 * @author ImBrooklyn
 * @since 26/04/2025
 */
@RestController
@RequestMapping("application")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<Long> createApp(@Valid @RequestBody CreateAppReq createAppReq) {
        Long appId = applicationService.createApp(createAppReq);
        return ResponseEntity.ok(appId);
    }

    @GetMapping
    public ResponseEntity<PageResp<AppInfo>> queryApps(@Valid QueryAppReq params) {
        PageResp<AppInfo> response = applicationService.queryApps(params);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<AppInfo> queryAppByName(@PathVariable String name) {
        AppInfo response = applicationService.queryAppByName(name);
        return ResponseEntity.ok(response);
    }
}
