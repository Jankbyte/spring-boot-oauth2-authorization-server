package ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.ClientAccount;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.service.AccountService;

import java.time.temporal.ChronoUnit;
import java.time.Clock;
import java.time.OffsetDateTime;
import java.time.Duration;

import java.util.UUID;

@Service
public class ClientServiceFacade {
    private final PasswordEncoder passwordEncoder;
    private final ClientService clientService;
    private final AccountService accountService;
    private final Clock clock;

    public ClientServiceFacade(Clock clock, PasswordEncoder passwordEncoder,
            ClientService clientService, AccountService accountService) {
        this.clock = clock;
        this.passwordEncoder = passwordEncoder;
        this.clientService = clientService;
        this.accountService = accountService;
    }

    @Autowired
    public ClientServiceFacade(PasswordEncoder passwordEncoder,
            ClientService clientService, AccountService accountService) {
        this(Clock.systemDefaultZone(),
            passwordEncoder, clientService, accountService);
    }

    @Transactional
    public void createNewClientAndEncodePassword(Client client) {
        ClientAccount account = client.getAccount();
        String password = passwordEncoder.encode(account.getPassword());
        account.setPassword(password);
        saveNewClient(client);
    }

    @Transactional
    public void createNewClient(Client client) {
        saveNewClient(client);
    }

    private void saveNewClient(Client client) {
        ClientAccount account = client.getAccount();
        // client account should be enabled by default everytime
        account.setEnabled(true);
        clientService.saveNewClient(client);
    }
}
