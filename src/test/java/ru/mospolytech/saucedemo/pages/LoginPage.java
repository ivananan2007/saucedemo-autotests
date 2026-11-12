package ru.mospolytech.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.mospolytech.saucedemo.config.Config;

/**
 * Страница входа.
 */
public class LoginPage extends BasePage {

    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(Config.BASE_URL);
        visible(LOGIN_BUTTON);
        return this;
    }

    public LoginPage enterUsername(String username) {
        type(USERNAME, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        type(PASSWORD, password);
        return this;
    }

    public void clickLogin() {
        click(LOGIN_BUTTON);
    }

    /** Вход с данными, при которых ожидается успех. */
    public InventoryPage loginAs(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return new InventoryPage(driver);
    }

    /** Попытка входа, после которой ожидается сообщение об ошибке. */
    public LoginPage loginExpectingError(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
        return this;
    }

    public String getErrorMessage() {
        return text(ERROR);
    }

    /** Открыта ли форма входа. */
    public boolean isOpened() {
        return isVisibleWithin(LOGIN_BUTTON);
    }
}
