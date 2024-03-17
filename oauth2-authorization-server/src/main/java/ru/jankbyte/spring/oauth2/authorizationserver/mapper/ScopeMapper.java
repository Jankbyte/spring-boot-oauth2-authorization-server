package ru.jankbyte.spring.oauth2.authorizationserver.mapper;

import org.springframework.stereotype.Component;
import ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2.ScopeInfoResponse;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Scope;

import java.util.List;
import java.util.Collection;

@Component
public class ScopeMapper {
    public Collection<ScopeInfoResponse> toScopeInfoResponse(
            Collection<Scope> scopes) {
        return scopes.stream()
            .map(this::toScopeInfoResponse)
            .toList();
    }

    public ScopeInfoResponse toScopeInfoResponse(Scope scope) {
        String name = scope.getName(),
            description = scope.getDescription();
        return new ScopeInfoResponse(name, description);
    }
}
