package ru.jankbyte.spring.oauth2.authorizationserver.dto.oauth2;

import java.time.OffsetDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

public record AuthorizationResponse(UUID id,
    @JsonProperty("authenticated_ip") String authenticatedIp,
    @JsonFormat(pattern = "dd.MM.yyyy HH:mm:ss")
        @JsonProperty("issued_at") OffsetDateTime issuedAt,
    @JsonProperty("client_id") String clientId,
    @JsonProperty("client_name") String clientName) {
}
