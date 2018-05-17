package com.araknoid.configurator.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
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

    @Test
    public void whenRetrievingConfiguration_thenItShouldReturnConfigurationNameAndValue() throws Exception {
        given(configurationService.getConfigurationByName(anyString()))
                .willReturn(new Configuration("server.port", "8080"));

        mockMvc.perform(MockMvcRequestBuilders.get("/configurations/server.port"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value("server.port"))
                .andExpect(jsonPath("value").value("8080"));
    }

    @Test
    public void whenRetrievingConfiguration_thenNotFound() throws Exception {
        given(configurationService.getConfigurationByName(anyString()))
                .willThrow(new ConfigurationNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/configurations/server.port"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void givenNewConfiguration_whenSavingNewConfiguration_thenSavedAndLocationIsReturned() throws Exception {
        Configuration configuration = new Configuration("project", "configurator");

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
}
