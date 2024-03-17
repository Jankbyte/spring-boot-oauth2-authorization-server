package ru.jankbyte.spring.oauth2.authorizationserver.component.oauth2.tokencustomizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

import org.springframework.stereotype.Component;

// https://github.com/spring-projects/spring-authorization-server/blob/main/oauth2-authorization-server/src/main/java/org/springframework/security/oauth2/server/authorization/oidc/authentication/OidcUserInfoAuthenticationProvider.java
//@Component
public class OidcIdUserInfoMapper implements
        Function<OidcUserInfoAuthenticationContext, OidcUserInfo> {
    private static final Logger log = LoggerFactory.getLogger(
        OidcIdUserInfoMapper.class);

    @Override
    public OidcUserInfo apply(OidcUserInfoAuthenticationContext context) {
        OAuth2Authorization authorization = context.getAuthorization();
        OidcIdToken idToken = authorization.getToken(OidcIdToken.class).getToken();
        OAuth2AccessToken accessToken = context.getAccessToken();
        OidcUserInfo.Builder builder = OidcUserInfo.builder();
            //.claims(consumer -> consumer.putAll(
            //    principal.getTokenAttributes()));
        return builder.build();
    }
}
