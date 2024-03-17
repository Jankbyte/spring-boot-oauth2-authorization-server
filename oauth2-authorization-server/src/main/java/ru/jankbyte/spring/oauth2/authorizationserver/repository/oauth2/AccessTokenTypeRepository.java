package ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AccessTokenType.TokenTypeValue;

import java.util.Optional;

@Transactional(readOnly = true)
public interface AccessTokenTypeRepository
        extends JpaRepository<AccessTokenType, Integer> {
    Optional<AccessTokenType> findByValue(TokenTypeValue value);
}
