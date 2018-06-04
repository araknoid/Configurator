package com.araknoid.configurator.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ConfigurationController.class)
public class ConfigurationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConfigurationService configurationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Configuration configuration;

    @Before
    public void setUp() {
        configuration = new Configuration("server.port", "Port of the server", "8080");
    }

    @Test
    public void whenRetrievingConfiguration_thenItShouldReturnConfigurationNameAndValue() throws Exception {
        given(configurationService.getConfigurationByName(anyString()))
                .willReturn(configuration);

        mockMvc.perform(MockMvcRequestBuilders.get("/configurations?name=server.port"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(configuration.getId()))
                .andExpect(jsonPath("name").value(configuration.getName()))
                .andExpect(jsonPath("value").value(configuration.getValue()));
    }

    @Test
    public void whenRetrievingConfiguration_thenNotFound() throws Exception {
        given(configurationService.getConfigurationByName(anyString()))
                .willThrow(new ConfigurationNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/configurations?name=server.port"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNewConfiguration_whenSavingNewConfiguration_thenSavedAndLocationIsReturned() throws Exception {

        given(configurationService.saveConfiguration(any(Configuration.class)))
                .willReturn(configuration);

        String jsonBody = objectMapper.writeValueAsString(configuration);

        mockMvc.perform(MockMvcRequestBuilders.post("/configurations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void givenConfigurationId_whenDeletingConfiguration_thenDeleted() throws Exception {

        doNothing().when(configurationService).deleteConfigurationById(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/configurations/{id}", configuration.getId()))
                .andExpect(status().isNoContent());

        verify(configurationService, times(1)).deleteConfigurationById(anyString());
    }

    @Test
    public void givenConfigurationIdThatDoesNotExists_whenDeletingConfiguration_thenConfigurationNotFound() throws Exception {
        doThrow(EntityNotFoundException.class).when(configurationService).deleteConfigurationById(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/configurations/{id}", configuration.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenConfigurationId_whenRetrievingConfiguration_thenConfigurationIsReturned() throws Exception {
        given(configurationService.getConfigurationById(anyString()))
                .willReturn(configuration);

        mockMvc.perform(MockMvcRequestBuilders.get("/configurations/{id}", configuration.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(configuration.getId()))
                .andExpect(jsonPath("name").value(configuration.getName()))
                .andExpect(jsonPath("value").value(configuration.getValue()));

    }

    @Test
    public void givenConfigurationIdAndConfiguration_whenUpdatingConfiguration_thenConfigurationUpdated() throws Exception {
        Configuration updatedConfiguration = new Configuration(configuration.getId(), "Default port", "8090");
        given(configurationService.updatedConfiguration(any(Configuration.class)))
                .willReturn(updatedConfiguration);

        String jsonBody = objectMapper.writeValueAsString(configuration);

        mockMvc.perform(MockMvcRequestBuilders.put("/configurations/{id}", configuration.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(updatedConfiguration.getId()))
                .andExpect(jsonPath("name").value(updatedConfiguration.getName()))
                .andExpect(jsonPath("value").value(updatedConfiguration.getValue()));
    }
}
