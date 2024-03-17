package ru.jankbyte.spring.oauth2.authorizationserver.mapper;

import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Token;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2.AuthorizationResponse;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization.AuthorizationBuilder;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.*;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token.*;
import ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2.AuthorizationCustomizer;

import static java.time.ZoneOffset.UTC;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.Set;

@Component
public class AuthorizationMapper {
    private final ClientMapper clientMapper;
    private final AuthorizationCustomizer authorizationCustomizer;

    public AuthorizationMapper(ClientMapper clientMapper,
            AuthorizationCustomizer authorizationCustomizer) {
        this.clientMapper = clientMapper;
        this.authorizationCustomizer = authorizationCustomizer;
    }

    public Collection<AuthorizationResponse> toAuthorizationResponses(
            Collection<Authorization> authorizations) {
        return authorizations.stream()
            .map(this::toAuthorizationResponse)
            .toList();
    }

    public AuthorizationResponse toAuthorizationResponse(
            Authorization authorization) {
        AuthorizationCode code = authorization.getAuthorizationCode();
        Client client = authorization.getClient();
        ClientAccount clientAccount = client.getAccount();
        return new AuthorizationResponse(authorization.getId(),
            authorization.getAuthenticatedIp(), code.getIssuedAt(),
            clientAccount.getName(), client.getName());
    }

    public Authorization toAuthorization(OAuth2Authorization oauth2Authorization) {
        String authorizationId = oauth2Authorization.getId();
        UUID id = UUID.fromString(authorizationId);
        String principalName = oauth2Authorization.getPrincipalName();
        Account account = Account.builder()
            .name(principalName)
            .build();
        String registeredClientId = oauth2Authorization.getRegisteredClientId();
        UUID clientId = UUID.fromString(registeredClientId);
        ClientAccount clientAccount = ClientAccount.clientAccountBuilder()
            .id(clientId)
            .build();
        Client client = Client.builder()
            .account(clientAccount)
            .id(clientId)
            .build();
        AuthorizationBuilder builder = Authorization.withId(id)
            .client(client)
            .account(account);
        Set<String> scopes = oauth2Authorization.getAuthorizedScopes();
        builder.scopes(scopes);
        AuthorizationGrantType grantType = oauth2Authorization.getAuthorizationGrantType();
        String grantTypeValue = grantType.getValue();
        builder.grantType(grantTypeValue);
        Map<String, Object> attributes = oauth2Authorization.getAttributes();
        builder.attributes(attributes);
        String state = oauth2Authorization.getAttribute(
            OAuth2ParameterNames.STATE);
        builder.state(state);
        Token<OAuth2AuthorizationCode> authorizationCodeToken =
            oauth2Authorization.getToken(OAuth2AuthorizationCode.class);
        if (authorizationCodeToken != null) {
            OAuth2AuthorizationCode code = authorizationCodeToken.getToken();
            AuthorizationCode authorizationCode =
                AuthorizationCode.withValue(code.getTokenValue())
                    .expiresAt(code.getExpiresAt().atOffset(UTC))
                    .issuedAt(code.getIssuedAt().atOffset(UTC))
                    .metadata(authorizationCodeToken.getMetadata())
                    .build();
            builder.authorizationCode(authorizationCode);
        }
        Token<OAuth2AccessToken> accessTokenWrapper =
            oauth2Authorization.getAccessToken();
        if (accessTokenWrapper != null) {
            OAuth2AccessToken token = accessTokenWrapper.getToken();
            TokenType tokenType = token.getTokenType();
            AccessToken accessToken = AccessToken.withValue(token.getTokenValue())
                .tokenType(tokenType.getValue())
                .scopes(token.getScopes())
                .issuedAt(token.getIssuedAt().atOffset(UTC))
                .expiresAt(token.getExpiresAt().atOffset(UTC))
                .metadata(accessTokenWrapper.getMetadata())
                .build();
            builder.accessToken(accessToken);
        }
        Token<OAuth2RefreshToken> refreshTokenWrapper =
            oauth2Authorization.getRefreshToken();
        if (refreshTokenWrapper != null) {
            OAuth2RefreshToken token = refreshTokenWrapper.getToken();
            RefreshToken refreshToken = RefreshToken.withValue(token.getTokenValue())
                .expiresAt(token.getExpiresAt().atOffset(UTC))
                .issuedAt(token.getIssuedAt().atOffset(UTC))
                .metadata(refreshTokenWrapper.getMetadata())
                .build();
            builder.refreshToken(refreshToken);
        }
        Token<OidcIdToken> oidcIdTokenWrapper =
            oauth2Authorization.getToken(OidcIdToken.class);
        if (oidcIdTokenWrapper != null) {
            OidcIdToken oidcIdToken = oidcIdTokenWrapper.getToken();
            OidcToken token = OidcToken.withValue(oidcIdToken.getTokenValue())
                .metadata(oidcIdTokenWrapper.getMetadata())
                .issuedAt(oidcIdToken.getIssuedAt().atOffset(UTC))
                .expiresAt(oidcIdToken.getExpiresAt().atOffset(UTC))
                .build();
            builder.oidcToken(token);
        }
        Token<OAuth2UserCode> userCodeWrapper =
            oauth2Authorization.getToken(OAuth2UserCode.class);
        if (userCodeWrapper != null) {
            OAuth2UserCode code = userCodeWrapper.getToken();
            UserCode userCode = UserCode.withValue(code.getTokenValue())
                .metadata(userCodeWrapper.getMetadata())
                .issuedAt(code.getIssuedAt().atOffset(UTC))
                .expiresAt(code.getExpiresAt().atOffset(UTC))
                .build();
            builder.userCode(userCode);
        }
        Token<OAuth2DeviceCode> deviceCodeWrapper =
            oauth2Authorization.getToken(OAuth2DeviceCode.class);
        if (deviceCodeWrapper != null) {
            OAuth2DeviceCode code = deviceCodeWrapper.getToken();
            DeviceCode deviceCode = DeviceCode.withValue(code.getTokenValue())
                .metadata(deviceCodeWrapper.getMetadata())
                .issuedAt(code.getIssuedAt().atOffset(UTC))
                .expiresAt(code.getExpiresAt().atOffset(UTC))
                .build();
            builder.deviceCode(deviceCode);
        }
        return builder.build();
    }

    public OAuth2Authorization toOAuth2Authorization(
            Authorization authorization) {
        Client client = authorization.getClient();
        ClientAccount clientAccount = client.getAccount();
        Account account = authorization.getAccount();
        RegisteredClient registeredClient = clientMapper.toRegisteredClient(client);
        String principalName = account.getName();
        AuthorizationGrantType grantType = resolveAuthorizationGrantType(
            authorization.getGrantType());
        OAuth2Authorization.Builder builder = OAuth2Authorization
            .withRegisteredClient(registeredClient)
            .authorizationGrantType(grantType)
            .principalName(principalName)
            .authorizedScopes(authorization.getScopes())
            .attributes(attrs -> attrs.putAll(authorization.getAttributes()))
            .id(authorization.getId().toString());
        String state = authorization.getState();
        if (StringUtils.hasText(state)) {
            builder.attribute(OAuth2ParameterNames.STATE, state);
        }
        AuthorizationCode authorizationCode = authorization.getAuthorizationCode();
        if (authorizationCode != null && StringUtils.hasText(authorizationCode.getValue())) {
            OAuth2AuthorizationCode code = new OAuth2AuthorizationCode(
                authorizationCode.getValue(),
                authorizationCode.getIssuedAt().toInstant(),
                authorizationCode.getExpiresAt().toInstant()
            );
            builder.token(code, metadata ->
                metadata.putAll(authorizationCode.getMetadata()));
        }
        AccessToken accessToken = authorization.getAccessToken();
        if (accessToken != null && StringUtils.hasText(accessToken.getValue())) {
            String tokenTypeValue = accessToken.getTokenType();
            boolean isBearer = tokenTypeValue.equalsIgnoreCase(
                OAuth2AccessToken.TokenType.BEARER.getValue());
            OAuth2AccessToken.TokenType tokenType = isBearer ?
                OAuth2AccessToken.TokenType.BEARER : null;
            OAuth2AccessToken token = new OAuth2AccessToken(
                tokenType, accessToken.getValue(),
                accessToken.getIssuedAt().toInstant(),
                accessToken.getExpiresAt().toInstant(),
                accessToken.getScopes()
            );
            builder.token(token, metadata ->
                metadata.putAll(accessToken.getMetadata()));
        }
        RefreshToken refreshToken = authorization.getRefreshToken();
        if (refreshToken != null && StringUtils.hasText(refreshToken.getValue())) {
            OAuth2RefreshToken token = new OAuth2RefreshToken(
                refreshToken.getValue(),
                refreshToken.getIssuedAt().toInstant(),
                refreshToken.getExpiresAt().toInstant()
            );
            builder.token(token, metadata ->
                metadata.putAll(refreshToken.getMetadata()));
        }
        UserCode userCode = authorization.getUserCode();
        if (userCode != null && StringUtils.hasText(userCode.getValue())) {
            OAuth2UserCode code = new OAuth2UserCode(
                userCode.getValue(),
                userCode.getIssuedAt().toInstant(),
                userCode.getExpiresAt().toInstant()
            );
            builder.token(code, metadata ->
                metadata.putAll(userCode.getMetadata()));
        }
        DeviceCode deviceCode = authorization.getDeviceCode();
        if ( deviceCode != null && StringUtils.hasText(deviceCode.getValue())) {
            OAuth2DeviceCode code = new OAuth2DeviceCode(
                deviceCode.getValue(),
                deviceCode.getIssuedAt().toInstant(),
                deviceCode.getExpiresAt().toInstant()
            );
            builder.token(code, metadata ->
                metadata.putAll(deviceCode.getMetadata()));
        }
        OidcToken oidcToken = authorization.getOidcToken();
        if (oidcToken != null && StringUtils.hasText(oidcToken.getValue())) {
            @SuppressWarnings("unchecked")
            Map<String, Object> tokenMetadata = oidcToken.getMetadata(),
                oidcMetadata = (Map<String, Object>) tokenMetadata.get(
                    Token.CLAIMS_METADATA_NAME);
            OidcIdToken token = new OidcIdToken(
                oidcToken.getValue(),
                oidcToken.getIssuedAt().toInstant(),
                oidcToken.getExpiresAt().toInstant(),
                oidcMetadata
            );
            builder.token(token, metadata ->
                metadata.putAll(tokenMetadata));
        }
        authorizationCustomizer.customize(builder, authorization);
        return builder.build();
    }

    private static AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
        if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.AUTHORIZATION_CODE;
        } else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.CLIENT_CREDENTIALS;
        } else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.REFRESH_TOKEN;
        } else if (AuthorizationGrantType.DEVICE_CODE.getValue().equals(authorizationGrantType)) {
            return AuthorizationGrantType.DEVICE_CODE;
        }
        return new AuthorizationGrantType(authorizationGrantType);
    }
}
