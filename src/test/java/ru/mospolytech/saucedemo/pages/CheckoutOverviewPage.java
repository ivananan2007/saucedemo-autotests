package ru.mospolytech.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;

/**
 * Шаг 2 оформления заказа: «Checkout: Overview».
 */
public class CheckoutOverviewPage extends AuthorizedPage {

    private static final By ITEM_TOTAL = By.className("summary_subtotal_label");
    private static final By TAX = By.className("summary_tax_label");
    private static final By TOTAL = By.className("summary_total_label");
    private static final By FINISH_BUTTON = By.id("finish");

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutOverviewPage waitUntilLoaded() {
        visible(TOTAL);
        return this;
    }

    public BigDecimal getItemTotal() {
        return amount(ITEM_TOTAL);
    }

    public BigDecimal getTax() {
        return amount(TAX);
    }

    public BigDecimal getTotal() {
        return amount(TOTAL);
    }

    public CheckoutCompletePage finish() {
        click(FINISH_BUTTON);
        return new CheckoutCompletePage(driver).waitUntilLoaded();
    }

    /** Извлекает сумму из строки вида "Item total: $39.98". */
    private BigDecimal amount(By locator) {
        String label = text(locator);
        return new BigDecimal(label.substring(label.indexOf('$') + 1).trim());
    }
}
