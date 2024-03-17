package ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2.tokencustomizer;

import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import java.util.Map;

import ru.jankbyte.spring.oauth2.authorizationserver.jpa.model.oauth2.Authorization;

public interface TokenClaimsCustomizer {
    Map<String, Object> getClaims(OAuth2TokenContext tokenContext);
}
