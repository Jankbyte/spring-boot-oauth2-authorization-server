package ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityQueryNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityGraphNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface AuthorizationRepository
        extends JpaRepository<Authorization, UUID> {
    @Query(name = EntityQueryNames.AUTHORIZATION_FIND_BY_ACCOUNT_NAME_AND_NON_EXPIRED)
    List<Authorization> findByAccountNameAndNonExpired(String name,
        OffsetDateTime currentDate);

    @Query(name = EntityQueryNames.AUTHORIZATION_FIND_BY_UNKNOWN_TOKEN)
    @EntityGraph(EntityGraphNames.AUTHORIZATION_FIND_BY_TOKEN)
    Optional<Authorization> findByUnknownToken(String token);

    @EntityGraph(EntityGraphNames.AUTHORIZATION_FIND_BY_TOKEN)
    Optional<Authorization> findByAuthorizationCodeValue(String value);

    @EntityGraph(EntityGraphNames.AUTHORIZATION_FIND_BY_TOKEN)
    Optional<Authorization> findByAccessTokenValue(String value);

    @EntityGraph(EntityGraphNames.AUTHORIZATION_FIND_BY_TOKEN)
    Optional<Authorization> findByRefreshTokenValue(String value);

    @EntityGraph(EntityGraphNames.AUTHORIZATION_FIND_BY_TOKEN)
    Optional<Authorization> findByOidcTokenValue(String value);

    @EntityGraph(EntityGraphNames.AUTHORIZATION_FIND_BY_TOKEN)
    Optional<Authorization> findByUserCodeValue(String value);

    @EntityGraph(EntityGraphNames.AUTHORIZATION_FIND_BY_TOKEN)
    Optional<Authorization> findByDeviceCodeValue(String value);

    @EntityGraph(EntityGraphNames.AUTHORIZATION_FIND_BY_TOKEN)
    Optional<Authorization> findByState(String state);

    @Modifying
    @Transactional
    @Query(name = EntityQueryNames.AUTHORIZATION_DELETE_EXPIRED)
    int deleteExpired(OffsetDateTime currentDate);
}
