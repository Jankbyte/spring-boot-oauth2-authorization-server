package ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2.tokencustomizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class OidcIdTokenClaimsCustomizer
        extends AbstractOidcIdTokenClaimsCustomizer {
    private static final Logger log = LoggerFactory.getLogger(
        OidcIdTokenClaimsCustomizer.class);

    @Override
    protected OidcUserInfo getOidcUserInfo(OAuth2TokenContext tokenContext) {
        OAuth2Authorization authorization = tokenContext.getAuthorization();
        if (authorization == null) {
            return null;
        }
        // FIX: thats ignoring PROFILE scope
        OidcUserInfo.Builder builder = OidcUserInfo.builder()
            .nickname(authorization.getPrincipalName());
        Collection<String> scopes = authorization.getAuthorizedScopes();
        if (scopes.contains("account_id")) {
            String accountId = authorization.getAttribute("accountId");
            builder.claim("account_id", accountId);
        }
        return builder.build();
    }
}
