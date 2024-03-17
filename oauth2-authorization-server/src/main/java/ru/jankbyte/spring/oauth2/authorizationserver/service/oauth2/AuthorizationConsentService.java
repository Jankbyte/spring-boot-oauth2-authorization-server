package ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthorizationConsent;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthorizationConsentId;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Scope;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;
import ru.jankbyte.spring.oauth2.authorizationserver.service.AccountService;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AuthorizationConsentRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.ClientRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.ScopeRepository;

import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthorizationConsentService {
    private static final Logger log = LoggerFactory.getLogger(
        AuthorizationConsentService.class);

    private final AccountService accountService;
    private final AuthorizationConsentRepository consentRepository;
    private final ScopeRepository scopeRepository;
    private final ClientRepository clientRepository;

    public AuthorizationConsentService(ClientService clientService,
            AccountService accountService,
            AuthorizationConsentRepository authorizationConsentRepository,
            ScopeRepository scopeRepository,
            ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
        this.accountService = accountService;
        this.scopeRepository = scopeRepository;
        this.consentRepository = authorizationConsentRepository;
    }

    @Transactional
    public void saveOrUpdate(AuthorizationConsent consent) {
        String accountName = consent.getAccount().getName();
        Account account = accountService.getAccountByName(accountName);
        consent.setAccount(account);
        UUID clientId = consent.getClient().getId();
        Client client = clientRepository.getReferenceById(clientId);
        consent.setClient(client);
        AuthorizationConsentId id = AuthorizationConsentId.from(
            account, client);
        consentRepository.findById(id).ifPresentOrElse(
            authConsent -> updateConsent(consent, authConsent),
            () -> saveNewConsent(consent));
    }

    @Transactional
    public void removeConsentById(String clientId,
            String accountName) {
        UUID clientUuid = UUID.fromString(clientId);
        Client client = clientRepository.getReferenceById(clientUuid);
        Account account = accountService.getAccountByName(accountName);
        AuthorizationConsentId id = AuthorizationConsentId.from(
            account, client);
        consentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<AuthorizationConsent> getConsentById(UUID clientId,
            String accountName) {
        Client client = clientRepository.getReferenceById(clientId);
        Account account = accountService.getAccountByName(accountName);
        AuthorizationConsentId id = AuthorizationConsentId.from(
            account, client);
        return consentRepository.findById(id);
    }

    private void updateConsent(AuthorizationConsent newConsent,
            AuthorizationConsent consent) {
        Collection<String> scopeNames =
            extractScopeNamesFromConsent(newConsent);
        addScopesToConsent(consent, scopeNames);
    }

    private void saveNewConsent(AuthorizationConsent consent) {
        Collection<String> scopeNames =
            extractScopeNamesFromConsent(consent);
        consent.getScopes().clear();
        addScopesToConsent(consent, scopeNames);
    }

    private void addScopesToConsent(AuthorizationConsent consent,
            Collection<String> scopeNames) {
        scopeRepository.findByNameIn(scopeNames)
            .forEach(consent::addScope);
        consentRepository.save(consent);
    }

    private static final Collection<String> extractScopeNamesFromConsent(
            AuthorizationConsent consent) {
        return consent.getScopes().stream()
            .map(Scope::getName)
            .toList();
    }
}
