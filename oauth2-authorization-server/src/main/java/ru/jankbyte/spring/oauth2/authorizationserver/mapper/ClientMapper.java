package ru.jankbyte.spring.oauth2.authorizationserver.mapper;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Component;

import static java.time.ZoneOffset.UTC;
import java.time.Clock;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthenticationMethod.AuthenticationMethodName;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.GrantType.GrantTypeName;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.*;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType.TokenTypeValue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public final class ClientMapper {
    private static final Logger log = LoggerFactory.getLogger(
        ClientMapper.class);

    private static final Collection<String> MAC_ALGORITHMS =
        Stream.of(MacAlgorithm.values()).map(JwsAlgorithm::getName)
            .toList();
    private static final Collection<String> SIGNATURE_ALGORITHMS =
        Stream.of(SignatureAlgorithm.values()).map(JwsAlgorithm::getName)
            .toList();

    public static final String CLIENT_LOGO_DEFAULT_URL =
        "/content/images/default-client-icon.png";

    public Client toClient(RegisteredClient registeredClient) {
        String id = registeredClient.getId();
        UUID uuid = id != null ? UUID.fromString(id) : null;
        Instant clientIssuedAt = registeredClient.getClientIdIssuedAt();
        OffsetDateTime issuedAt = clientIssuedAt != null ?
            clientIssuedAt.atOffset(UTC) : null;
        Instant clientSecretExpiresAt =
            registeredClient.getClientSecretExpiresAt();
        OffsetDateTime secretExpiresAt = clientSecretExpiresAt != null ?
            clientSecretExpiresAt.atOffset(UTC) : null;
        ClientAccount clientAccount = ClientAccount.clientAccountBuilder()
            .id(uuid)
            .name(registeredClient.getClientId())
            .password(registeredClient.getClientSecret())
            .passwordExpiresAt(secretExpiresAt)
            .build();
        Client.ClientBuilder builder = Client.withName(
                registeredClient.getClientName())
            .id(uuid)
            .account(clientAccount)
            .issuedAt(issuedAt)
            .redirectUrls(registeredClient.getRedirectUris())
            .postLogoutUrls(
                registeredClient.getPostLogoutRedirectUris());
        ClientSettings clientSettings = registeredClient.getClientSettings();
        JwsAlgorithm jwsAlgorithm =
            clientSettings.getTokenEndpointAuthenticationSigningAlgorithm();
        if (jwsAlgorithm != null) {
            String tokenAlgorithm = jwsAlgorithm.getName();
            builder.tokenAlgorithm(tokenAlgorithm);
        }
        builder.jwkSetUrl(clientSettings.getJwkSetUrl())
            .requiredProofKey(clientSettings.isRequireProofKey())
            .requiredAuthorizationConsent(
                clientSettings.isRequireAuthorizationConsent());
        TokenSettings tokenSettings = registeredClient.getTokenSettings();
        OAuth2TokenFormat clientTokenTypeFormat =
            tokenSettings.getAccessTokenFormat();
        TokenTypeValue tokenType = resolveTokenTypeValue(
            clientTokenTypeFormat);
        AccessTokenType accessTokenType = AccessTokenType.of(tokenType);
        builder.authorizationCodeTimeToLive(
                tokenSettings.getAuthorizationCodeTimeToLive())
            .accessTokenTimeToLive(tokenSettings.getAccessTokenTimeToLive())
            .refreshTokenTimeToLive(
                tokenSettings.getRefreshTokenTimeToLive())
            .deviceCodeTimeToLive(tokenSettings.getDeviceCodeTimeToLive())
            .reuseRefreshTokens(tokenSettings.isReuseRefreshTokens())
            .accessTokenType(accessTokenType);
        Set<AuthorizationGrantType> authenticationGrantTypes =
            registeredClient.getAuthorizationGrantTypes();
        Collection<GrantType> grantTypes = resolveGrantTypes(
            authenticationGrantTypes).stream()
                .map(GrantType::of)
                .toList();
        builder.grantTypes(grantTypes);
        Set<ClientAuthenticationMethod> authenticationMethods =
            registeredClient.getClientAuthenticationMethods();
        Collection<AuthenticationMethod> methods =
            resolveAuthenticationMethods(authenticationMethods).stream()
                .map(AuthenticationMethod::of)
                .toList();
        builder.authenticationMethods(methods);
        Collection<Scope> scopes = registeredClient.getScopes()
            .stream()
            .map(Scope::of)
            .toList();
        builder.scopes(scopes);
        return builder.build();
    }

    public RegisteredClient toRegisteredClient(Client client) {
        ClientAccount clientAccount = client.getAccount();
        String clientId = client.getId().toString();
        Instant issuedAt = client.getIssuedAt().toInstant();
        RegisteredClient.Builder builder = RegisteredClient.withId(clientId)
            .clientId(clientAccount.getName())
            .clientSecret(clientAccount.getPassword())
            .clientName(client.getName())
            .clientIdIssuedAt(issuedAt)
            .redirectUris(consumer ->
                consumer.addAll(client.getRedirectUrls()));
        Collection<String> logoutUris = client.getPostLogoutUrls();
        if (logoutUris != null && !logoutUris.isEmpty()) {
            builder.postLogoutRedirectUris(consumer ->
                consumer.addAll(client.getPostLogoutUrls()));
        }
        OffsetDateTime expiresAt = clientAccount.getPasswordExpiresAt();
        Instant secretExpiresAt = expiresAt != null ?
            expiresAt.toInstant() : null;
        builder.clientSecretExpiresAt(secretExpiresAt);
        TokenTypeValue tokenType = client.getAccessTokenType().getValue();
        OAuth2TokenFormat tokenFormat = switch (tokenType) {
            case OPAQUE -> OAuth2TokenFormat.REFERENCE;
            case JWT -> OAuth2TokenFormat.SELF_CONTAINED;
        };
        ClientSettings.Builder clientSettingsBuilder = ClientSettings.builder();
        TokenSettings.Builder tokenSettingsBuilder = TokenSettings.builder();
        String tokenAlgorithm = client.getTokenAlgorithm();
        if (tokenAlgorithm != null) {
            JwsAlgorithm algorithm = resolveJwsAlgorithm(tokenAlgorithm);
            // TODO: fix casting algorithm
            tokenSettingsBuilder.idTokenSignatureAlgorithm(
                (SignatureAlgorithm) algorithm);
            clientSettingsBuilder.tokenEndpointAuthenticationSigningAlgorithm(
                algorithm);
        }
        tokenSettingsBuilder.accessTokenFormat(tokenFormat)
            .authorizationCodeTimeToLive(client.getAuthorizationCodeTimeToLive())
            .accessTokenTimeToLive(client.getAccessTokenTimeToLive())
            .refreshTokenTimeToLive(client.getRefreshTokenTimeToLive())
            .deviceCodeTimeToLive(client.getDeviceCodeTimeToLive())
            .reuseRefreshTokens(client.canReuseRefreshTokens());
        TokenSettings tokenSettings = tokenSettingsBuilder.build();
        builder.tokenSettings(tokenSettings);
        String jwtSetUrl = client.getJwkSetUrl();
        if (jwtSetUrl != null) {
            clientSettingsBuilder.jwkSetUrl(jwtSetUrl);
        }
        clientSettingsBuilder.requireProofKey(client.isRequiredProofKey())
            .requireAuthorizationConsent(
                client.isRequiredAuthorizationConsent())
            .build();
        ClientSettings clientSettings = clientSettingsBuilder.build();
        builder.clientSettings(clientSettings);
        Collection<AuthorizationGrantType> grantTypes =
            resolveGrantTypes(client.getGrantTypes());
        builder.authorizationGrantTypes(consumer ->
            consumer.addAll(grantTypes));
        Collection<ClientAuthenticationMethod> authenticationMethods =
            resolveAuthenticationMethods(client.getAuthenticationMethods());
        builder.clientAuthenticationMethods(consumer ->
            consumer.addAll(authenticationMethods));
        Collection<String> scopes = client.getScopes().stream()
            .map(Scope::getName)
            .toList();
        builder.scopes(consumer -> consumer.addAll(scopes));
        return builder.build();
    }

    private static Collection<AuthenticationMethodName>
        resolveAuthenticationMethods(
            Set<ClientAuthenticationMethod> authenticationMethods) {
        return authenticationMethods.stream()
            .map(ClientMapper::resolveAuthenticationMethod)
            .toList();
    }

    private static Collection<ClientAuthenticationMethod>
        resolveAuthenticationMethods(
            Collection<AuthenticationMethod> authenticationMethods) {
        return authenticationMethods.stream()
            .map(method -> resolveAuthenticationMethod(method.getName()))
            .toList();
    }

    private static AuthenticationMethodName resolveAuthenticationMethod(
            ClientAuthenticationMethod method) {
        AuthenticationMethodName name;
        if (method.equals(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)) {
            name = AuthenticationMethodName.CLIENT_SECRET_BASIC;
        } else if (method.equals(ClientAuthenticationMethod.CLIENT_SECRET_POST)) {
            name = AuthenticationMethodName.CLIENT_SECRET_POST;
        } else if (method.equals(ClientAuthenticationMethod.CLIENT_SECRET_JWT)) {
            name = AuthenticationMethodName.CLIENT_SECRET_JWT;
        } else if (method.equals(ClientAuthenticationMethod.PRIVATE_KEY_JWT)) {
            name = AuthenticationMethodName.PRIVATE_KEY_JWT;
        } else if (method.equals(ClientAuthenticationMethod.NONE)) {
            name  = AuthenticationMethodName.NONE;
        } else {
            String message = "Unknown authentication method: %s"
                .formatted(method);
            throw new IllegalArgumentException(message);
        }
        return name;
    }

    private static ClientAuthenticationMethod resolveAuthenticationMethod(
            AuthenticationMethodName methodName) {
        return switch (methodName) {
            case CLIENT_SECRET_BASIC ->
                ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
            case CLIENT_SECRET_POST ->
                ClientAuthenticationMethod.CLIENT_SECRET_POST;
            case CLIENT_SECRET_JWT ->
                ClientAuthenticationMethod.CLIENT_SECRET_JWT;
            case PRIVATE_KEY_JWT ->
                ClientAuthenticationMethod.PRIVATE_KEY_JWT;
            case NONE -> ClientAuthenticationMethod.NONE;
            default -> throw new IllegalArgumentException("Unknown method name: %s"
                .formatted(methodName));
        };
    }

    private static TokenTypeValue resolveTokenTypeValue(OAuth2TokenFormat tokenFormat) {
        TokenTypeValue tokenType;
        if (tokenFormat.equals(OAuth2TokenFormat.REFERENCE)) {
            tokenType = TokenTypeValue.OPAQUE;
        } else if (tokenFormat.equals(OAuth2TokenFormat.SELF_CONTAINED)) {
            tokenType = TokenTypeValue.JWT;
        } else {
            String message = "Unknown access token type: %s"
                .formatted(tokenFormat);
            throw new IllegalArgumentException(message);
        }
        return tokenType;
    }

    private static Collection<GrantTypeName> resolveGrantTypes(
        Set<AuthorizationGrantType> grantTypes) {
        return grantTypes.stream()
            .map(ClientMapper::resolveGrantType)
            .toList();
    }

    private static GrantTypeName resolveGrantType(
            AuthorizationGrantType grantType) {
        GrantTypeName name;
        if (grantType.equals(AuthorizationGrantType.JWT_BEARER)) {
            name = GrantTypeName.JWT_BEARER;
        } else if (grantType.equals(AuthorizationGrantType.CLIENT_CREDENTIALS)) {
            name = GrantTypeName.CLIENT_CREDENTIALS;
        } else if (grantType.equals(AuthorizationGrantType.REFRESH_TOKEN)) {
            name = GrantTypeName.REFRESH_TOKEN;
        } else if (grantType.equals(AuthorizationGrantType.AUTHORIZATION_CODE)) {
            name = GrantTypeName.AUTHORIZATION_CODE;
        } else if (grantType.equals(AuthorizationGrantType.DEVICE_CODE)) {
            name = GrantTypeName.DEVICE_CODE;
        } else {
            String message = "Unknown grant type: %s"
                .formatted(grantType);
            throw new IllegalArgumentException(message);
        }
        return name;
    }

    private static Collection<AuthorizationGrantType> resolveGrantTypes(
        Collection<GrantType> grantTypes) {
        return grantTypes.stream()
            .map(grantType -> resolveGrantType(grantType.getName()))
            .toList();
    }

    private static AuthorizationGrantType resolveGrantType(GrantTypeName name) {
        return switch (name) {
            case JWT_BEARER ->AuthorizationGrantType.JWT_BEARER;
            case CLIENT_CREDENTIALS ->
                AuthorizationGrantType.CLIENT_CREDENTIALS;
            case REFRESH_TOKEN -> AuthorizationGrantType.REFRESH_TOKEN;
            case AUTHORIZATION_CODE ->
                AuthorizationGrantType.AUTHORIZATION_CODE;
            case DEVICE_CODE -> AuthorizationGrantType.DEVICE_CODE;
            default -> throw new IllegalArgumentException(
                "Unknown grant type: %s".formatted(name));
        };
    }

    private static JwsAlgorithm resolveJwsAlgorithm(String algorithm) {
        if (MAC_ALGORITHMS.contains(algorithm)) {
            return MacAlgorithm.from(algorithm);
        } else if (SIGNATURE_ALGORITHMS.contains(algorithm)) {
            return SignatureAlgorithm.from(algorithm);
        } else {
            String message = "Unknown algorithm: %s"
                .formatted(algorithm);
            throw new IllegalArgumentException(message);
        }
    }
}
