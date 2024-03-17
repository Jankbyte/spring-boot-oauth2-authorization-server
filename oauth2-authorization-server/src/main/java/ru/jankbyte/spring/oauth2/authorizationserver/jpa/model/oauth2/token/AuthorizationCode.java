package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name = "issuedAt",
        column = @Column(name = "authorization_code_issued_at",
            columnDefinition = "TIMESTAMP")
    ),
    @AttributeOverride(name = "expiresAt",
        column = @Column(name = "authorization_code_expires_at",
            columnDefinition = "TIMESTAMP")
    ),
    @AttributeOverride(name = "value",
        column = @Column(name = "authorization_code_value",
            columnDefinition = "TEXT")
    ),
    @AttributeOverride(name = "metadata",
        column = @Column(name = "authorization_code_metadata",
            columnDefinition = "TEXT")
    )
})
public final class AuthorizationCode extends AbstractToken {
    public static TokenBuilder<AuthorizationCode> withValue(String value) {
        return new TokenBuilder<>(value, new AuthorizationCode());
    }
}
