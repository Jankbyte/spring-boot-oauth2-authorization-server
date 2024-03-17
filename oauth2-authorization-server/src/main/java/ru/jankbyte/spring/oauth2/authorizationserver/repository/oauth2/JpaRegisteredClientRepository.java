package ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2.ClientServiceFacade;
import ru.jankbyte.spring.oauth2.authorizationserver.mapper.ClientMapper;

import java.util.UUID;

@Repository
public class JpaRegisteredClientRepository
        implements RegisteredClientRepository {
    private static final Logger log = LoggerFactory.getLogger(
        JpaRegisteredClientRepository.class);
    private final ClientRepository clientRepository;
    private final ClientServiceFacade clientService;
    private final ClientMapper clientMapper;

    public JpaRegisteredClientRepository(ClientRepository clientRepository,
            ClientMapper clientMapper, ClientServiceFacade clientService) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.clientService = clientService;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        log.debug("Save client: {}", registeredClient);
        Client client = clientMapper.toClient(registeredClient);
        clientService.createNewClient(client);
        log.debug("Client saved with ID: {}", client.getId());
    }

    @Override
    public RegisteredClient findById(String id) {
        log.debug("Find client by ID: {}", id);
        UUID uuid = UUID.fromString(id);
        RegisteredClient registeredClient = clientRepository.findById(uuid)
            .map(clientMapper::toRegisteredClient)
            .orElse(null);
        log.debug("Found client: {}", registeredClient);
        return registeredClient;
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        log.debug("Find client by clientID: {}", clientId);
        RegisteredClient registeredClient =
            clientRepository.findByAccountName(clientId)
                .map(clientMapper::toRegisteredClient)
                .orElse(null);
        log.debug("Found client: {}", registeredClient);
        return registeredClient;
    }
}
