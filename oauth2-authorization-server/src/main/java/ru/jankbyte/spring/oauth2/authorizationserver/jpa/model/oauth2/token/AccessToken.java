package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token;

import jakarta.persistence.*;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.converter.SecurityJsonToMapConverter;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.converter.StringToSetConverter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.Map;

import java.time.OffsetDateTime;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name = "issuedAt",
        column = @Column(name = "access_token_issued_at",
            columnDefinition = "TIMESTAMP")
    ),
    @AttributeOverride(name = "expiresAt",
        column = @Column(name = "access_token_expires_at",
            columnDefinition = "TIMESTAMP")
    ),
    @AttributeOverride(name = "value",
        column = @Column(name = "access_token_value",
            columnDefinition = "TEXT")
    ),
    @AttributeOverride(name = "metadata",
        column = @Column(name = "access_token_metadata",
            columnDefinition = "TEXT")
    )
})
public final class AccessToken extends AbstractToken {
    @Column(name = "access_token_scopes", columnDefinition = "VARCHAR",
        length = 1000)
    @Convert(converter = StringToSetConverter.class)
    private Set<String> scopes = new HashSet<>();

    @Column(name = "access_token_type", length = 100)
    private String tokenType;

    public static AccessTokenBuilder withValue(String value) {
        return new AccessTokenBuilder(value);
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenType() {
        return tokenType;
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        return Objects.hash(scopes) + hash;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof AccessToken accessToken) {
            return super.equals(accessToken) &&
                Objects.equals(accessToken.tokenType, tokenType) &&
                Objects.equals(accessToken.scopes, scopes);
        }
        return false;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public static final class AccessTokenBuilder
            extends TokenBuilder<AccessToken> {
        private AccessTokenBuilder(String value) {
            super(value, new AccessToken());
        }

        public AccessTokenBuilder tokenType(String tokenType) {
            token.setTokenType(tokenType);
            return this;
        }

        public AccessTokenBuilder scopes(Collection<String> scopes) {
            token.getScopes().addAll(scopes);
            return this;
        }
        
        @Override
        public AccessTokenBuilder metadata(Map<String, Object> metadata) {
            return (AccessTokenBuilder) super.metadata(metadata);
        }
        
        @Override
        public AccessTokenBuilder issuedAt(OffsetDateTime issuedAt) {
            return (AccessTokenBuilder) super.issuedAt(issuedAt);
        }
        
        @Override
        public AccessTokenBuilder expiresAt(OffsetDateTime expiresAt) {
            return (AccessTokenBuilder) super.expiresAt(expiresAt);
        }
    }
}
