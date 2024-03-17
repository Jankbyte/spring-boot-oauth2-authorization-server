package ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2;

import ru.jankbyte.spring.oauth2.authorizationserver.mapper.ClientMapper;
import ru.jankbyte.spring.oauth2.authorizationserver.mapper.ScopeMapper;
import ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2.ClientService;
import ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2.ConsentDisplayInfoRequest;
import ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2.ConsentDisplayInfoResponse;
import ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2.ScopeInfoResponse;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthorizationConsent;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Scope;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;

import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toSet;

@Service
public class AuthorizationConsentServiceFacade {
    private final AuthorizationConsentService consentService;
    private final ClientService clientService;
    private final ScopeMapper scopeMapper;

    public AuthorizationConsentServiceFacade(ClientService clientService,
            AuthorizationConsentService consentService,
            ScopeMapper scopeMapper) {
        this.consentService = consentService;
        this.clientService = clientService;
        this.scopeMapper = scopeMapper;
    }

    @Transactional(readOnly = true)
    public ConsentDisplayInfoResponse getConsentDisplayInfoResponse(
            ConsentDisplayInfoRequest request) {
        String clientId = request.clientId();
        String accountName = request.accountName();
        Collection<String> requestedScopes = request.requestedScopes();
        Client client = clientService.getClientByAccountName(clientId);
        UUID clientUuid = client.getId();
        Optional<AuthorizationConsent> consentWrapper =
            consentService.getConsentById(clientUuid, accountName);
        Set<ScopeInfoResponse> scopesToAuthorize = new HashSet<>();
        Set<ScopeInfoResponse> scopesPreviouslyAuthorized = new HashSet<>();
        Collection<Scope> clientScopes = client.getScopes();
        if (consentWrapper.isEmpty()) {
            clientScopes.forEach(scope -> {
                String scopeName = scope.getName();
                if (scopeName.equals(OidcScopes.OPENID) ||
                        !requestedScopes.contains(scopeName)) {
                    return;
                }
                String scopeDescription = scope.getDescription();
                ScopeInfoResponse response = new ScopeInfoResponse(
                    scopeName, scopeDescription);
                scopesToAuthorize.add(response);
            });
        } else {
            AuthorizationConsent consent = consentWrapper.get();
            Collection<Scope> consentScopes = consent.getScopes();
            requestedScopes.forEach(scope -> {
                if (scope.equals(OidcScopes.OPENID)) {
                    return;
                }
                Scope targetScope = getScopeFromCollectionByName(
                    consentScopes, scope);
                boolean isScopePreviouslyAuthorized = targetScope != null;
                if (isScopePreviouslyAuthorized) {
                    ScopeInfoResponse scopeInfoResponse =
                        scopeMapper.toScopeInfoResponse(targetScope);
                    scopesPreviouslyAuthorized.add(scopeInfoResponse);
                } else {
                    targetScope = getScopeFromCollectionByName(
                        clientScopes, scope);
                    ScopeInfoResponse scopeInfoResponse =
                        scopeMapper.toScopeInfoResponse(targetScope);
                    scopesToAuthorize.add(scopeInfoResponse);
                }
            });
        }
        String logoUrl = client.getLogoUrl();
        String clientLogoUrl = logoUrl != null ? logoUrl :
            ClientMapper.CLIENT_LOGO_DEFAULT_URL;
        return new ConsentDisplayInfoResponse(client.getName(),
            clientLogoUrl, scopesToAuthorize, scopesPreviouslyAuthorized);
    }

    private static Scope getScopeFromCollectionByName(
            Collection<Scope> scopes, String scopeName) {
        for (Scope scope : scopes) {
            String collectionScopeName = scope.getName();
            if (collectionScopeName.equals(scopeName)) {
                return scope;
            }
        }
        return null;
    }

    public Collection<String> convertScopeParamToCollection(
            String scope) {
        return Stream.of(scope.split("\\s+"))
            .collect(toSet());
    }
}
