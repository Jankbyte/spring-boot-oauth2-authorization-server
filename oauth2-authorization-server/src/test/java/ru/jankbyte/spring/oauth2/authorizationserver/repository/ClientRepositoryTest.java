package ru.jankbyte.spring.oauth2.authorizationserver.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.GrantType.GrantTypeName;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthenticationMethod.AuthenticationMethodName;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType.TokenTypeValue;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.*;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AuthenticationMethodRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.GrantTypeRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AccessTokenTypeRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.ClientRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.AbstractOrderedJpaTest;
import ru.jankbyte.spring.oauth2.authorizationserver.configuration.BaseTestConfiguration;
import ru.jankbyte.spring.oauth2.authorizationserver.factory.TestClientFactory;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.UUID;
import java.util.Collection;

@Import({
    BaseTestConfiguration.class,
    TestClientFactory.class
})
public class ClientRepositoryTest extends AbstractOrderedJpaTest {
    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private TestClientFactory testClientFactory;

    @Autowired
    private AuthenticationMethodRepository methodRepository;

    @Autowired
    private AccessTokenTypeRepository tokenTypeRepository;

    @Autowired
    private GrantTypeRepository grantTypeRepository;
    
    @Test
    public void shouldSaveNewClient() {
        AuthenticationMethodName[] allMethods =
            AuthenticationMethodName.values();
        Collection<AuthenticationMethodName> methodNames =
            List.of(allMethods);
        Collection<AuthenticationMethod> methods =
            methodRepository.findByNameIn(methodNames);
        GrantTypeName[] allGrants = GrantTypeName.values();
        Collection<GrantTypeName> grantTypesNames = List.of(allGrants);
        Collection<GrantType> grantTypes =
            grantTypeRepository.findByNameIn(grantTypesNames);
        AccessTokenType tokenType = tokenTypeRepository.findByValue(
            TokenTypeValue.JWT).get();
        Client newClient = testClientFactory.createPreparedClientBuilder()
            .authenticationMethods(methods)
            .grantTypes(grantTypes)
            .accessTokenType(tokenType)
            .build();
        clientRepository.saveAndFlush(newClient);
        UUID id = newClient.getId();
        entityManager.clear();
        Client client = clientRepository.findById(id).get();
        assertThat(client).extracting(
                "authenticationMethods.size", "grantTypes.size",
                "accessTokenType.value")
            .contains(allMethods.length, allGrants.length,
                TokenTypeValue.JWT);
    }
}
