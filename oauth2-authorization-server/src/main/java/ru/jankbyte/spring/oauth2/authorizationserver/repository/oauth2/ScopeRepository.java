package ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2;

import java.util.Optional;
import java.util.Set;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.EntityQueryNames;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Scope;

@Transactional(readOnly = true)
public interface ScopeRepository extends JpaRepository<Scope, Integer> {
    Collection<Scope> findByNameIn(Collection<String> names);

    boolean existsByName(String name);

    @Query(name = EntityQueryNames.SCOPE_COUNT_BY_NAMES)
    int countByNamesIn(Set<String> scopeNames);
}
