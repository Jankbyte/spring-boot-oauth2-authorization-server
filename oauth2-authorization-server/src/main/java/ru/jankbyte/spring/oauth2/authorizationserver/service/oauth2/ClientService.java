package ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityReloader;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.ClientRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.exception.repository.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class ClientService {
    private final ClientRepository clientRepository;
    private List<EntityReloader<Client>> clientReloaders;

    public ClientService(ClientRepository clientRepository,
            List<EntityReloader<Client>> clientReloaders) {
        this.clientRepository = clientRepository;
        this.clientReloaders = clientReloaders;
    }

    @Transactional
    public void saveNewClient(Client client) {
        clientReloaders.forEach(reloader -> reloader.reload(client));
        clientRepository.saveAndFlush(client);
    }

    public Client getClientByAccountName(String clientId) {
        return clientRepository.findByAccountName(clientId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Client with clientId=%s not found".formatted(clientId)));
    }

    public Client getClientById(UUID id) {
        return clientRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                "Client with id=%s not found".formatted(id)));
    }
}
