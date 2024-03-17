package ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2;

import java.util.Collection;

public record ConsentDisplayInfoResponse(
    String clientName, String clientLogoUrl,
    Collection<ScopeInfoResponse> scopesToAuthorize,
    Collection<ScopeInfoResponse> scopesPreviouslyAuthorized) {
}
