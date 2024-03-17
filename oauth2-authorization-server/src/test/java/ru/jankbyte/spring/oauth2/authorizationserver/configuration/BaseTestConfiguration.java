package ru.jankbyte.spring.oauth2.authorizationserver.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.ZoneOffset;
import java.time.Instant;

@TestConfiguration
public class BaseTestConfiguration {
    @Bean
    public Clock clock() {
        Instant fixedInstant = Instant.parse("2012-12-12T00:00:00.00Z");
        return Clock.fixed(fixedInstant, ZoneOffset.UTC);
    }
}
