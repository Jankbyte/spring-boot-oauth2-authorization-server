package ru.jankbyte.spring.oauth2.authorizationserver.component;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

import ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2.ClientServiceFacade;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.*;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.GrantType.GrantTypeName;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthenticationMethod.AuthenticationMethodName;
import ru.jankbyte.spring.oauth2.authorizationserver.property.ClientCredential;

import java.util.Collection;
import java.util.UUID;

import java.time.OffsetDateTime;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestComponent
public class TestClientsCreator implements DatabaseCreator {
    private static final Logger log = LoggerFactory.getLogger(
        TestClientsCreator.class);
    private final Collection<ClientCredential> clientCredentials;
    private final ClientServiceFacade clientServiceFacade;

    public TestClientsCreator(ClientServiceFacade clientServiceFacade,
        Collection<ClientCredential> clientCredentials) {
        this.clientCredentials = clientCredentials;
        this.clientServiceFacade = clientServiceFacade;
    }

    @Override
    public void create() {
        log.debug("Creating test clients");
        clientCredentials.stream()
            .map(TestClientsCreator::toClient)
            .forEach(clientServiceFacade::createNewClientAndEncodePassword);
    }
    
    private static Client toClient(ClientCredential credentials) {
        log.debug("Converting ClientCredentials to Client: {}", credentials);
        String secretValue = credentials.getClientSecret();
        AccessTokenType tokenType = AccessTokenType.of(
            credentials.getAccessTokenTypeValue());
        Collection<GrantType> grantTypes = credentials.getGrantTypes()
            .stream()
            .map(GrantType::of)
            .toList();
        Collection<AuthenticationMethod> methods =
            credentials.getAuthenticationMethods().stream()
                .map(AuthenticationMethod::of)
                .toList();
        Collection<Scope> scopes = credentials.getScopes().stream()
            .map(Scope::of)
            .toList();
        ClientAccount account = ClientAccount.clientAccountBuilder()
            .id(UUID.randomUUID())
            .name(credentials.getClientId())
            .password(secretValue)
            .build();
        return Client.withName(credentials.getClientName())
            .account(account)
            .authorizationCodeTimeToLive(Duration.ofMinutes(5))
            .accessTokenTimeToLive(Duration.ofMinutes(15))
            .refreshTokenTimeToLive(Duration.ofDays(30))
            .deviceCodeTimeToLive(Duration.ofMinutes(5))
            .issuedAt(OffsetDateTime.now())
            .accessTokenType(tokenType)
            .requiredProofKey(credentials.isRequiredProofKey())
            .requiredAuthorizationConsent(
                credentials.isRequireAuthorizationConsent())
            .reuseRefreshTokens(credentials.isReuseRefreshTokens())
            .scopes(scopes)
            .grantTypes(grantTypes)
            .authenticationMethods(methods)
            .redirectUrls(credentials.getRedirectUrls())
            .postLogoutUrls(credentials.getPostlogoutUrls())
            .build();
    }
}
