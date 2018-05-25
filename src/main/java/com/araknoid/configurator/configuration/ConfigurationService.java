package com.araknoid.configurator.configuration;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {
    private final ConfigurationRepository configurationRepository;

    public ConfigurationService(ConfigurationRepository configurationRepository) {

        this.configurationRepository = configurationRepository;
    }

    public Configuration getConfigurationByName(String name) {
        return configurationRepository.findByName(name).orElseThrow(() -> new ConfigurationNotFoundException());
    }

    public Configuration saveConfiguration(Configuration configuration) {
        return configurationRepository.save(configuration);
    }

    public void deleteConfigurationById(Long configurationId) {
        configurationRepository.deleteById(configurationId);
    }

    public Configuration getConfigurationById(Long configurationId) {
        return configurationRepository.findById(configurationId).orElseThrow(() -> new ConfigurationNotFoundException());
    }
}
