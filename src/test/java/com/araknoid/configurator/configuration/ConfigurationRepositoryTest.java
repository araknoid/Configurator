package com.araknoid.configurator.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ConfigurationRepositoryTest {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void whenFindingConfigurationByName_thenConfigurationDetailsAreReturned() {

        Configuration savedConfiguration = entityManager.persistAndFlush(new Configuration("server.port", "8080"));

        Optional<Configuration> configuration = configurationRepository.findByName("server.port");

        assertThat(configuration.get().getName()).isEqualTo(savedConfiguration.getName());
        assertThat(configuration.get().getValue()).isEqualTo(savedConfiguration.getValue());
    }
}