package com.araknoid.configurator.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    private ConfigurationService configurationService;

    @Before
    public void setUp() {
        configurationService = new ConfigurationService(configurationRepository);
    }

    @Test
    public void whenRetrievingConfiguration_thenConfigurationIsReturned() {
        given(configurationRepository.findByName("server.port"))
                .willReturn(Optional.of(new Configuration("server.port", "8080")));

        Configuration configuration = configurationService.getConfigurationByName("server.port");

        assertThat(configuration.getName()).isEqualTo("server.port");
        assertThat(configuration.getValue()).isEqualTo("8080");
    }

    @Test(expected = ConfigurationNotFoundException.class)
    public void whenRetrievingConfiguration_thenNotFoundException() {
        given(configurationRepository.findByName("server.port"))
                .willReturn(Optional.empty());

        configurationService.getConfigurationByName("server.port");

    }

    @Test
    public void whenSavingNewConfiguration_thenSavedConfigurationIsReturned() {
        Configuration configuration = new Configuration("project", "configurator");

        given(configurationRepository.save(configuration))
                .willReturn(configuration);

        Configuration savedConfiguration = configurationService.saveConfiguration(configuration);

        assertThat(savedConfiguration.getName()).isEqualTo(configuration.getName());
        assertThat(savedConfiguration.getValue()).isEqualTo(configuration.getValue());
    }

}