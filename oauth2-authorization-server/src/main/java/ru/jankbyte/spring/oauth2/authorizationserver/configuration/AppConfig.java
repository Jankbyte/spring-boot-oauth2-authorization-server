package ru.jankbyte.spring.oauth2.authorizationserver.configuration;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import ru.jankbyte.spring.oauth2.authorizationserver.property.AuthorizationServerProperty;

@Configuration(proxyBeanMethods = false)
@ConfigurationPropertiesScan(basePackageClasses = {
    AuthorizationServerProperty.class
})
public class AppConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
