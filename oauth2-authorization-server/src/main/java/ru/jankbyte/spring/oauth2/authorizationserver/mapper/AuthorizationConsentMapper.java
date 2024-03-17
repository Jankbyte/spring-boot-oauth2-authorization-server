package ru.jankbyte.spring.oauth2.authorizationserver.mapper;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.stereotype.Component;
import java.util.Collection;
import java.util.UUID;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthorizationConsent;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.ClientAccount;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Scope;

@Component
public class AuthorizationConsentMapper {
    public OAuth2AuthorizationConsent toOAuth2AuthorizationConsent(
            AuthorizationConsent consent) {
        Client client = consent.getClient();
        Account account = consent.getAccount();
        String consentId = client.getId().toString(),
            accountName = account.getName();
        OAuth2AuthorizationConsent.Builder builder =
            OAuth2AuthorizationConsent.withId(consentId, accountName);
        consent.getScopes().stream()
            .map(Scope::getName)
            .forEach(builder::scope);
        return builder.build();
    }

    public AuthorizationConsent toAuthorizationConsent(
            OAuth2AuthorizationConsent oauth2consent) {
        String accountName = oauth2consent.getPrincipalName();
        Account account = Account.builder()
            .name(accountName)
            .build();
        String clientId = oauth2consent.getRegisteredClientId();
        UUID clientUuid = UUID.fromString(clientId);
        ClientAccount clientAccount = ClientAccount.clientAccountBuilder()
            .id(clientUuid)
            .build();
        Client client = Client.builder()
            .account(clientAccount)
            .id(clientUuid)
            .build();
        Collection<Scope> scopes = oauth2consent.getScopes().stream()
            .map(Scope::of)
            .toList();
        return AuthorizationConsent.builder()
            .account(account)
            .client(client)
            .scopes(scopes)
            .build();
    }
}
