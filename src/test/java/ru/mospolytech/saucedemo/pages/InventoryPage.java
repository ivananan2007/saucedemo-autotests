package ru.mospolytech.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import ru.mospolytech.saucedemo.data.SortOption;
import ru.mospolytech.saucedemo.model.Product;

import java.math.BigDecimal;
import java.util.List;

/**
 * Страница каталога товаров («Products»).
 */
public class InventoryPage extends AuthorizedPage {

    private static final By INVENTORY_LIST = By.className("inventory_list");
    private static final By ITEM = By.className("inventory_item");
    private static final By ITEM_NAME = By.className("inventory_item_name");
    private static final By ITEM_DESCRIPTION = By.className("inventory_item_desc");
    private static final By ITEM_PRICE = By.className("inventory_item_price");
    private static final By ITEM_IMAGE = By.cssSelector("img.inventory_item_img");
    private static final By ITEM_BUTTON = By.tagName("button");
    private static final By SORT_SELECT = By.className("product_sort_container");

    public InventoryPage(WebDriver driver) {
        super(driver);
    }

    public InventoryPage waitUntilLoaded() {
        visible(INVENTORY_LIST);
        return this;
    }

    /** Считывает все товары в порядке отображения. */
    public List<Product> getProducts() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ITEM));
        // Список может перерисовываться (например, после сортировки) — повторяем чтение при устаревших элементах
        return wait.until(d -> {
            try {
                return d.findElements(ITEM).stream().map(InventoryPage::toProduct).toList();
            } catch (StaleElementReferenceException e) {
                return null;
            }
        });
    }

    public List<String> getProductNames() {
        return getProducts().stream().map(Product::name).toList();
    }

    public List<BigDecimal> getProductPrices() {
        return getProducts().stream().map(Product::price).toList();
    }

    public Product getProduct(String name) {
        return getProducts().stream()
                .filter(p -> p.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден в каталоге: " + name));
    }

    public InventoryPage sortBy(SortOption option) {
        new Select(visible(SORT_SELECT)).selectByVisibleText(option.label());
        return this;
    }

    public InventoryPage addToCart(String productName) {
        click(findItem(productName).findElement(ITEM_BUTTON));
        return this;
    }

    public String getButtonText(String productName) {
        return findItem(productName).findElement(ITEM_BUTTON).getText().trim();
    }

    public ProductPage openProduct(String productName) {
        click(findItem(productName).findElement(ITEM_NAME));
        return new ProductPage(driver).waitUntilLoaded();
    }

    private WebElement findItem(String productName) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(ITEM));
        return driver.findElements(ITEM).stream()
                .filter(item -> item.findElement(ITEM_NAME).getText().trim().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Товар не найден в каталоге: " + productName));
    }

    private static Product toProduct(WebElement item) {
        WebElement image = item.findElement(ITEM_IMAGE);
        return new Product(
                item.findElement(ITEM_NAME).getText().trim(),
                item.findElement(ITEM_DESCRIPTION).getText().trim(),
                item.findElement(ITEM_PRICE).getText().trim(),
                image.getDomAttribute("src"),
                image.isDisplayed(),
                item.findElement(ITEM_BUTTON).getText().trim());
    }
}
