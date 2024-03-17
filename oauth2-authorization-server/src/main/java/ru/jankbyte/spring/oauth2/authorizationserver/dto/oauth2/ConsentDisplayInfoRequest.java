package ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2;

import java.util.Collection;

public record ConsentDisplayInfoRequest(String clientId, String accountName,
    Collection<String> requestedScopes) {
}
