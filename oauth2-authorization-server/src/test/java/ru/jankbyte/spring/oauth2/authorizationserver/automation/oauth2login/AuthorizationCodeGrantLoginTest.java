package ru.jankbyte.spring.oauth2.authorizationserver.automation.oauth2login;

import org.openqa.selenium.WebDriver;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.assertj.core.api.Assertions.entry;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import ru.jankbyte.spring.oauth2.authorizationserver.property.*;
import ru.jankbyte.spring.oauth2.authorizationserver.urlbuilder.OAuth2UrlBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.jankbyte.spring.oauth2.authorizationserver.utils.UrlUtils.getQueryParameters;

public class AuthorizationCodeGrantLoginTest
        extends AbstractOAuth2LoginTest {
    private static final Logger log = LoggerFactory.getLogger(
        AuthorizationCodeGrantLoginTest.class);

    @Autowired
    private ClientCredential jwtWithConsentsClient;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Test
    public void shouldGetAuthorizationCode() {
        String code = getAuthorizationCode();
        assertThat(code).isNotNull();
        Map<String, String> responseJsonAsMap =
            oauth2RequestService.getTokensByAuthorizationCode(
                jwtWithConsentsClient, code);
        log.debug("Response JSON: {}", responseJsonAsMap);
        assertThat(responseJsonAsMap).isNotNull()
            .containsOnlyKeys("access_token", "refresh_token",
                "expires_in", "token_type", "id_token", "scope");
        String jwtStr = responseJsonAsMap.get("access_token");
        extractingJwtAndCheckClaims(jwtStr);
        webDriver.close();
        webDriver.quit();
    }

    private String getAuthorizationCode() {
        String redirectUrl = jwtWithConsentsClient.getRedirectUrls()
            .stream()
            .findFirst()
            .get();
        String url = OAuth2UrlBuilder.withBaseURL(baseUrl)
            .path("/oauth2/authorize")
            .responseType("code")
            .clientId(jwtWithConsentsClient.getClientId())
            .scope(jwtWithConsentsClient.getScopes())
            .redirectUri(redirectUrl)
            .getAsString();
        webDriver.navigate().to(url);
        loginPage.setCredentials(accountCredential.getName(),
            accountCredential.getPassword());
        loginPage.clickLogin();
        loginPage.getConsentPage().selectCheckboxesWithConsents();
        loginPage.getConsentPage().clickSubmitConsents();
        String redirectedUrl = webDriver.getCurrentUrl();
        log.debug("The redirect URL: {}", redirectedUrl);
        Map<String, String> queryParams = getQueryParameters(redirectedUrl);
        return queryParams.get("code");
    }

    private void extractingJwtAndCheckClaims(String jwtStr) {
        Jwt jwt = jwtDecoder.decode(jwtStr);
        Map<String, Object> claims = jwt.getClaims();
        assertThat(claims).isNotNull()
            .contains(entry("sub",accountCredential.getName()),
                entry("aud", List.of(jwtWithConsentsClient.getClientId())),
                // entry("scope", List.of(clientCredential.getScopes()))
                entry("iss", baseUrl));
    }
}
