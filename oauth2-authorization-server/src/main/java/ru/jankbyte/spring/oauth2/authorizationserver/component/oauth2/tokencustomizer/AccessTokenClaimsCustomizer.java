package ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2.tokencustomizer;

import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2.DefaultAuthorizationCustomizer.AttributeNames;

@Component
public class AccessTokenClaimsCustomizer implements TokenClaimsCustomizer {
    private static final Logger log = LoggerFactory.getLogger(
        AccessTokenClaimsCustomizer.class);

    @Override
    public Map<String, Object> getClaims(OAuth2TokenContext tokenContext) {
        OAuth2TokenType tokenType = tokenContext.getTokenType();
        log.debug("Token type: {}", tokenType.getValue());
        OAuth2Authorization authorization = tokenContext.getAuthorization();
        if (authorization == null) {
            return null;
        }
        String accountId = authorization.getAttribute(
            AttributeNames.ACCOUNT_ID);
        return Map.of("account_id", accountId);
    }
}
