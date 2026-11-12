package ru.mospolytech.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Шаг 3 оформления заказа: «Checkout: Complete!».
 */
public class CheckoutCompletePage extends AuthorizedPage {

    private static final By HEADER = By.className("complete-header");

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
    }

    public CheckoutCompletePage waitUntilLoaded() {
        visible(HEADER);
        return this;
    }

    public String getHeader() {
        return text(HEADER);
    }
}
