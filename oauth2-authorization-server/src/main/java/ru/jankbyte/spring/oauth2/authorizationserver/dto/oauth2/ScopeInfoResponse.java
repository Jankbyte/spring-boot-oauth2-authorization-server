package ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ScopeInfoResponse(@JsonProperty("name") String scopeName,
    @JsonProperty("description") String scopeDescription) {
}
