package ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityReloader;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AuthenticationMethodRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthenticationMethod;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthenticationMethod.AuthenticationMethodName;

import java.util.Set;
import java.util.Collection;

@Service
public class AuthenticationMethodService implements EntityReloader<Client> {
    private final AuthenticationMethodRepository methodRepository;

    public AuthenticationMethodService(
            AuthenticationMethodRepository methodRepository) {
        this.methodRepository = methodRepository;
    }

    /**
     * Reload client authentication methods.
     * @param client The target client
     */
    @Override
    @Transactional(propagation = MANDATORY)
    public void reload(Client client) {
        Set<AuthenticationMethod> methods = client.getAuthenticationMethods();
        Collection<AuthenticationMethodName> methodNames = methods.stream()
            .map(AuthenticationMethod::getName)
            .toList();
        methods.clear();
        methodRepository.findByNameIn(methodNames)
            .forEach(client::addAuthenticationMethod);
    }
}
