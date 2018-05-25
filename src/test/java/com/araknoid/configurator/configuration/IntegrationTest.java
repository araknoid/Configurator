package com.araknoid.configurator.configuration;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Test
    public void whenRetrievingConfiguration_thenItShouldReturnConfigurationNameAndValue() {
        Configuration configuration = new Configuration("server.port", "8080");
        configurationRepository.save(configuration);

        ResponseEntity<Configuration> response = restTemplate.getForEntity("/configurations?name={name}", Configuration.class, "server.port");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(configuration.getName());
        assertThat(response.getBody().getValue()).isEqualTo(configuration.getValue());
    }

    @Test
    public void givenConfiguration_whenSavingNewConfiguration_thenSaved() {
        ResponseEntity<Configuration> postResponse = restTemplate.postForEntity("/configurations", new Configuration("project", "configurator"), Configuration.class);

        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getHeaders().getLocation()).isNotNull();
    }

    @Test
    public void givenConfiguration_whenDeletingConfiguration_thenDeleted() {
        Configuration configuration = new Configuration("server.port", "8080");
        Configuration savedConfiguration = configurationRepository.save(configuration);

        UriComponents deleteURI = UriComponentsBuilder.fromUriString("/configurations/{id}")
                .buildAndExpand(savedConfiguration.getId());

        RequestEntity<Void> deleteRequest = RequestEntity.delete(deleteURI.toUri()).build();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(deleteRequest, Void.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    public void whenRetrievingConfigurationByID_thenItShouldReturnConfigurationNameAndValue() {
        Configuration configuration = new Configuration("server.name", "localhost");
        Configuration savedConfiguration = configurationRepository.save(configuration);

        UriComponents selectByIdUri = UriComponentsBuilder.fromUriString("/configurations/{id}")
                .buildAndExpand(savedConfiguration.getId());

        ResponseEntity<Configuration> response = restTemplate.getForEntity(selectByIdUri.toUri(), Configuration.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(configuration.getName());
        assertThat(response.getBody().getValue()).isEqualTo(configuration.getValue());
    }

    @After
    public void tearDown() {
        configurationRepository.deleteAll();
    }
}
