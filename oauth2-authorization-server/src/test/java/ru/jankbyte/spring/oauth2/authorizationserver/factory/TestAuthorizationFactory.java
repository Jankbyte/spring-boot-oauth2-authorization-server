package ru.jankbyte.spring.oauth2.authorizationserver.factory;

import org.springframework.boot.test.context.TestComponent;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization.AuthorizationBuilder;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token.AccessToken;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token.AccessToken.AccessTokenBuilder;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token.AuthorizationCode;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token.RefreshToken;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.UUID;

@TestComponent
public class TestAuthorizationFactory {
    private final Clock clock;

    public TestAuthorizationFactory(Clock clock) {
        this.clock = clock;
    }

    public AuthorizationBuilder createPreparedBuilder(
            Client client, Account account) {
        UUID id = UUID.randomUUID();
        OffsetDateTime current = OffsetDateTime.now(clock);
        String randomCode = getRandomUUIDAsString();
        AuthorizationCode authorizationCode = AuthorizationCode.withValue(randomCode)
            .issuedAt(current)
            .expiresAt(current.plusMinutes(5))
            .build();
        String randomAccessTokenValue = getRandomUUIDAsString();
        AccessToken accessToken = AccessToken.withValue(
                randomAccessTokenValue)
            .issuedAt(current)
            .expiresAt(current.plusMinutes(15))
            .build();
        String randomRefreshTokenValue = getRandomUUIDAsString();
        RefreshToken refreshToken = RefreshToken.withValue(
                randomRefreshTokenValue)
            .issuedAt(current)
            .expiresAt(current.plusDays(15))
            .build();
        return Authorization.withId(id)
            .authorizationCode(authorizationCode)
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .client(client)
            .account(account)
            .grantType("code");
    }

    public AuthorizationCode createExpiredAuthorizationCode() {
        String value = getRandomUUIDAsString();
        OffsetDateTime current = OffsetDateTime.now(clock);
        return AuthorizationCode.withValue(value)
            .issuedAt(current.minusSeconds(1))
            .expiresAt(current)
            .build();
    }

    public AccessToken createExpiredAccessToken() {
        String value = getRandomUUIDAsString();
        OffsetDateTime current = OffsetDateTime.now(clock);
        return AccessToken.withValue(value)
            .issuedAt(current.minusSeconds(1))
            .expiresAt(current)
            .tokenType("OPAQUE")
            .build();
    }

    public RefreshToken createExpiredRefreshToken() {
        String value = getRandomUUIDAsString();
        OffsetDateTime current = OffsetDateTime.now(clock);
        return RefreshToken.withValue(value)
            .issuedAt(current.minusSeconds(1))
            .expiresAt(current)
            .build();
    }

    private static String getRandomUUIDAsString() {
        return UUID.randomUUID().toString();
    }
}
