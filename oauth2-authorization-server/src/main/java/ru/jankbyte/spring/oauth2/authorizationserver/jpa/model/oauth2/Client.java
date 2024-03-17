package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2;

import java.util.*;
import java.time.OffsetDateTime;
import java.time.Duration;

import jakarta.persistence.*;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityGraphNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.converter.DurationToMillisConverter;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.converter.StringToSetConverter;

@NamedEntityGraph(
    name = EntityGraphNames.CLIENT_FIND_BY_ID,
    attributeNodes = {
        @NamedAttributeNode("scopes"),
        @NamedAttributeNode("grantTypes"),
        @NamedAttributeNode("authenticationMethods"),
        @NamedAttributeNode("accessTokenType"),
        @NamedAttributeNode("account")
    }
)
@Entity
@Table(schema = "oauth2")
public class Client {
    @Id
    private UUID id;

    @MapsId
    @JoinColumn(name = "id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ClientAccount account;

    @Column(columnDefinition = "TEXT", name = "jwk_set_url")
    private String jwkSetUrl;
    
    @Column(length = 15, name = "token_algorithm")
    private String tokenAlgorithm;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(name = "logo_url", columnDefinition = "TEXT")
    private String logoUrl;

    @Column(name = "issued_at", nullable = false,
        columnDefinition = "TIMESTAMP")
    private OffsetDateTime issuedAt;

    @Column(nullable = false,
        name = "refresh_token_time_live_millis"
    )
    @Convert(converter = DurationToMillisConverter.class)
    private Duration refreshTokenTimeToLive;

    @Column(nullable = false,
        name = "device_code_time_live_millis"
    )
    @Convert(converter = DurationToMillisConverter.class)
    private Duration deviceCodeTimeToLive;

    @Column(nullable = false,
        name = "access_token_time_live_millis"
    )
    @Convert(converter = DurationToMillisConverter.class)
    private Duration accessTokenTimeToLive;

    @Column(nullable = false,
        name = "authorization_code_time_live_millis"
    )
    @Convert(converter = DurationToMillisConverter.class)
    private Duration authorizationCodeTimeToLive;

    @Column(name = "redirect_urls", nullable = false, columnDefinition = "TEXT")
    @Convert(converter = StringToSetConverter.class)
    private Set<String> redirectUrls = new LinkedHashSet<>();

    @Column(name = "post_logout_urls", columnDefinition = "TEXT")
    @Convert(converter = StringToSetConverter.class)
    private Set<String> postLogoutUrls = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(schema = "oauth2", name = "client_scopes",
        joinColumns = {
            @JoinColumn(name = "client_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "scope_id")
        }
    )
    private Set<Scope> scopes = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(schema = "oauth2", name = "client_grant_types",
        joinColumns = {
            @JoinColumn(name = "client_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "grant_type_id")
        }
    )
    private Set<GrantType> grantTypes = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(schema = "oauth2", name = "client_authentication_methods",
        joinColumns = {
            @JoinColumn(name = "client_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "authentication_method_id")
        }
    )
    private Set<AuthenticationMethod> authenticationMethods = new LinkedHashSet<>();

    @Column(name = "reuse_refresh_tokens", nullable = false)
    private Boolean reuseRefreshTokens;

    @Column(name = "required_authorization_consent", nullable = false)
    private Boolean requiredAuthorizationConsent;

    @Column(name = "required_proof_key", nullable = false)
    private Boolean requiredProofKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "access_token_type_id", nullable = false)
    private AccessTokenType accessTokenType;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL,
        orphanRemoval = true)
    private Set<AuthorizationConsent> authorizationConsents = new LinkedHashSet<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL,
        orphanRemoval = true)
    private Set<Authorization> authorizations = new LinkedHashSet<>();

    public static ClientBuilder withName(String name) {
        return new ClientBuilder(name);
    }

    public static ClientBuilder builder() {
        return new ClientBuilder();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, issuedAt,
            redirectUrls, postLogoutUrls, reuseRefreshTokens,
            requiredAuthorizationConsent, requiredProofKey,
            jwkSetUrl, tokenAlgorithm, authorizationCodeTimeToLive,
            accessTokenTimeToLive, refreshTokenTimeToLive,
            deviceCodeTimeToLive, logoUrl);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Client client) {
            return Objects.equals(client.issuedAt, issuedAt) &&
                Objects.equals(client.id, id) &&
                Objects.equals(client.redirectUrls, redirectUrls) &&
                Objects.equals(client.postLogoutUrls, postLogoutUrls) &&
                Objects.equals(client.reuseRefreshTokens, reuseRefreshTokens) &&
                Objects.equals(client.requiredAuthorizationConsent,
                    requiredAuthorizationConsent) &&
                Objects.equals(client.requiredProofKey, requiredProofKey) &&
                Objects.equals(client.name, name) &&
                Objects.equals(client.jwkSetUrl, jwkSetUrl) &&
                Objects.equals(client.tokenAlgorithm, tokenAlgorithm) &&
                Objects.equals(client.authorizationCodeTimeToLive,
                    authorizationCodeTimeToLive) &&
                Objects.equals(client.refreshTokenTimeToLive,
                    refreshTokenTimeToLive) &&
                Objects.equals(client.deviceCodeTimeToLive,
                    deviceCodeTimeToLive) &&
                Objects.equals(client.logoUrl, logoUrl) &&
                Objects.equals(client.accessTokenTimeToLive, accessTokenTimeToLive);
        }
        return false;
    }

    @Override
    public String toString() {
        String className = getClass().getSimpleName();
        return """
            %s[id=%s, name=%s, issuedAt=%s, redirectUrls=%s, \
            postLogoutUrls=%s, reuseRefreshTokens=%b, \
            requiredAuthorizationConsent=%b, requiredProofKey=%b, \
            jwkSetUrl=%s, tokenAlgorithm=%s, accessTokenTimeToLive=%s \
            authorizationCodeTimeToLive=%s, refreshTokenTimeToLive=%s, \
            deviceCodeTimeToLive=%s, logoUrl=%s]\
            """.formatted(className, id, name, issuedAt,
                    redirectUrls, postLogoutUrls, reuseRefreshTokens,
                    requiredAuthorizationConsent,
                    requiredProofKey, jwkSetUrl, tokenAlgorithm,
                    accessTokenTimeToLive, authorizationCodeTimeToLive,
                    refreshTokenTimeToLive, deviceCodeTimeToLive, logoUrl);
    }

    public void setAccount(ClientAccount account) {
        this.account = account;
    }

    public ClientAccount getAccount() {
        return account;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setDeviceCodeTimeToLive(Duration deviceCodeTimeToLive) {
        this.deviceCodeTimeToLive = deviceCodeTimeToLive;
    }

    public Duration getDeviceCodeTimeToLive() {
        return deviceCodeTimeToLive;
    }

    public void setAuthorizationCodeTimeToLive(Duration authorizationCodeTimeToLive) {
        this.authorizationCodeTimeToLive = authorizationCodeTimeToLive;
    }

    public Duration getAuthorizationCodeTimeToLive() {
        return authorizationCodeTimeToLive;
    }

    public void setRefreshTokenTimeToLive(Duration refreshTokenTimeToLive) {
        this.refreshTokenTimeToLive = refreshTokenTimeToLive;
    }

    public Duration getRefreshTokenTimeToLive() {
        return refreshTokenTimeToLive;
    }

    public void setAccessTokenTimeToLive(Duration accessTokenTimeToLive) {
        this.accessTokenTimeToLive = accessTokenTimeToLive;
    }

    public Duration getAccessTokenTimeToLive() {
        return accessTokenTimeToLive;
    }

    public void setTokenAlgorithm(String tokenAlgorithm) {
        this.tokenAlgorithm = tokenAlgorithm;
    }

    public String getTokenAlgorithm() {
        return tokenAlgorithm;
    }

    public void setJwkSetUrl(String jwkSetUrl) {
        this.jwkSetUrl = jwkSetUrl;
    }

    public String getJwkSetUrl() {
        return jwkSetUrl;
    }

    public void setRequiredProofKey(Boolean requiredProofKey) {
        this.requiredProofKey = requiredProofKey;
    }
    
    public Boolean isRequiredProofKey() {
        return requiredProofKey;
    }

    public void setRequiredAuthorizationConsent(Boolean requiredAuthorizationConsent) {
        this.requiredAuthorizationConsent = requiredAuthorizationConsent;
    }

    public Boolean isRequiredAuthorizationConsent() {
        return requiredAuthorizationConsent;
    }

    public void setReuseRefreshTokens(Boolean reuseRefreshTokens) {
        this.reuseRefreshTokens = reuseRefreshTokens;
    }

    public Boolean canReuseRefreshTokens() {
        return reuseRefreshTokens;
    }

    public void setAccessTokenType(AccessTokenType accessTokenType) {
        this.accessTokenType = accessTokenType;
    }

    public AccessTokenType getAccessTokenType() {
        return accessTokenType;
    }

    public void addAuthorizationConsent(AuthorizationConsent authorizationConsent) {
        authorizationConsents.add(authorizationConsent);
        authorizationConsent.setClient(this);
    }

    public void removeAuthorizationConsent(AuthorizationConsent authorizationConsent) {
        authorizationConsents.remove(authorizationConsent);
        authorizationConsent.setClient(null);
    }

    public Set<AuthorizationConsent> getAuthorizationConsents() {
        return authorizationConsents;
    }

    public void addAuthorization(Authorization authorization) {
        authorizations.add(authorization);
        authorization.setClient(this);
    }

    public void removeAuthorization(Authorization authorization) {
        authorizations.remove(authorization);
        authorization.setClient(null);
    }
    
    public Set<Authorization> getAuthorizations() {
        return authorizations;
    }

    public Set<GrantType> getGrantTypes() {
        return grantTypes;
    }

    public void addGrantType(GrantType grantType) {
        grantTypes.add(grantType);
        grantType.getClients().add(this);
    }

    public void removeGrantType(GrantType grantType) {
        grantTypes.remove(grantType);
        grantType.getClients().remove(this);
    }

    public Set<AuthenticationMethod> getAuthenticationMethods() {
        return authenticationMethods;
    }

    public void addAuthenticationMethod(
            AuthenticationMethod authenticationMethod) {
        authenticationMethods.add(authenticationMethod);
        authenticationMethod.getClients().add(this);
    }

    public void removeAuthenticationMethod(
            AuthenticationMethod authenticationMethod) {
        authenticationMethods.remove(authenticationMethod);
        authenticationMethod.getClients().remove(this);
    }

    public Set<Scope> getScopes() {
        return scopes;
    }

    public void addScope(Scope scope) {
        scopes.add(scope);
        scope.getClients().add(this);
    }

    public void removeScope(Scope scope) {
        scopes.remove(scope);
        scope.getClients().remove(this);
    }

    public void setIssuedAt(OffsetDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public OffsetDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setRedirectUrls(Set<String> redirectUrls) {
        this.redirectUrls = redirectUrls;
    }

    public Set<String> getRedirectUrls() {
        return redirectUrls;
    }

    public void setPostLogoutUrls(Set<String> postLogoutUrls) {
        this.postLogoutUrls = postLogoutUrls;
    }

    public Set<String> getPostLogoutUrls() {
        return postLogoutUrls;
    }

    public static final class ClientBuilder {
        private final Client client = new Client();

        private ClientBuilder() {}

        private ClientBuilder(String name) {
            client.setName(name);
        }

        public ClientBuilder tokenAlgorithm(String tokenAlgorithm) {
            client.setTokenAlgorithm(tokenAlgorithm);
            return this;
        }

        public ClientBuilder jwkSetUrl(String jwkSetUrl) {
            client.setJwkSetUrl(jwkSetUrl);
            return this;
        }

        public ClientBuilder id(UUID id) {
            client.setId(id);
            return this;
        }

        public ClientBuilder logoUrl(String logoUrl) {
            client.setLogoUrl(logoUrl);
            return this;
        }

        public ClientBuilder name(String name) {
            client.setName(name);
            return this;
        }

        public ClientBuilder account(ClientAccount account) {
            client.setAccount(account);
            return this;
        }

        public ClientBuilder requiredProofKey(Boolean requiredProofKey) {
            client.setRequiredProofKey(requiredProofKey);
            return this;
        }

        public ClientBuilder requiredAuthorizationConsent(
                Boolean requiredAuthorizationConsent) {
            client.setRequiredAuthorizationConsent(
                requiredAuthorizationConsent);
            return this;
        }

        public ClientBuilder accessTokenType(
                AccessTokenType accessTokenType) {
            accessTokenType.addClient(client);
            return this;
        }

        public ClientBuilder issuedAt(OffsetDateTime issuedAt) {
            client.setIssuedAt(issuedAt);
            return this;
        }

        public ClientBuilder reuseRefreshTokens(Boolean reuseRefreshTokens) {
            client.setReuseRefreshTokens(reuseRefreshTokens);
            return this;
        }

        public ClientBuilder redirectUrls(Set<String> redirectUrls) {
            client.setRedirectUrls(redirectUrls);
            return this;
        }

        public ClientBuilder postLogoutUrls(Set<String> postLogoutUrls) {
            client.setPostLogoutUrls(postLogoutUrls);
            return this;
        }

        public ClientBuilder scopes(Collection<Scope> scopes) {
            scopes.forEach(client::addScope);
            return this;
        }

        public ClientBuilder grantTypes(Collection<GrantType> grantTypes) {
            grantTypes.forEach(client::addGrantType);
            return this;
        }

        public ClientBuilder deviceCodeTimeToLive(Duration deviceCodeTimeToLive) {
            client.setDeviceCodeTimeToLive(deviceCodeTimeToLive);
            return this;
        }

        public ClientBuilder accessTokenTimeToLive(Duration accessTokenTimeToLive) {
            client.setAccessTokenTimeToLive(accessTokenTimeToLive);
            return this;
        }

        public ClientBuilder refreshTokenTimeToLive(Duration refreshTokenTimeToLive) {
            client.setRefreshTokenTimeToLive(refreshTokenTimeToLive);
            return this;
        }

        public ClientBuilder authorizationCodeTimeToLive(Duration authorizationCodeTimeToLive) {
            client.setAuthorizationCodeTimeToLive(authorizationCodeTimeToLive);
            return this;
        }

        public ClientBuilder authenticationMethods(
                Collection<AuthenticationMethod> authenticationMethods) {
            authenticationMethods.forEach(client::addAuthenticationMethod);
            return this;
        }

        public Client build() {
            return client;
        }
    }
}
