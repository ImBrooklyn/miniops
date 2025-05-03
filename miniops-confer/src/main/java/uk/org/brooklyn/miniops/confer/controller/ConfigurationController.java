package uk.org.brooklyn.miniops.confer.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.org.brooklyn.miniops.confer.model.request.config.AddConfigPropertiesReq;
import uk.org.brooklyn.miniops.confer.model.request.config.AppConfigReq;
import uk.org.brooklyn.miniops.confer.model.request.config.CreateConfigReq;
import uk.org.brooklyn.miniops.confer.model.request.config.EditConfigPropertyReq;
import uk.org.brooklyn.miniops.confer.model.response.AppConfiguration;
import uk.org.brooklyn.miniops.confer.service.ConfigurationService;

/**
 * @author ImBrooklyn
 * @since 02/05/2025
 */
@RestController
@RequestMapping("configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping("app")
    public ResponseEntity<AppConfiguration> appConfig(@Valid AppConfigReq appConfigReq) {
        AppConfiguration configuration = configurationService.appConfig(appConfigReq);
        return ResponseEntity.ok(configuration);
    }

    @PostMapping
    public ResponseEntity<Long> createConfig(@Valid @RequestBody CreateConfigReq createConfigReq) {
        Long configId = configurationService.createConfig(createConfigReq);
        return ResponseEntity.ok(configId);
    }

    @PostMapping("properties")
    public ResponseEntity<Boolean> addConfigProperties(@Valid @RequestBody AddConfigPropertiesReq addConfigPropertiesReq) {
        Boolean added = configurationService.addConfigProperties(addConfigPropertiesReq);
        return ResponseEntity.ok(added);
    }

    @PutMapping("property")
    public ResponseEntity<Boolean> editConfigProperty(@Valid @RequestBody EditConfigPropertyReq editConfigPropertyReq) {
        Boolean edited = configurationService.editConfigProperty(editConfigPropertyReq);
        return ResponseEntity.ok(edited);
    }

    @DeleteMapping("property/{configId}/{propKey}")
    public ResponseEntity<Boolean> deleteConfigProperty(@PathVariable Long configId,
                                                        @PathVariable String propKey) {
        Boolean deleted = configurationService.deleteConfigProperty(configId, propKey);
        return ResponseEntity.ok(deleted);
    }

    @PostMapping("resource")
    public ResponseEntity<Boolean> bindResource(@Valid @NotNull @Positive Long configId,
                                                @Valid @NotNull @Positive Long resourceId) {
        Boolean bound = configurationService.bindResource(configId, resourceId);
        return ResponseEntity.ok(bound);
    }

    @DeleteMapping("resource")
    public ResponseEntity<Boolean> unbindResource(@Valid @NotNull @Positive Long configId,
                                                  @Valid @NotNull @Positive Long resourceId) {
        Boolean unbound = configurationService.unbindResource(configId, resourceId);
        return ResponseEntity.ok(unbound);
    }
}
