package ru.jankbyte.spring.oauth2.authorizationserver.service;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;

import ru.jankbyte.spring.oauth2.authorizationserver.property.ClientCredential;
import ru.jankbyte.spring.oauth2.authorizationserver.urlbuilder.FormParamBodyBuilder;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TestComponent
public class OAuth2RequestService {
    private static final Logger log = LoggerFactory.getLogger(
        OAuth2RequestService.class);
    private static final ParameterizedTypeReference<Map<String, String>>
        mapTypeReferences = new ParameterizedTypeReference<>() {};
    private static final ParameterizedTypeReference<Map<String, Object>>
        jsonReferences = new ParameterizedTypeReference<>() {};

    private final RestClient restClient;

    public OAuth2RequestService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Map<String, Object> getDataFromJwksEndpoint() {
        return restClient.get().uri("/oauth2/jwks")
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .body(jsonReferences);
    }

    public Map<String, Object> getDataFromWellknowEndpoint() {
        return restClient.get().uri("/.well-known/openid-configuration")
            .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .body(jsonReferences);
    }

    public Map<String, String> getTokensByDeviceCode(
                ClientCredential credential, String deviceCode) {
        String grantTypeDeviceCode = "urn:ietf:params:oauth:grant-type:device_code";
        MultiValueMap<String, String> formParams =
            FormParamBodyBuilder.builder().deviceCode(deviceCode)
                .grantType(grantTypeDeviceCode)
                .credentials(credential.getClientId(),
                    credential.getClientSecret())
                .build();
        return restClient.post().uri("/oauth2/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formParams)
            .retrieve()
            .body(mapTypeReferences);
    }

    public Map<String, String> getDeviceCode(ClientCredential credential) {
        MultiValueMap<String, String> formParams =
            FormParamBodyBuilder.builder().scope(credential.getScopes())
                .credentials(credential.getClientId(),
                    credential.getClientSecret())
                .build();
        return restClient.post().uri("/oauth2/device_authorization")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formParams)
            .retrieve()
            .body(mapTypeReferences);
    }

    public Map<String, String> getTokensByClientCredential(
            ClientCredential credential) {
        MultiValueMap<String, String> formParams =
            FormParamBodyBuilder.builder()
                .grantType(AuthorizationGrantType.CLIENT_CREDENTIALS
                    .getValue())
                .credentials(credential.getClientId(),
                    credential.getClientSecret())
                .build();
        return restClient.post().uri("/oauth2/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formParams)
            .retrieve()
            .body(mapTypeReferences);
    }

    public Map<String, String> getTokensByCodeChelange(
            ClientCredential credential, String code, String codeVerifier) {
        String redirectUrl = credential.getRedirectUrls().stream()
            .findFirst()
            .get();
        MultiValueMap<String, String> formParams =
            FormParamBodyBuilder.builder()
                .grantType(AuthorizationGrantType.AUTHORIZATION_CODE
                    .getValue())
                .redirectUri(redirectUrl)
                .code(code)
                .codeVerifier(codeVerifier)
                .clientId(credential.getClientId())
                .build();
        return restClient.post().uri("/oauth2/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formParams)
            .retrieve()
            .body(mapTypeReferences);
    }

    public Map<String, String> getTokensByAuthorizationCode(
            ClientCredential credential, String authorizationCode) {
        String redirectUrl = credential.getRedirectUrls().stream()
            .findFirst()
            .get();
        MultiValueMap<String, String> formParams =
            FormParamBodyBuilder.builder()
                .grantType(AuthorizationGrantType.AUTHORIZATION_CODE
                    .getValue())
                .redirectUri(redirectUrl)
                .code(authorizationCode)
                .credentials(credential.getClientId(),
                    credential.getClientSecret())
                .build();
        return restClient.post().uri("/oauth2/token")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formParams)
            .retrieve()
            .body(mapTypeReferences);
    }
}
