package ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2.tokencustomizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithm;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Map;

@Component
public class JwtOAuth2TokenCustomizer
        implements OAuth2TokenCustomizer<JwtEncodingContext> {
    private static final Logger log = LoggerFactory.getLogger(
        JwtOAuth2TokenCustomizer.class);
    private final TokenClaimsCustomizer accessTokenClaimsCustomizer,
        oidcIdTokenClaimsCustomizer;

    public JwtOAuth2TokenCustomizer(
            TokenClaimsCustomizer oidcIdTokenClaimsCustomizer,
            TokenClaimsCustomizer accessTokenClaimsCustomizer) {
        this.accessTokenClaimsCustomizer = accessTokenClaimsCustomizer;
        this.oidcIdTokenClaimsCustomizer = oidcIdTokenClaimsCustomizer;
    }

    @Override
    public void customize(JwtEncodingContext context) {
        OAuth2TokenType tokenType = context.getTokenType();
        String tokenValue = tokenType.getValue();
        log.trace("Customize JWT token (token type: {})", tokenValue);
        Map<String, Object> customClaims;
        if (tokenValue.equals(OidcParameterNames.ID_TOKEN)) {
            customClaims = oidcIdTokenClaimsCustomizer.getClaims(context);
        } else if (tokenValue.equals(
                OAuth2TokenType.ACCESS_TOKEN.getValue())) {
            customClaims = accessTokenClaimsCustomizer.getClaims(context);
        } else {
            throw new IllegalArgumentException(
                "Uknown token type value %s".formatted(tokenValue));
        }
        if (customClaims != null && !customClaims.isEmpty()) {
            JwtClaimsSet.Builder claimsBuilder = context.getClaims();
            claimsBuilder.claims(claimsConsumer ->
                claimsConsumer.putAll(customClaims));
        }
    }
}
