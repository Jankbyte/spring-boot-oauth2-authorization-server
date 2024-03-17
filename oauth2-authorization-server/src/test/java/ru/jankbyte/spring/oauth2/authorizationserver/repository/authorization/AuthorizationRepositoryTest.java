package ru.jankbyte.spring.oauth2.authorizationserver.repository.authorization;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Authority;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType.TokenTypeValue;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.AbstractOrderedJpaTest;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AccessTokenTypeRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AuthorizationRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.AuthorityRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.AccountRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.factory.TestAccountFactory;
import ru.jankbyte.spring.oauth2.authorizationserver.factory.TestClientFactory;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.ClientRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.configuration.BaseTestConfiguration;

import org.springframework.context.annotation.Import;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

@Import({
    BaseTestConfiguration.class,
    TestClientFactory.class,
    TestAccountFactory.class
})
public class AuthorizationRepositoryTest extends AbstractOrderedJpaTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private AuthorizationRepository authorizationRepository;
    
    @Autowired
    private TestClientFactory testClientFactory;

    @Autowired
    private TestAccountFactory testAccountFactory;

    @Autowired
    private AccessTokenTypeRepository tokenTypeRepository;

    @Test
    public void shouldSaveAuthorization() {
        AccessTokenType tokenType = tokenTypeRepository.findByValue(
            TokenTypeValue.JWT).get();
        Client client = testClientFactory.createPreparedClientBuilder()
            .accessTokenType(tokenType)
            .build();
        clientRepository.saveAndFlush(client);
        Account account = testAccountFactory.createPreparedBuilder().build();
        accountRepository.saveAndFlush(account);
        UUID id = UUID.randomUUID();
        Authorization auth = Authorization.withId(id)
            .client(client)
            .account(account)
            .grantType("code")
            .build();
        authorizationRepository.saveAndFlush(auth);
    }
}
