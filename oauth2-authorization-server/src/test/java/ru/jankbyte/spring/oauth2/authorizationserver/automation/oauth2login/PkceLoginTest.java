package ru.jankbyte.spring.oauth2.authorizationserver.automation.oauth2login;

import ru.jankbyte.spring.oauth2.authorizationserver.property.ClientCredential;
import ru.jankbyte.spring.oauth2.authorizationserver.urlbuilder.OAuth2UrlBuilder;
import static ru.jankbyte.spring.oauth2.authorizationserver.utils.UrlUtils.getQueryParameters;
import static ru.jankbyte.spring.oauth2.authorizationserver.utils.PKCEUtils.getCodeChallenge;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PkceLoginTest extends AbstractOAuth2LoginTest {
    private static final Logger log = LoggerFactory.getLogger(
        PkceLoginTest.class);

    @Autowired
    private ClientCredential pkceClientCredential;

    @Test
    public void shouldGetAccessTokenWithCodeChallengeS256() {
        String codeVerifier = "my-test-code-verifier";
        String state = "my-test-state";
        String code = getAuthorizationCode(codeVerifier, state);
        log.debug("Generated code: {}", code);
        Map<String, String> responseValues =
            oauth2RequestService.getTokensByCodeChelange(
                pkceClientCredential, code, codeVerifier);
        log.debug("Generated response: {}", responseValues);
        assertThat(responseValues).isNotNull()
            .containsOnlyKeys("access_token", "scope",
                "token_type", "expires_in", "id_token");
        webDriver.close();
        webDriver.quit();
    }

    private String getAuthorizationCode(String codeVerifier, String state) {
        String codeChallenge = getCodeChallenge(codeVerifier);
        String redirectUrl = pkceClientCredential.getRedirectUrls()
            .stream()
            .findFirst()
            .get();
        String requestUrl = OAuth2UrlBuilder.withBaseURL(baseUrl)
            .path("/oauth2/authorize")
            .responseType("code")
            .clientId(pkceClientCredential.getClientId())
            .state(state)
            .scope(pkceClientCredential.getScopes())
            .redirectUri(redirectUrl)
            .codeChallenge(codeChallenge)
            .codeChallengeMethod("S256")
            .getAsString();
        log.debug("Generating code challenge URL: {}", requestUrl);
        webDriver.navigate().to(requestUrl);
        loginPage.setCredentials(accountCredential.getName(),
            accountCredential.getPassword());
        loginPage.clickLogin();
        String redirectedUrl = webDriver.getCurrentUrl();
        Map<String, String> queryParams = getQueryParameters(redirectedUrl);
        String code = queryParams.get("code");
        return code;
    }
}
