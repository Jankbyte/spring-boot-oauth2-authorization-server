package ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Builder;

/**
 * An customizer for {@link org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Builder}.
 * @see ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization
 * @see org.springframework.security.oauth2.server.authorization.OAuth2Authorization.Builder
 * @see ru.jankbyte.spring.oauth2.authorizationserver.mapper.AuthorizationMapper
 */
public interface AuthorizationCustomizer {
    /**
     * Customizing authorization.
     * @param authorizationBuilder The builder of authorization
     * @param entity The JPA entity of authorization
     */
    void customize(Builder authorizationBuilder, Authorization entity);
}
