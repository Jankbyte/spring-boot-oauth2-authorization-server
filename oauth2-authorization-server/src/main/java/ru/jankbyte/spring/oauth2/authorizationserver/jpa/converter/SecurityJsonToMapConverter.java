package ru.jankbyte.spring.oauth2.authorizationserver.jpa.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

import java.util.List;
import java.util.Map;

/**
 * Converter from JSON-string to Map object (and reverse).
 * @author Jankbyte
 * @since 1.0
 */
@Converter
public class SecurityJsonToMapConverter
        implements AttributeConverter<Map<String,Object>, String> {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger log = LoggerFactory.getLogger(
        SecurityJsonToMapConverter.class);

    public SecurityJsonToMapConverter() {
        ClassLoader classLoader = SecurityJsonToMapConverter.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(
            classLoader);
        objectMapper.registerModules(securityModules);
        Module oauth2Module = new OAuth2AuthorizationServerJackson2Module();
        objectMapper.registerModule(oauth2Module);
    }

    @Override
    public String convertToDatabaseColumn(Map<String, Object> jsonMap) {
        try {
            return objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException(ex);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>(){});
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException(ex);
        }
    }
}
