package ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2.tokencustomizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OpaqueOAuth2TokenCustomizer
        implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {
    private static final Logger log = LoggerFactory.getLogger(
        OpaqueOAuth2TokenCustomizer.class);
    private final TokenClaimsCustomizer tokenClaimsCustomizer;

    public OpaqueOAuth2TokenCustomizer(
            TokenClaimsCustomizer accessTokenClaimsCustomizer) {
        this.tokenClaimsCustomizer = accessTokenClaimsCustomizer;
    }

    @Override
    public void customize(OAuth2TokenClaimsContext context) {
        log.trace("Customize Opaque token");
        Map<String, Object> customClaims =
            tokenClaimsCustomizer.getClaims(context);
        if (customClaims != null && !customClaims.isEmpty()) {
            OAuth2TokenClaimsSet.Builder claimsBuilder = context.getClaims();
            claimsBuilder.claims(claimsConsumer ->
                claimsConsumer.putAll(customClaims));
        }
    }
}
