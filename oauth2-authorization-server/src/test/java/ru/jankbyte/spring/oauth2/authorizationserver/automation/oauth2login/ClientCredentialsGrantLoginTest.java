package ru.jankbyte.spring.oauth2.authorizationserver.automation.oauth2login;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.jankbyte.spring.oauth2.authorizationserver.property.ClientCredential;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientCredentialsGrantLoginTest extends AbstractOAuth2LoginTest {
    private static final Logger log = LoggerFactory.getLogger(
        ClientCredentialsGrantLoginTest.class);

    @Autowired
    @Qualifier("clientCredentialsClient")
    private ClientCredential clientCredential;
    
    @Test
    public void shouldLoginWithRest() {
        Map<String, String> responseJson =
            oauth2RequestService.getTokensByClientCredential(clientCredential);
        log.debug("Response: {}", responseJson);
        assertThat(responseJson).isNotNull()
            .containsOnlyKeys("access_token", "token_type", "expires_in");
    }
}
