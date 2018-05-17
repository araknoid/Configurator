package com.araknoid.configurator.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
public class ConfigurationController {

    private ConfigurationService configurationRepository;

    public ConfigurationController(ConfigurationService configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    @GetMapping("/configurations/{name}")
    private Configuration getConfigurationByName(@PathVariable String name) {
        return configurationRepository.getConfigurationByName(name);
    }

    @PostMapping
    private ResponseEntity<?> addConfiguration(@RequestBody Configuration configurationInput) {
        Configuration configuration = configurationRepository.saveConfiguration(configurationInput);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(configuration.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void configurationNotFoundHandler(ConfigurationNotFoundException ex) {
    }
}
