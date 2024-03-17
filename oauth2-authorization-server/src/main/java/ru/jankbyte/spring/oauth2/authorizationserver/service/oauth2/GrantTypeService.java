package ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityReloader;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.GrantType;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.GrantType.GrantTypeName;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.GrantTypeRepository;

import java.util.Set;
import java.util.Collection;

@Service
public class GrantTypeService implements EntityReloader<Client> {
    private final GrantTypeRepository grantTypeRepository;

    public GrantTypeService(GrantTypeRepository grantTypeRepository) {
        this.grantTypeRepository = grantTypeRepository;
    }

    /**
     * Reload client grant types.
     * @param client The target client
     */
    @Override
    @Transactional(propagation = MANDATORY)
    public void reload(Client client) {
        Set<GrantType> grantTypes = client.getGrantTypes();
        Collection<GrantTypeName> grantTypeNames = grantTypes.stream()
            .map(GrantType::getName)
            .toList();
        grantTypes.clear();
        grantTypeRepository.findByNameIn(grantTypeNames)
            .forEach(client::addGrantType);
    }
}
