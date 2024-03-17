package ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityReloader;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Scope;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.ScopeRepository;

import java.util.Set;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ScopeService implements EntityReloader<Client> {
    private static final Logger log = LoggerFactory.getLogger(
        ScopeService.class);
    private final ScopeRepository scopeRepository;

    public ScopeService(ScopeRepository scopeRepository) {
        this.scopeRepository = scopeRepository;
    }

    /**
     * Refreshing client scopes.
     * <p>This method extract collection of scopes from client
     * and refresh that.</p>
     */
    @Override
    @Transactional(propagation = MANDATORY)
    public void reload(Client client) {
        log.debug("Loading scopes for client: {}", client.getAccount().getName());
        Set<Scope> scopes = client.getScopes();
        Collection<String> scopeNames = scopes.stream()
            .map(Scope::getName)
            .toList();
        scopes.clear();
        scopeRepository.findByNameIn(scopeNames)
            .forEach(client::addScope);
    }

    public Collection<Scope> getAllScopes() {
        return scopeRepository.findAll();
    }

    public boolean checkExistingScopeNames(Set<String> scopeNames) {
        int collectionCount = scopeNames.size();
        int dbCount = scopeRepository.countByNamesIn(scopeNames);
        return collectionCount == dbCount;
    }
}
