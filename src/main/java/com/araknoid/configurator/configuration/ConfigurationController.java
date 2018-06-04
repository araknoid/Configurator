package com.araknoid.configurator.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.net.URI;

@RestController
@RequestMapping("/configurations")
public class ConfigurationController {

    private ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping("/{id}")
    private Configuration getConfigurationById(@PathVariable String id) {
        return configurationService.getConfigurationById(id);
    }

    @GetMapping
    private Configuration getConfigurationByName(@RequestParam String name) {
        return configurationService.getConfigurationByName(name);
    }

    @PostMapping
    private ResponseEntity<?> addConfiguration(@RequestBody Configuration configurationInput) {
        Configuration configuration = configurationService.saveConfiguration(configurationInput);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(configuration.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/{id}")
    private ResponseEntity deleteConfiguration(@PathVariable String id) {
        try {
            configurationService.deleteConfigurationById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    private ResponseEntity<Configuration> putConfiguration(@PathVariable String id, @RequestBody Configuration configuration) {
        Configuration updatedConfiguration = configurationService.updatedConfiguration(configuration);
        return ResponseEntity.ok(updatedConfiguration);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void configurationNotFoundHandler(ConfigurationNotFoundException ex) {
    }
}
