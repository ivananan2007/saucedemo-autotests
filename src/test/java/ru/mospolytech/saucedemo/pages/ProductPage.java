package ru.mospolytech.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;

/**
 * Страница подробной информации о товаре.
 */
public class ProductPage extends AuthorizedPage {

    private static final By NAME = By.className("inventory_details_name");
    private static final By DESCRIPTION = By.className("inventory_details_desc");
    private static final By PRICE = By.className("inventory_details_price");
    private static final By BACK_BUTTON = By.id("back-to-products");

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public ProductPage waitUntilLoaded() {
        visible(NAME);
        return this;
    }

    public String getName() {
        return text(NAME);
    }

    public String getDescription() {
        return text(DESCRIPTION);
    }

    public BigDecimal getPrice() {
        return new BigDecimal(text(PRICE).replace("$", "").trim());
    }

    public InventoryPage backToProducts() {
        click(BACK_BUTTON);
        return new InventoryPage(driver).waitUntilLoaded();
    }
}
