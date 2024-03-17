package ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2.tokencustomizer;

import java.util.Map;

import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

public abstract class AbstractOidcIdTokenClaimsCustomizer
        implements TokenClaimsCustomizer {
    @Override
    public final Map<String, Object> getClaims(
            OAuth2TokenContext tokenContext) {
        OidcUserInfo userInfo = getOidcUserInfo(tokenContext);
        return userInfo != null ? userInfo.getClaims() : null;
    }

    protected abstract OidcUserInfo getOidcUserInfo(
        OAuth2TokenContext tokenContext);
}
