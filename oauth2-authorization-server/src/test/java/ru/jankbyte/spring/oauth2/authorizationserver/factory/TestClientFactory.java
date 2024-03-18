package ru.jankbyte.spring.oauth2.authorizationserver.factory;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client.ClientBuilder;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.ClientAccount;

import org.springframework.boot.test.context.TestComponent;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.Set;
import java.util.UUID;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@TestComponent
public final class TestClientFactory {
    private final Clock clock;

    public static final String TEST_CLIENT_ID = "client-test-id";

    public TestClientFactory(Clock clock) {
        this.clock = clock;
    }

    public final ClientBuilder createPreparedClientBuilder() {
        OffsetDateTime expiringTime = OffsetDateTime.now(clock);
        Set<String> redirectUrls = Set.of(
            "http://123.com", "http://demo.com"
        );
        Set<String> postlogoutUrls = Set.of(
            "http://postlogout1.com", "http://postlogout2.com"
        );
        OffsetDateTime clientIssuedAt = OffsetDateTime.now(clock);
        ClientAccount account = ClientAccount.clientAccountBuilder()
            .id(UUID.randomUUID())
            .enabled(true)
            .name("client-test-id")
            .password("mysupersecrettest")
            .passwordExpiresAt(expiringTime)
            .build();
        Duration authorizationCodeTimeToLive = Duration.of(5, ChronoUnit.MINUTES);
        Duration accessTokenTimeToLive = Duration.of(15, ChronoUnit.MINUTES);
        Duration refreshTokenTimeToLive = Duration.of(30, ChronoUnit.DAYS);
        return Client.withName("The test client")
            .requiredProofKey(false)
            .redirectUrls(redirectUrls)
            .postLogoutUrls(postlogoutUrls)
            .issuedAt(clientIssuedAt)
            .requiredAuthorizationConsent(false)
            .reuseRefreshTokens(false)
            .accessTokenTimeToLive(accessTokenTimeToLive)
            .refreshTokenTimeToLive(refreshTokenTimeToLive)
            .authorizationCodeTimeToLive(authorizationCodeTimeToLive)
            .deviceCodeTimeToLive(authorizationCodeTimeToLive)
            .account(account);
    }
}
