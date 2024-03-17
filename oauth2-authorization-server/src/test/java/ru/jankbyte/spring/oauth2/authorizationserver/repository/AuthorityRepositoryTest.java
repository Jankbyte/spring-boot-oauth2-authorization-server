package ru.jankbyte.spring.oauth2.authorizationserver.repository;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.AbstractOrderedJpaTest;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Authority;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Authority.AuthorityName;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.AuthorityRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;

public class AuthorityRepositoryTest extends AbstractOrderedJpaTest {
    @Autowired
    private AuthorityRepository authorityRepository;

    @Test
    public void shouldFindAuthoritiesByNameIn() {
        AuthorityName[] authorityNamesArray = AuthorityName.values();
        Collection<AuthorityName> names = List.of(authorityNamesArray);
        Collection<Authority> authorities =
            authorityRepository.findByNameIn(names);
        assertThat(authorities).map(Authority::getName)
            .hasSize(2)
            .containsOnly(authorityNamesArray);
    }
}
