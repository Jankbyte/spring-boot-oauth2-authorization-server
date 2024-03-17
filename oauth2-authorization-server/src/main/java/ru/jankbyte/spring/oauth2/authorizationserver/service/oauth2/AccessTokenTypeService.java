package ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityReloader;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.AccessTokenTypeRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType.TokenTypeValue;
import ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2.GrantTypeRepository;
import ru.jankbyte.spring.oauth2.authorizationserver.exception.repository.EntityNotFoundException;

import java.util.Set;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AccessTokenTypeService implements EntityReloader<Client> {
    private static final Logger log = LoggerFactory.getLogger(
        AccessTokenTypeService.class);
    private final AccessTokenTypeRepository tokenTypeRepository;

    public AccessTokenTypeService(
            AccessTokenTypeRepository tokenTypeRepository) {
        this.tokenTypeRepository = tokenTypeRepository;
    }

    /**
     * Reload client access token type.
     * @param client The target client
     */
    @Override
    @Transactional(propagation = MANDATORY)
    public void reload(Client client) {
        log.debug("Reloading client AccessTokenType");
        AccessTokenType accessTokenType = client.getAccessTokenType();
        TokenTypeValue tokenTypeValue = accessTokenType.getValue();
        tokenTypeRepository.findByValue(tokenTypeValue).ifPresentOrElse(
            tokenType -> tokenType.addClient(client), () -> {
                String message = "Token type not found (%s)".formatted(
                    tokenTypeValue);
                throw new EntityNotFoundException(message);
             });
    }
}
