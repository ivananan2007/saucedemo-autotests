package ru.mospolytech.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.math.BigDecimal;
import java.util.List;

/**
 * Страница корзины.
 */
public class CartPage extends AuthorizedPage {

    private static final By CART_LIST = By.className("cart_list");
    private static final By CART_ITEM = By.className("cart_item");
    private static final By ITEM_NAME = By.className("inventory_item_name");
    private static final By ITEM_PRICE = By.className("inventory_item_price");
    private static final By ITEM_BUTTON = By.tagName("button");
    private static final By CHECKOUT_BUTTON = By.id("checkout");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage waitUntilLoaded() {
        visible(CART_LIST);
        return this;
    }

    public List<String> getItemNames() {
        return driver.findElements(CART_ITEM).stream()
                .map(item -> item.findElement(ITEM_NAME).getText().trim())
                .toList();
    }

    public BigDecimal getItemPrice(String productName) {
        String price = findItem(productName).findElement(ITEM_PRICE).getText();
        return new BigDecimal(price.replace("$", "").trim());
    }

    public CartPage removeItem(String productName) {
        click(findItem(productName).findElement(ITEM_BUTTON));
        return this;
    }

    public CheckoutInfoPage checkout() {
        click(CHECKOUT_BUTTON);
        return new CheckoutInfoPage(driver).waitUntilLoaded();
    }

    private WebElement findItem(String productName) {
        return driver.findElements(CART_ITEM).stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден в корзине: " + productName));
    }
}
