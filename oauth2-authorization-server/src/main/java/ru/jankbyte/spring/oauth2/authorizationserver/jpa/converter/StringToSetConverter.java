package ru.jankbyte.spring.oauth2.authorizationserver.jpa.converter;

import jakarta.persistence.AttributeConverter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringToSetConverter
        implements AttributeConverter<Set<String>, String> {
    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return null;
        }
        return String.join(DELIMITER, strings);
    }

    @Override
    public Set<String> convertToEntityAttribute(String delimiterArray) {
        if (delimiterArray == null) {
            return null;
        }
        String[] elements = delimiterArray.split(DELIMITER);
        return Stream.of(elements)
            .collect(Collectors.toSet());
    }
}
