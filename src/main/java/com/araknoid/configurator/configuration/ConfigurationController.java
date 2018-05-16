package com.araknoid.configurator.configuration;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private void configurationNotFoundHandler(ConfigurationNotFoundException ex) {
    }
}
