package ru.jankbyte.spring.oauth2.authorizationserver.configuration;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;

import ru.jankbyte.spring.oauth2.authorizationserver.component.DatabaseCreatorsExecutor;

import java.io.File;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestConfiguration(proxyBeanMethods = false)
@Import({
    TestClientsPropertiesConfiguration.class,
    TestAccountsPropertiesConfiguration.class,
    DatabaseCreatorsExecutor.class
})
public class AutomationTestConfiguration {
    @Bean
    public RestClient oauth2RestClient(@Value("${baseUrl}") String baseUrl) {
        return RestClient.create(baseUrl);
    }
}
