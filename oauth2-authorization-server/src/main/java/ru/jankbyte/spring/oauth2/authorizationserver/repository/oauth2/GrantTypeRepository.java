package ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.GrantType;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.GrantType.GrantTypeName;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Collection;

@Transactional(readOnly = true)
public interface GrantTypeRepository
        extends JpaRepository<GrantType, Integer> {
    List<GrantType> findByNameIn(Collection<GrantTypeName> names);
}
