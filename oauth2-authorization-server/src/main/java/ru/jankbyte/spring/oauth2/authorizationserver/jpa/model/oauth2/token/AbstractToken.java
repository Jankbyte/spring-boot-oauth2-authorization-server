package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token;

import jakarta.persistence.Convert;
import jakarta.persistence.MappedSuperclass;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.converter.SecurityJsonToMapConverter;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Objects;

@MappedSuperclass
public sealed abstract class AbstractToken permits AccessToken,
        AuthorizationCode, DeviceCode, OidcToken, RefreshToken, UserCode {
    @Convert(converter = SecurityJsonToMapConverter.class)
    private Map<String,Object> metadata;

    private OffsetDateTime issuedAt;

    private OffsetDateTime expiresAt;

    private String value;

    protected AbstractToken() {}

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public OffsetDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(OffsetDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(OffsetDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuedAt, expiresAt, value, metadata);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof AbstractToken token) {
            return Objects.equals(token.issuedAt, issuedAt) &&
                Objects.equals(token.expiresAt, expiresAt) &&
                Objects.equals(token.metadata, metadata) &&
                Objects.equals(token.value, value);
        }
        return false;
    }

    public static class TokenBuilder<T extends AbstractToken> {
        protected T token;

        protected TokenBuilder(String value, T token) {
            this.token = token;
            token.setValue(value);
        }

        public TokenBuilder<T> metadata(Map<String, Object> metadata) {
            token.setMetadata(metadata);
            return this;
        }

        public TokenBuilder<T> issuedAt(OffsetDateTime issuedAt) {
            token.setIssuedAt(issuedAt);
            return this;
        }

        public TokenBuilder<T> expiresAt(OffsetDateTime expiresAt) {
            token.setExpiresAt(expiresAt);
            return this;
        }

        public T build() {
            return token;
        }
    }
}
