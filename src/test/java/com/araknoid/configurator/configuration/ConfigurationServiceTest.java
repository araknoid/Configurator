package com.araknoid.configurator.configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ConfigurationServiceTest {

    @Mock
    private ConfigurationRepository configurationRepository;

    private ConfigurationService configurationService;

    private Configuration configuration;

    @Before
    public void setUp() {
        configurationService = new ConfigurationService(configurationRepository);
        configuration = new Configuration("server.port", "Port of the server", "8080");
    }

    @Test
    public void whenRetrievingConfiguration_thenConfigurationIsReturned() {
        given(configurationRepository.findByName(anyString()))
                .willReturn(Optional.of(configuration));

        Configuration retrievedConfiguration = configurationService.getConfigurationByName(configuration.getName());

        assertThat(retrievedConfiguration.getId()).isEqualTo(configuration.getId());
        assertThat(retrievedConfiguration.getName()).isEqualTo(configuration.getName());
        assertThat(retrievedConfiguration.getValue()).isEqualTo(configuration.getValue());
    }

    @Test(expected = ConfigurationNotFoundException.class)
    public void whenRetrievingConfiguration_thenNotFoundException() {
        given(configurationRepository.findByName(anyString()))
                .willReturn(Optional.empty());

        configurationService.getConfigurationByName(configuration.getName());

    }

    @Test
    public void whenSavingNewConfiguration_thenSavedConfigurationIsReturned() {
        given(configurationRepository.save(configuration))
                .willReturn(configuration);

        Configuration savedConfiguration = configurationService.saveConfiguration(configuration);

        assertThat(savedConfiguration.getId()).isEqualTo(configuration.getId());
        assertThat(savedConfiguration.getName()).isEqualTo(configuration.getName());
        assertThat(savedConfiguration.getValue()).isEqualTo(configuration.getValue());
    }

    @Test
    public void whenDeletingAConfiguration_thenConfigurationIsDeleted() {
        doNothing().when(configurationRepository).deleteById(anyString());

        configurationService.deleteConfigurationById(configuration.getId());

        verify(configurationRepository, times(1)).deleteById(anyString());
    }

    @Test
    public void whenRetrievingConfigurationById_thenConfigurationIsReturned() {

        given(configurationRepository.findById(anyString()))
                .willReturn(Optional.of(configuration));

        Configuration retrievedConfiguration = configurationService.getConfigurationById(configuration.getId());

        assertThat(retrievedConfiguration.getId()).isEqualTo(configuration.getId());
        assertThat(retrievedConfiguration.getName()).isEqualTo(configuration.getName());
        assertThat(retrievedConfiguration.getValue()).isEqualTo(configuration.getValue());

    }

    @Test(expected = ConfigurationNotFoundException.class)
    public void whenRetrievingConfigurationById_thenNotFoundException() {
        given(configurationRepository.findById(anyString()))
                .willReturn(Optional.empty());

        configurationService.getConfigurationById(configuration.getId());

    }
}