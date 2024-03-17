package ru.jankbyte.spring.oauth2.authorizationserver.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import org.testcontainers.containers.PostgreSQLContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class TestContainersConfiguration {
    private static final Logger log = LoggerFactory.getLogger(
        TestContainersConfiguration.class);

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> releationalDatabaseContainer() {
        log.debug("Initializing PostgreSQL test container");
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>(
            "postgres:16.1");
        container.start();
        String jdbcUrl = container.getJdbcUrl();
        log.debug("PostgreSQL container success started with URL: {}",
            jdbcUrl);
        return container;
    }
}
