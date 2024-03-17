package ru.jankbyte.spring.oauth2.authorizationserver.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.security.oauth2.authorizationserver")
public record AuthorizationServerProperty(String issuerUrl, JwtProperty jwt) {
    public static record JwtProperty(String secret) {}
}
