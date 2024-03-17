package ru.jankbyte.spring.oauth2.authorizationserver.jpa.converter;

import jakarta.persistence.AttributeConverter;
import java.time.Duration;

public class DurationToMillisConverter
        implements AttributeConverter<Duration, Long> {
    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        return duration.toMillis();
    }

    @Override
    public Duration convertToEntityAttribute(Long millis) {
        return Duration.ofMillis(millis);
    }
}
