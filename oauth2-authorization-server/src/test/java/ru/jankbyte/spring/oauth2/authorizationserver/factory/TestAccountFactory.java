package ru.jankbyte.spring.oauth2.authorizationserver.factory;

import org.springframework.boot.test.context.TestComponent;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Authority;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account.AccountBuilder;

import java.util.Collection;
import java.util.UUID;

@TestComponent
public final class TestAccountFactory {
    public AccountBuilder<? extends Account> createPreparedBuilder() {
        return Account.builder()
            .id(UUID.randomUUID())
            .name("testaccount")
            .password("testpassword")
            .enabled(true);
    }

    public Account createAccountWithAuthorities(
            Collection<Authority> authorities) {
        return createPreparedBuilder()
            .authorities(authorities)
            .build();
    }
}
