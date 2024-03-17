package ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityGraphNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Client;

@Transactional(readOnly = true)
public interface ClientRepository extends JpaRepository<Client, UUID> {
    @Modifying
    @EntityGraph(EntityGraphNames.CLIENT_FIND_BY_ID)
    Optional<Client> findById(UUID id);

    @EntityGraph(EntityGraphNames.CLIENT_FIND_BY_ID)
    Optional<Client> findByAccountName(String accountName);

    boolean existsByAccountName(String namedId);
}
