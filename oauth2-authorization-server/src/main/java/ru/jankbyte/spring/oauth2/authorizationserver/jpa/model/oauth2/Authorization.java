package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2;

import jakarta.persistence.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityQueryNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityGraphNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token.*;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.converter.SecurityJsonToMapConverter;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.converter.StringToSetConverter;

import org.hibernate.proxy.HibernateProxy;

import java.util.*;

@NamedQueries({
    @NamedQuery(
        name = EntityQueryNames.AUTHORIZATION_FIND_BY_UNKNOWN_TOKEN,
        query = """
            SELECT a FROM  Authorization a
            WHERE a.state = :token OR
                a.accessToken.value = :token OR
                a.refreshToken.value = :token OR
                a.userCode.value = :token OR
                a.oidcToken.value = :token OR
                a.deviceCode.value = :token OR
                a.authorizationCode.value = :token"""
    ),
    @NamedQuery(
        name = EntityQueryNames.AUTHORIZATION_DELETE_EXPIRED,
        query = """
            DELETE Authorization a
            WHERE (a.refreshToken.expiresAt <= :currentDate)
                OR (a.authorizationCode.expiresAt <= :currentDate
                    AND a.accessToken.tokenType IS NULL)
                OR (a.refreshToken.value IS NULL
                    AND a.accessToken.expiresAt <= :currentDate)"""
    ),
    @NamedQuery(
        name = EntityQueryNames.AUTHORIZATION_FIND_BY_ACCOUNT_NAME_AND_NON_EXPIRED,
        query = """
            SELECT a FROM Authorization a
            JOIN FETCH a.client c
            JOIN FETCH c.account ca
            WHERE (a.account.name = :name)
            AND ((a.refreshToken.expiresAt > :currentDate)
                OR (a.authorizationCode.expiresAt > :currentDate
                    AND a.accessToken.tokenType IS NOT NULL)
                OR (a.refreshToken.value IS NOT NULL
                    AND a.accessToken.expiresAt > :currentDate))"""
    )
})
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = EntityGraphNames.AUTHORIZATION_FIND_BY_TOKEN,
        attributeNodes = {
            @NamedAttributeNode(value = "client", subgraph = "client.scopes"),
            @NamedAttributeNode(value = "account", subgraph = "account.authorities")
        },
        subgraphs = {
            @NamedSubgraph(name = "client.scopes", attributeNodes = {
                @NamedAttributeNode("account"),
                @NamedAttributeNode("scopes"),
                @NamedAttributeNode("grantTypes"),
                @NamedAttributeNode("authenticationMethods"),
                @NamedAttributeNode("accessTokenType")
            }),
            @NamedSubgraph(name = "account.authorities", attributeNodes = {
                @NamedAttributeNode("authorities")
            })
        }
    )
})
@Entity
@Table(schema = "oauth2")
public class Authorization {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    @Column(nullable = false, length = 100)
    private String grantType;

    @Column(length = 1000)
    @Convert(converter = StringToSetConverter.class)
    private Set<String> scopes = new LinkedHashSet<>();

    @Column(columnDefinition = "TEXT")
    @Convert(converter = SecurityJsonToMapConverter.class)
    private Map<String, Object> attributes;

    @Column(length = 1000)
    private String state;

    @Embedded
    private AuthorizationCode authorizationCode;

    @Embedded
    private AccessToken accessToken;

    @Embedded
    private RefreshToken refreshToken;

    @Embedded
    private OidcToken oidcToken;

    @Embedded
    private UserCode userCode;

    @Embedded
    private DeviceCode deviceCode;

    public static AuthorizationBuilder withId(UUID id) {
        return new AuthorizationBuilder(id);
    }
    
    @Override
    public int hashCode() {
        return this instanceof HibernateProxy proxy
            ? proxy.getHibernateLazyInitializer()
                .getPersistentClass().hashCode()
            : getClass().hashCode(); 
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof Authorization authorization) {
            return Objects.equals(id, authorization.id) &&
                Objects.equals(grantType, authorization.grantType) &&
                Objects.equals(scopes, authorization.scopes) &&
                Objects.equals(attributes, authorization.attributes) &&
                Objects.equals(state, authorization.state) &&
                Objects.equals(authorizationCode,
                    authorization.authorizationCode) &&
                Objects.equals(accessToken, authorization.accessToken) &&
                Objects.equals(refreshToken, authorization.refreshToken) &&
                Objects.equals(oidcToken, authorization.oidcToken) &&
                Objects.equals(userCode, authorization.userCode) &&
                Objects.equals(deviceCode, authorization.deviceCode);
        }
        return false;
    }

    @Override
    public String toString() {
        return """
            Authorization[id=%s, grantType=%s, scopes=%s, attributes=%s, \
            state=%s, authorizationCode=%s, accessToken=%s, refreshToken=%s, \
            oidcToken=%s, userCode=%s, deviceCode=%s]"""
                .formatted(id, grantType, scopes, attributes,
                    state, authorizationCode, accessToken, refreshToken,
                    oidcToken, userCode, deviceCode);
    }

    public String getAuthenticatedIp() {
        UsernamePasswordAuthenticationToken token = getAuthenticatedToken();
        if (token == null) {
            return null;
        }
        WebAuthenticationDetails webDetails = (WebAuthenticationDetails)
            getAuthenticatedToken().getDetails();
        if (webDetails == null) {
            return null;
        }
        return webDetails.getRemoteAddress();
    }

    private UsernamePasswordAuthenticationToken getAuthenticatedToken() {
        return attributes == null ? null :
            (UsernamePasswordAuthenticationToken)
                attributes.get("java.security.Principal");
    }

    public void setDeviceCode(DeviceCode deviceCode) {
        this.deviceCode = deviceCode;
    }

    public DeviceCode getDeviceCode() {
        return deviceCode;
    }

    public void setUserCode(UserCode userCode) {
        this.userCode = userCode;
    }

    public UserCode getUserCode() {
        return userCode;
    }

    public void setOidcToken(OidcToken oidcToken) {
        this.oidcToken = oidcToken;
    }

    public OidcToken getOidcToken() {
        return oidcToken;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setRefreshToken(RefreshToken refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public void setAuthorizationCode(AuthorizationCode authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public AuthorizationCode getAuthorizationCode() {
        return authorizationCode;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public static final class AuthorizationBuilder {
        private final Authorization authorization = new Authorization();

        public AuthorizationBuilder(UUID id) {
            authorization.setId(id);
        }

        public AuthorizationBuilder client(Client client) {
            authorization.setClient(client);
            return this;
        }

        public AuthorizationBuilder account(Account account) {
            authorization.setAccount(account);
            return this;
        }

        public AuthorizationBuilder state(String state) {
            authorization.setState(state);
            return this;
        }

        public AuthorizationBuilder grantType(String grantType) {
            authorization.setGrantType(grantType);
            return this;
        }

        public AuthorizationBuilder scopes(Collection<String> scopes) {
            authorization.getScopes().addAll(scopes);
            return this;
        }

        public AuthorizationBuilder attributes(Map<String, Object> attributes) {
            authorization.setAttributes(attributes);
            return this;
        }

        public AuthorizationBuilder authorizationCode(AuthorizationCode authorizationCode) {
            authorization.setAuthorizationCode(authorizationCode);
            return this;
        }

        public AuthorizationBuilder accessToken(AccessToken accessToken) {
            authorization.setAccessToken(accessToken);
            return this;
        }

        public AuthorizationBuilder oidcToken(OidcToken oidcToken) {
            authorization.setOidcToken(oidcToken);
            return this;
        }

        public AuthorizationBuilder userCode(UserCode userCode) {
            authorization.setUserCode(userCode);
            return this;
        }

        public AuthorizationBuilder deviceCode(DeviceCode deviceCode) {
            authorization.setDeviceCode(deviceCode);
            return this;
        }

        public AuthorizationBuilder refreshToken(RefreshToken refreshToken) {
            authorization.setRefreshToken(refreshToken);
            return this;
        }

        public Authorization build() {
            return authorization;
        }
    }

}
