package ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityGraphNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthorizationConsent;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthorizationConsentId;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface AuthorizationConsentRepository
        extends JpaRepository<AuthorizationConsent, AuthorizationConsentId> {
    @EntityGraph(EntityGraphNames.AUTHORIZATION_CONSENT_LOAD_SCOPES)
    Optional<AuthorizationConsent> findById(AuthorizationConsentId id);
}
