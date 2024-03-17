package ru.jankbyte.spring.oauth2.authorizationserver.property;

import java.util.Set;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.GrantType.GrantTypeName;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType.TokenTypeValue;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthenticationMethod.AuthenticationMethodName;

public class ClientCredential {
    private String clientId, clientSecret, clientName;
    private TokenTypeValue accessTokenTypeValue;
    private Boolean requireAuthorizationConsent, reuseRefreshTokens, requiredProofKey;
    private Set<String> scopes;
    private Set<String> redirectUrls, postlogoutUrls;
    private Set<GrantTypeName> grantTypes;
    private Set<AuthenticationMethodName> authenticationMethods;

    public void setRequiredProofKey(Boolean requiredProofKey) {
        this.requiredProofKey = requiredProofKey;
    }

    public Boolean isRequiredProofKey() {
        return requiredProofKey;
    }

    public void setPostlogoutUrls(Set<String> postlogoutUrls) {
        this.postlogoutUrls = postlogoutUrls;
    }

    public Set<String> getPostlogoutUrls() {
        return postlogoutUrls;
    }

    public void setRedirectUrls(Set<String> redirectUrls) {
        this.redirectUrls = redirectUrls;
    }

    public Set<String> getRedirectUrls() {
        return redirectUrls;
    }

    public void setAuthenticationMethods(
            Set<AuthenticationMethodName> authenticationMethods) {
        this.authenticationMethods = authenticationMethods;
    }

    public Set<AuthenticationMethodName> getAuthenticationMethods() {
        return authenticationMethods;
    }

    public void setGrantTypes(Set<GrantTypeName> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public Set<GrantTypeName> getGrantTypes() {
        return grantTypes;
    }

    public void setRequireAuthorizationConsent(Boolean requireAuthorizationConsent) {
        this.requireAuthorizationConsent = requireAuthorizationConsent;
    }

    public void setReuseRefreshTokens(Boolean reuseRefreshTokens) {
        this.reuseRefreshTokens = reuseRefreshTokens;
    }

    public Boolean isReuseRefreshTokens() {
        return reuseRefreshTokens;
    }

    public Boolean isRequireAuthorizationConsent() {
        return requireAuthorizationConsent;
    }

    public void setAccessTokenTypeValue(TokenTypeValue accessTokenTypeValue) {
        this.accessTokenTypeValue = accessTokenTypeValue;
    }

    public TokenTypeValue getAccessTokenTypeValue() {
        return accessTokenTypeValue;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    @Override
    public String toString() {
        String className = getClass().getSimpleName();
        return """
            %s[clientId=%s, clientSecret=%s, clientName=%s, \
            accessTokenTypeValue=%s, requireAuthorizationConsent=%b, \
            reuseRefreshTokens=%b, requiredProofKey=%b, scopes=%s, \
            redirectUrls=%s, postlogoutUrls=%s, grantTypes=%s, \
            authenticationMethods=%s]\
            """
            .formatted(className, clientId, clientSecret, clientName,
                accessTokenTypeValue, requireAuthorizationConsent,
                reuseRefreshTokens, requiredProofKey, scopes, redirectUrls,
                postlogoutUrls, grantTypes, authenticationMethods);
    }
}
