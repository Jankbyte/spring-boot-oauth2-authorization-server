package ru.jankbyte.spring.oauth2.authorizationserver.service.security.oauth2;

import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsent;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;

import ru.jankbyte.spring.oauth2.authorizationserver.mapper.AuthorizationConsentMapper;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthorizationConsent;
import ru.jankbyte.spring.oauth2.authorizationserver.service.oauth2.AuthorizationConsentService;
import ru.jankbyte.spring.oauth2.authorizationserver.util.ToStringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;
import java.util.Collection;

@Service
public class JpaOAuth2AuthorizationConsentService
        implements OAuth2AuthorizationConsentService {
    private static final Logger log = LoggerFactory.getLogger(
        JpaOAuth2AuthorizationConsentService.class);
    private final AuthorizationConsentMapper consentMapper;
    private final AuthorizationConsentService consentService;

    public JpaOAuth2AuthorizationConsentService(
            AuthorizationConsentService consentService,
            AuthorizationConsentMapper consentMapper) {
        this.consentService = consentService;
        this.consentMapper = consentMapper;
    }
    
    @Override
    public void save(OAuth2AuthorizationConsent oauth2Consent) {
        printConsentInfo("Save consent", oauth2Consent);
        AuthorizationConsent consent =
            consentMapper.toAuthorizationConsent(oauth2Consent);
        consentService.saveOrUpdate(consent);
    }

    @Override
    public void remove(OAuth2AuthorizationConsent oauth2Consent) {
        printConsentInfo("Removed authorization consent", oauth2Consent);
        String accountName = oauth2Consent.getPrincipalName();
        String clientId = oauth2Consent.getRegisteredClientId();
        consentService.removeConsentById(clientId, accountName);
    }

    @Override
    public OAuth2AuthorizationConsent findById(
            String clientId, String principalName) {
        log.debug("Find consent by clientId={} and principalName={}",
            clientId, principalName);
        UUID clientUuid = UUID.fromString(clientId);
        OAuth2AuthorizationConsent oauth2Consent =
            consentService.getConsentById(clientUuid, principalName)
                .map(consentMapper::toOAuth2AuthorizationConsent)
                .orElse(null);
        printConsentInfo("Found consent", oauth2Consent);
        return oauth2Consent;
    }

    private void printConsentInfo(String message,
            OAuth2AuthorizationConsent oauth2Consent) {
        if (log.isDebugEnabled()) {
            String consentAsString = ToStringUtils.toString(oauth2Consent);
            log.debug("{}: {}", message, consentAsString);
        }
    }
}
