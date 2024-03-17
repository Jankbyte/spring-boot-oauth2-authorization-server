package ru.jankbyte.spring.oauth2.authorizationserver.automation.page;

import org.openqa.selenium.WebDriver;

public abstract class AbstractPage {
    protected final WebDriver webDriver;

    public AbstractPage(WebDriver webDriver) {
        this.webDriver = webDriver;
    }
}
