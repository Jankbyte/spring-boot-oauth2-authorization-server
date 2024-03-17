package ru.jankbyte.spring.oauth2.authorizationserver.automation.oauth2login;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.jankbyte.spring.oauth2.authorizationserver.property.*;
import ru.jankbyte.spring.oauth2.authorizationserver.urlbuilder.OAuth2UrlBuilder;

import java.util.Map;

import ru.jankbyte.spring.oauth2.authorizationserver.automation.page.LoginPage.ConsentPage;

public class DeviceCodeLoginTest extends AbstractOAuth2LoginTest {
    private static final Logger log = LoggerFactory.getLogger(
        DeviceCodeLoginTest.class);

    @Autowired
    private ClientCredential deviceAuthorizationTestClient;

    @Test
    public void shouldGetTokensByDeviceCode() {
        Map<String, String> response = oauth2RequestService.getDeviceCode(
            deviceAuthorizationTestClient);
        log.debug("The response: {}", response);
        assertThat(response).isNotEmpty()
            .containsOnlyKeys("user_code", "device_code",
                "verification_uri_complete",
                "verification_uri", "expires_in");
        String url = response.get("verification_uri_complete");
        loginAndSubmitConsents(url);
        String deviceCode = response.get("device_code");
        Map<String, String> tokensResponse =
            oauth2RequestService.getTokensByDeviceCode(
                deviceAuthorizationTestClient, deviceCode);
        log.debug("Tokens response: {}", tokensResponse);
        assertThat(tokensResponse).isNotNull()
            .containsOnlyKeys("access_token", "refresh_token",
                "expires_in", "token_type", "scope");
    }

    private void loginAndSubmitConsents(String url) {
        webDriver.navigate().to(url);
        String username = deviceAuthorizationTestClient.getClientId();
        String password = deviceAuthorizationTestClient.getClientSecret();
        loginPage.setCredentials(username, password);
        loginPage.clickLogin();
        ConsentPage consentPage = loginPage.getConsentPage();
        consentPage.selectCheckboxesWithConsents();
        consentPage.clickSubmitConsents();
    }
}
