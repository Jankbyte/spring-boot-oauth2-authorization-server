package ru.jankbyte.spring.oauth2.authorizationserver.repository;

import org.springframework.context.annotation.Import;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.AbstractOrderedJpaTest;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Authority;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Authority.AuthorityName;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.AuthorityRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.AccountRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.factory.TestAccountFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Import(TestAccountFactory.class)
public class AccountRepositoryTest extends AbstractOrderedJpaTest {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private TestAccountFactory testAccountFactory;

    @Test
    public void shouldSaveNewAccount() {
        Collection<Authority> authorities =
            authorityRepository.findByNameIn(List.of(
                AuthorityName.CREATE_OAUTH2_CLIENT));
        Account newAccount =
            testAccountFactory.createAccountWithAuthorities(authorities);
        accountRepository.saveAndFlush(newAccount);
        UUID id = newAccount.getId();
        entityManager.clear();
        Account account = accountRepository.findById(id).get();
        assertThat(account).extracting("name", "id",
                "authorities.size", "enabled", "password")
            .contains(newAccount.getName(), id,
                newAccount.getAuthorities().size(),
                newAccount.isEnabled(),
                newAccount.getPassword());
    }

    @Test
    public void shouldReplaceAuthority() {
        Collection<Authority> authorities =
            authorityRepository.findByNameIn(List.of(
                AuthorityName.CREATE_OAUTH2_CLIENT));
        Account account =
            testAccountFactory.createAccountWithAuthorities(authorities);
        accountRepository.saveAndFlush(account);
        UUID accountId = account.getId();
        entityManager.clear();
        account = accountRepository.findById(accountId).get();
        Collection<Authority> accountAuthorities = account.getAuthorities();
        assertThat(accountAuthorities).hasSize(1);
        Authority authority = authorityRepository.findByName(
            AuthorityName.CREATE_OAUTH2_CLIENT).get();
        account.getAuthorities().remove(authority);
        accountRepository.saveAndFlush(account);
        entityManager.clear();
        account = accountRepository.findById(accountId).get();
        accountAuthorities = account.getAuthorities();
        assertThat(accountAuthorities).hasSize(0);
    }
}
