package ru.mospolytech.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Шаг 1 оформления заказа: «Checkout: Your Information».
 */
public class CheckoutInfoPage extends AuthorizedPage {

    private static final By FIRST_NAME = By.id("first-name");
    private static final By LAST_NAME = By.id("last-name");
    private static final By POSTAL_CODE = By.id("postal-code");
    private static final By CONTINUE_BUTTON = By.id("continue");
    private static final By ERROR = By.cssSelector("[data-test='error']");

    public CheckoutInfoPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutInfoPage waitUntilLoaded() {
        visible(FIRST_NAME);
        return this;
    }

    public CheckoutInfoPage fillCustomerInfo(String firstName, String lastName, String postalCode) {
        type(FIRST_NAME, firstName);
        type(LAST_NAME, lastName);
        type(POSTAL_CODE, postalCode);
        return this;
    }

    public String getFirstNameValue() {
        return value(FIRST_NAME);
    }

    public String getLastNameValue() {
        return value(LAST_NAME);
    }

    public String getPostalCodeValue() {
        return value(POSTAL_CODE);
    }

    /** Нажимает «Continue» без ожидания перехода (для негативных проверок). */
    public void clickContinue() {
        click(CONTINUE_BUTTON);
    }

    /** Нажимает «Continue» и переходит к шагу проверки заказа. */
    public CheckoutOverviewPage continueToOverview() {
        click(CONTINUE_BUTTON);
        return new CheckoutOverviewPage(driver).waitUntilLoaded();
    }

    public String getErrorMessage() {
        return text(ERROR);
    }
}
