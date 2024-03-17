package ru.jankbyte.spring.oauth2.authorizationserver.repository;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Authority;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.account.Authority.AuthorityName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.List;
import java.util.Collection;
import java.util.Optional;

@Transactional(readOnly = true)
public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    List<Authority> findByNameIn(Collection<AuthorityName> names);
    Optional<Authority> findByName(AuthorityName name);
}
