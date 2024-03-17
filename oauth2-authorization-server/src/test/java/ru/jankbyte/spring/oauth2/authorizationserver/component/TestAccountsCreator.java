package ru.jankbyte.spring.oauth2.authorizationserver.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.service.AccountServiceFacade;
import ru.jankbyte.spring.oauth2.authorizationserver.property.AccountCredential;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

@TestComponent
public class TestAccountsCreator implements DatabaseCreator {
    private static final Logger log = LoggerFactory.getLogger(
        TestClientsCreator.class);
    private final AccountServiceFacade accountServiceFacade;
    private final Collection<AccountCredential> credentials;

    public TestAccountsCreator(Collection<AccountCredential> credentials,
            AccountServiceFacade accountServiceFacade) {
        this.accountServiceFacade = accountServiceFacade;
        this.credentials = credentials;
    }

    @Override
    public void create() {
        log.debug("Creating test accounts");
        credentials.stream()
            .map(TestAccountsCreator::toAccount)
            .forEach(accountServiceFacade::prepareAndSaveAccount);
    }

    private static Account toAccount(AccountCredential credential) {
        return Account.builder()
            .id(UUID.randomUUID())
            .name(credential.getName())
            .password(credential.getPassword())
            .build();
    }
}