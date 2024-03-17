package ru.jankbyte.spring.oauth2.authorizationserver.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import ru.jankbyte.spring.oauth2.authorizationserver.property.ClientCredential;
import org.springframework.boot.context.properties.ConfigurationProperties;

@TestConfiguration(proxyBeanMethods = false)
public class TestClientsPropertiesConfiguration {
    @Bean
    @ConfigurationProperties("clients.jwt-client-with-consents")
    public ClientCredential jwtWithConsentsClient() {
        return new ClientCredential();
    }

    @Bean
    @ConfigurationProperties("clients.client-credentials-client")
    public ClientCredential clientCredentialsClient() {
        return new ClientCredential();
    }

    @Bean
    @ConfigurationProperties("clients.device-authorization-test-client")
    public ClientCredential deviceAuthorizationTestClient() {
        return new ClientCredential();
    }

    @Bean
    @ConfigurationProperties("clients.pkce-client")
    public ClientCredential pkceClientCredential() {
        return new ClientCredential();
    }
}
