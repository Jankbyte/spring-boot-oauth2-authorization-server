package ru.jankbyte.spring.oauth2.authorizationserver.repository.oauth2;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthenticationMethod;
import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.AuthenticationMethod.AuthenticationMethodName;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Collection;

@Transactional(readOnly = true)
public interface AuthenticationMethodRepository
        extends JpaRepository<AuthenticationMethod, Integer> {
    List<AuthenticationMethod> findByNameIn(
        Collection<AuthenticationMethodName> names);
}
