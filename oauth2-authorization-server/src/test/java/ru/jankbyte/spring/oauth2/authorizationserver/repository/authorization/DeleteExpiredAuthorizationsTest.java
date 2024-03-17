package ru.jankbyte.spring.oauth2.authorizationserver.repository.authorization;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import ru.jankbyte.spring.oauth2.authorizationserver.configuration.BaseTestConfiguration;
import ru.jankbyte.spring.oauth2.authorizationserver.factory.TestAccountFactory;
import ru.jankbyte.spring.oauth2.authorizationserver.factory.TestAuthorizationFactory;
import ru.jankbyte.spring.oauth2.authorizationserver.factory.TestClientFactory;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType.TokenTypeValue;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token.AccessToken;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token.AuthorizationCode;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token.RefreshToken;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Import({
    TestClientFactory.class,
    TestAccountFactory.class,
    TestAuthorizationFactory.class,
    BaseTestConfiguration.class
})
public class DeleteExpiredAuthorizationsTest extends AbstractAuthorizationRepositoryTest {
    @Autowired
    private TestAccountFactory accountFactory;

    @Autowired
    private TestClientFactory clientFactory;

    @Autowired
    private TestAuthorizationFactory authorizationFactory;

    @Autowired
    private Clock clock;

    private List<UUID> expiredAuthorizationIds = new ArrayList<>();

    @BeforeEach
    public void initializeData() {
        Account account = accountFactory.createPreparedBuilder().build();
        AccessTokenType tokenType = tokenTypeRepository.findByValue(
            TokenTypeValue.JWT).get();
        Client client = clientFactory.createPreparedClientBuilder()
            .accessTokenType(tokenType)
            .build();
        accountRepository.saveAndFlush(account);
        clientRepository.saveAndFlush(client);
        insertAuthorizations(client, account);
    }

    private void insertAuthorizations(Client client, Account account) {
        AuthorizationCode expiredAuthorizationCode =
            authorizationFactory.createExpiredAuthorizationCode();
        Authorization authorization1 =
            authorizationFactory.createPreparedBuilder(client, account)
                .authorizationCode(expiredAuthorizationCode)
                .accessToken(null)
                .build();
        AccessToken accessToken =
            authorizationFactory.createExpiredAccessToken();
        Authorization authorization2 =
            authorizationFactory.createPreparedBuilder(client, account)
                .accessToken(accessToken)
                .refreshToken(null)
                .build();
        RefreshToken refreshToken =
            authorizationFactory.createExpiredRefreshToken();
        Authorization authorization3 =
            authorizationFactory.createPreparedBuilder(client, account)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        List<Authorization> authorizations = List.of(
            authorization1, authorization2, authorization3);
        authorizationRepository.saveAllAndFlush(authorizations);
        List<UUID> ids = List.of(authorization1.getId(),
            authorization2.getId(), authorization3.getId());
        expiredAuthorizationIds.addAll(ids);

    }

    @Test
    public void shouldDeleteExpiredAuthorizations() {
        OffsetDateTime currentDate = OffsetDateTime.now(clock);
        int deleted = authorizationRepository.deleteExpired(currentDate);
        assertThat(deleted).isEqualTo(expiredAuthorizationIds.size());
    }
}