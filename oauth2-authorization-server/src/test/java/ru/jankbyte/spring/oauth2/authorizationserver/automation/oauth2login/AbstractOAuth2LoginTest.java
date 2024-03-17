package ru.jankbyte.spring.oauth2.authorizationserver.automation.oauth2login;

import ru.jankbyte.spring.oauth2.authorizationserver.TestMain;
import ru.jankbyte.spring.oauth2.authorizationserver.Main;
import ru.jankbyte.spring.oauth2.authorizationserver.configuration.AutomationTestConfiguration;
import ru.jankbyte.spring.oauth2.authorizationserver.service.OAuth2RequestService;
import ru.jankbyte.spring.oauth2.authorizationserver.property.*;
import ru.jankbyte.spring.oauth2.authorizationserver.automation.page.LoginPage;
import ru.jankbyte.spring.oauth2.authorizationserver.configuration.TestContainersConfiguration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
    classes = {
        TestMain.class, AutomationTestConfiguration.class,
        OAuth2RequestService.class
    }
)
public abstract class AbstractOAuth2LoginTest {
    protected LoginPage loginPage;
    protected WebDriver webDriver;

    @Value("${baseUrl}")
    protected String baseUrl;

    @Autowired
    protected AccountCredential accountCredential;

    @Autowired
    protected OAuth2RequestService oauth2RequestService;
    
    @BeforeEach
    protected final void initializeWebDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        webDriver = new ChromeDriver(chromeOptions);
        Options options = webDriver.manage();
        Window window = options.window();
        window.maximize();
        loginPage = new LoginPage(webDriver);
    }

    @AfterEach
    protected final void shutdownDriver() {
        webDriver.quit();
    }
}
