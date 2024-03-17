package ru.jankbyte.spring.oauth2.authorizationserver.automation;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.beans.factory.annotation.Autowired;

import ru.jankbyte.spring.oauth2.authorizationserver.TestMain;
import ru.jankbyte.spring.oauth2.authorizationserver.service.OAuth2RequestService;

import org.junit.jupiter.api.Test;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
    classes = {
        TestMain.class, OAuth2RequestService.class
    }
)
public class WellKnowEndPointTest {
    private static final Logger log = LoggerFactory.getLogger(
        OAuth2RequestService.class);

    @Autowired
    protected OAuth2RequestService oauth2RequestService;
    
    @Test
    public void shouldGetInformationFromWellknowEndpoint() {
        Map<String, Object>  response =
            oauth2RequestService.getDataFromWellknowEndpoint();
        log.debug("The response: {}", response);
    }

    @Test
    public void shouldGetInformationFromJwksEndpoint() {
        Map<String, Object>  response =
            oauth2RequestService.getDataFromJwksEndpoint();
        log.debug("The response: {}", response);
    }
}
