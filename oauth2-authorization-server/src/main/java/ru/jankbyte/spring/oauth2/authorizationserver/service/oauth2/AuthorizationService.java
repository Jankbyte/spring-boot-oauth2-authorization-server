package ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.jankbyte.spring.oauth2.authorizationserver.service.AccountService;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AuthorizationRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.ClientRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.token.*;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;

import java.time.Clock;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.Set;
import java.util.Optional;

@Service
public class AuthorizationService {
    private final AuthorizationRepository authorizationRepository;
    private final Clock clock;
    private final ClientRepository clientRepository;
    private final AccountService accountService;

    public AuthorizationService(Clock clock,
            AuthorizationRepository authorizationRepository,
            ClientRepository clientRepository,
            AccountService accountService) {
        this.clock = clock;
        this.clientRepository = clientRepository;
        this.accountService = accountService;
        this.authorizationRepository = authorizationRepository;
    }

    @Autowired
    public AuthorizationService(ClientRepository clientRepository,
            AccountService accountService,
            AuthorizationRepository authorizationRepository) {
        this(Clock.systemDefaultZone(), authorizationRepository,
            clientRepository, accountService);
    }

    public List<Authorization> getAuthorizationsByAccountName(
            String accountName) {
        OffsetDateTime currentDate = OffsetDateTime.now(clock);
        return authorizationRepository.findByAccountNameAndNonExpired(
            accountName, currentDate);
    }

    public void removeAuthorizationById(String authorizationId) {
        UUID id = UUID.fromString(authorizationId);
        authorizationRepository.deleteById(id);
    }

    public Optional<Authorization> getAuthorizationById(String id) {
        UUID uuid = UUID.fromString(id);
        return authorizationRepository.findById(uuid);
    }

    @Transactional
    public void saveOrUpdateAuthorization(Authorization newAuthorization) {
        UUID id = newAuthorization.getId();
        authorizationRepository.findById(id).ifPresentOrElse(
            authorization -> updateAuthorization(newAuthorization,
                authorization),
            () -> createNewAuthorization(newAuthorization));
    }

    private void createNewAuthorization(Authorization newAuthorization) {
        String accountName = newAuthorization.getAccount().getName();
        Account account = accountService.getAccountByName(accountName);
        UUID clientId = newAuthorization.getClient().getId();
        Client client = clientRepository.getReferenceById(clientId);
        UUID id = newAuthorization.getId();
        Authorization authorization = Authorization.withId(id)
            .client(client)
            .account(account)
            .build();
        updateAuthorization(newAuthorization, authorization);
    }

    private void updateAuthorization(Authorization newAuthorization,
            Authorization authorization) {
        Set<String> scopes = newAuthorization.getScopes();
        authorization.setScopes(scopes);
        String grantTypeValue = newAuthorization.getGrantType();
        authorization.setGrantType(grantTypeValue);
        Map<String, Object> attributes = newAuthorization.getAttributes();
        authorization.setAttributes(attributes);
        String state = newAuthorization.getState();
        authorization.setState(state);
        AuthorizationCode newAuthorizationCode =
            newAuthorization.getAuthorizationCode();
        if (newAuthorizationCode != null) {
            authorization.setAuthorizationCode(newAuthorizationCode);
        }
        AccessToken newAccessToken = newAuthorization.getAccessToken();
        if (newAccessToken != null) {
            authorization.setAccessToken(newAccessToken);
        }
        RefreshToken newRefreshToken = newAuthorization.getRefreshToken();
        if (newRefreshToken != null) {
            authorization.setRefreshToken(newRefreshToken);
        }
        OidcToken newOidcIdToken = newAuthorization.getOidcToken();
        if (newOidcIdToken != null) {
            authorization.setOidcToken(newOidcIdToken);
        }
        UserCode newUserCode = newAuthorization.getUserCode();
        if (newUserCode != null) {
            authorization.setUserCode(newUserCode);
        }
        DeviceCode newDeviceCode = newAuthorization.getDeviceCode();
        if (newDeviceCode != null) {
            authorization.setDeviceCode(newDeviceCode);
        }
        authorizationRepository.saveAndFlush(authorization);
    }
}
