package ru.jankbyte.spring.oauth2.authorizationserver.automation.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

public class LoginPage extends AbstractPage {
    private static final String SUBMIT_BUTTON_ID = "submitButton";
    private static final String INPUT_USERNAME_ID = "usernameInput";
    private static final String INPUT_PASSWORD_ID = "passwordInput";

    private final ConsentPage consentPage;

    public LoginPage(WebDriver webDriver) {
        super(webDriver);
        this.consentPage = new ConsentPage();
    }

    public void setCredentials(String username, String password) {
        WebElement usernameField = webDriver.findElement(
            By.id(INPUT_USERNAME_ID));
        usernameField.sendKeys(username);
        WebElement passwordField = webDriver.findElement(
            By.id(INPUT_PASSWORD_ID));
        passwordField.sendKeys(password);
    }
    
    public void clickLogin() {
        WebElement signButton = webDriver.findElement(
            By.id(SUBMIT_BUTTON_ID));
        signButton.click();
    }

    public ConsentPage getConsentPage() {
        return consentPage;
    }

    public class ConsentPage {
        public static final String CONSENT_CHECKBOX_NAME = "scope";
        private static final String SUBMIT_CONSENT_BUTTON_ID = "submit-consent";

        protected ConsentPage() {}

        public void clickSubmitConsents() {
            WebElement submitConsentButton = webDriver.findElement(
                By.id(SUBMIT_CONSENT_BUTTON_ID));
            submitConsentButton.click();
        }

        public void selectCheckboxesWithConsents() {
            List<WebElement> consentCheckboxes = webDriver.findElements(
                By.name(CONSENT_CHECKBOX_NAME));
            consentCheckboxes.forEach(WebElement::click);
        }
    }
}
