package ru.jankbyte.spring.oauth2.authorizationserver.urlbuilder;

import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.Collection;
import static java.util.stream.Collectors.joining;

public final class FormParamBodyBuilder {
    private final MultiValueMap<String, String> formParams =
        new LinkedMultiValueMap<>();

    public static FormParamBodyBuilder builder() {
        return new FormParamBodyBuilder();
    }

    private FormParamBodyBuilder() {}

    public FormParamBodyBuilder deviceCode(String deviceCode) {
        formParams.add(OAuth2ParameterNames.DEVICE_CODE, deviceCode);
        return this;
    }

    public FormParamBodyBuilder grantType(String grantType) {
        formParams.add(OAuth2ParameterNames.GRANT_TYPE, grantType);
        return this;
    }

    public FormParamBodyBuilder scope(Collection<String> scopes) {
        String scopesStr = scopes.stream()
            .collect(joining(" "));
        formParams.add(OAuth2ParameterNames.SCOPE, scopesStr);
        return this;
    }

    public FormParamBodyBuilder redirectUri(String redirectUri) {
        formParams.add(OAuth2ParameterNames.REDIRECT_URI, redirectUri);
        return this;
    }

    public FormParamBodyBuilder clientId(String clientId) {
        formParams.add(OAuth2ParameterNames.CLIENT_ID, clientId);
        return this;
    }

    public FormParamBodyBuilder clientSecret(String clientSecret) {
        formParams.add(OAuth2ParameterNames.CLIENT_SECRET, clientSecret);
        return this;
    }

    public FormParamBodyBuilder codeVerifier(String code) {
        formParams.add("code_verifier", code);
        return this;
    }

    public FormParamBodyBuilder credentials(String clientId,
            String clientSecret) {
        return clientId(clientId).clientSecret(clientSecret);
    }

    public FormParamBodyBuilder code(String code) {
        formParams.add(OAuth2ParameterNames.CODE, code);
        return this;
    }

    public MultiValueMap<String, String> build() {
        return formParams;
    }
}
