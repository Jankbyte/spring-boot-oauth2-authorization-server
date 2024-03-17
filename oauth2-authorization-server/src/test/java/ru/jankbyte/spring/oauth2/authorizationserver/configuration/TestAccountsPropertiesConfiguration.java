package ru.jankbyte.spring.oauth2.authorizationserver.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import ru.jankbyte.spring.oauth2.authorizationserver.property.AccountCredential;
import org.springframework.boot.context.properties.ConfigurationProperties;

@TestConfiguration(proxyBeanMethods = false)
public class TestAccountsPropertiesConfiguration {
    @Bean
    @ConfigurationProperties("accounts.default-credentials")
    public AccountCredential accountCredential() {
        return new AccountCredential();
    }
}
