package ru.jankbyte.spring.oauth2.authorizationserver.repository;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.AbstractOrderedJpaTest;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.GrantType;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.GrantType.GrantTypeName;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.GrantTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import static java.util.List.of;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class GrantTypeRepositoryTest extends AbstractOrderedJpaTest {
    @Autowired
    private GrantTypeRepository grantTypeRepository;

    @Test
    public void shouldFindAllGrantTypesByNames() {
        GrantTypeName[] grantTypeNamesArray = GrantTypeName.values();
        Collection<GrantTypeName> grantTypeNames = of(grantTypeNamesArray);
        Collection<GrantType> grantTypes =
            grantTypeRepository.findByNameIn(grantTypeNames);
        assertThat(grantTypes)
            .map(GrantType::getName)
            .containsOnly(grantTypeNamesArray);
    }
}
