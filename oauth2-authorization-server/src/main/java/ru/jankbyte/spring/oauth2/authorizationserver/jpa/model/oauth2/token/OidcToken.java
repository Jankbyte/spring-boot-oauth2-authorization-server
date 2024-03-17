package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name = "issuedAt",
        column = @Column(name = "oidc_id_token_issued_at",
            columnDefinition = "TIMESTAMP")
    ),
    @AttributeOverride(name = "expiresAt",
        column = @Column(name = "oidc_id_token_expires_at",
            columnDefinition = "TIMESTAMP")
    ),
    @AttributeOverride(name = "value",
        column = @Column(name = "oidc_id_token_value",
            columnDefinition = "TEXT")
    ),
    @AttributeOverride(name = "metadata",
        column = @Column(name = "oidc_id_token_metadata",
            columnDefinition = "TEXT")
    )
})
public final class OidcToken extends AbstractToken {
    public static TokenBuilder<OidcToken> withValue(String value) {
        return new TokenBuilder<>(value, new OidcToken());
    }
}
