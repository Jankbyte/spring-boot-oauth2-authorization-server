package ru.jankbyte.spring.oauth2.authorizationserver.urlbuilder;

import org.springframework.web.util.UriComponentsBuilder;
import java.util.stream.Collectors;
import java.util.Collection;

public class OAuth2UrlBuilder {
    private final UriComponentsBuilder linkBuilder;
    
    public static OAuth2UrlBuilder withBaseURL(String baseUrl) {
        return new OAuth2UrlBuilder(baseUrl);
    }

    public static OAuth2UrlBuilder builder() {
        return withBaseURL("");
    }

    private OAuth2UrlBuilder(String baseUrl) {
        linkBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);
    }

    public OAuth2UrlBuilder path(String path) {
        linkBuilder.path(path);
        return this;
    }

    public OAuth2UrlBuilder responseType(
            String responseType) {
        linkBuilder.queryParam("response_type", responseType);
        return this;
    }

    public OAuth2UrlBuilder clientId(String clientId) {
        linkBuilder.queryParam("client_id", clientId);
        return this;
    }

    public OAuth2UrlBuilder scope(Collection<String> scopes) {
        String joinedScopes = scopes.stream()
            .collect(Collectors.joining("%20"));
        linkBuilder.queryParam("scope", joinedScopes);
        return this;
    }

    public OAuth2UrlBuilder redirectUri(String redirectUri) {
        linkBuilder.queryParam("redirect_uri", redirectUri);
        return this;
    }

    public OAuth2UrlBuilder state(String state) {
        linkBuilder.queryParam("state", state);
        return this;
    }

    public OAuth2UrlBuilder codeChallenge(String codeChallenge) {
        linkBuilder.queryParam("code_challenge", codeChallenge);
        return this;
    }

    public OAuth2UrlBuilder codeChallengeMethod(String codeChallengeMethod) {
        linkBuilder.queryParam("code_challenge_method", codeChallengeMethod);
        return this;
    }

    public OAuth2UrlBuilder code(String code) {
        linkBuilder.queryParam("code", code);
        return this;
    }

    public OAuth2UrlBuilder grantType(String grantType) {
        linkBuilder.queryParam("grant_type", grantType);
        return this;
    }
    
    public OAuth2UrlBuilder param(String paramName, String value) {
        linkBuilder.queryParam(paramName, value);
        return this;
    }

    public String getAsString() {
        return linkBuilder.build().toString();
    }
}
