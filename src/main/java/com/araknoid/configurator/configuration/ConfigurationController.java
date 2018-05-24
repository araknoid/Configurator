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
    private ResponseEntity deleteConfiguration(@PathVariable Long id) {
        try {
            configurationService.deleteConfigurationById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void configurationNotFoundHandler(ConfigurationNotFoundException ex) {
    }
}
