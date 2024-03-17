package ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@AttributeOverrides({
    @AttributeOverride(name = "issuedAt",
        column = @Column(name = "device_code_issued_at",
            columnDefinition = "TIMESTAMP")
    ),
    @AttributeOverride(name = "expiresAt",
        column = @Column(name = "device_code_expires_at",
            columnDefinition = "TIMESTAMP")
    ),
    @AttributeOverride(name = "value",
        column = @Column(name = "device_code_value",
            columnDefinition = "TEXT")
    ),
    @AttributeOverride(name = "metadata",
        column = @Column(name = "device_code_metadata",
            columnDefinition = "TEXT")
    )
})
public final class DeviceCode extends AbstractToken {
    public static TokenBuilder<DeviceCode> withValue(String value) {
        return new TokenBuilder<>(value, new DeviceCode());
    }
}
