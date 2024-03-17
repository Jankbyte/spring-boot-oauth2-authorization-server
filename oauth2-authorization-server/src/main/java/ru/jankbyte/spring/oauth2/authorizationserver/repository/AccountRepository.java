package ru.jankbyte.spring.oauth2.authorizationserver.repository;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityGraphNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.UUID;
import java.util.Optional;

@Transactional(readOnly = true)
public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByName(String name);

    @EntityGraph(EntityGraphNames.ACCOUNT_FIND_BY_NAME)
    Optional<Account> findByName(String name);
}
