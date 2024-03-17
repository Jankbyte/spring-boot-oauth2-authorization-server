package ru.jankbyte.spring.oauth2.authorizationserver.repository.authorization;

import org.springframework.beans.factory.annotation.Autowired;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.AbstractOrderedJpaTest;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.AccountRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AccessTokenTypeRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AuthorizationRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.ClientRepository;

public abstract class AbstractAuthorizationRepositoryTest
        extends AbstractOrderedJpaTest {
    @Autowired
    protected ClientRepository clientRepository;

    @Autowired
    protected AuthorizationRepository authorizationRepository;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected AccessTokenTypeRepository tokenTypeRepository;
}