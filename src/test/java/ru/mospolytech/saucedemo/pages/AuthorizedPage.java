package ru.mospolytech.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;

/**
 * Общие элементы страниц, доступных после входа: заголовок, значок корзины и боковое меню.
 */
public abstract class AuthorizedPage extends BasePage {

    private static final By TITLE = By.className("title");
    private static final By CART_LINK = By.className("shopping_cart_link");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By MENU_BUTTON = By.id("react-burger-menu-btn");
    private static final By OPENED_MENU = By.cssSelector(".bm-menu-wrap[aria-hidden='false']");
    private static final By CLOSE_MENU_BUTTON = By.id("react-burger-cross-btn");
    private static final By LOGOUT_LINK = By.id("logout_sidebar_link");
    private static final By RESET_LINK = By.id("reset_sidebar_link");

    private static final int MENU_OPEN_ATTEMPTS = 3;
    private static final Duration MENU_OPEN_TIMEOUT = Duration.ofSeconds(3);

    protected AuthorizedPage(WebDriver driver) {
        super(driver);
    }

    public String getTitle() {
        return text(TITLE);
    }

    public boolean isCartIconDisplayed() {
        return visible(CART_LINK).isDisplayed();
    }

    /** Число на значке корзины; 0, если значок не отображается. */
    public int getCartBadgeCount() {
        List<WebElement> badge = driver.findElements(CART_BADGE);
        return badge.isEmpty() ? 0 : Integer.parseInt(badge.get(0).getText().trim());
    }

    public CartPage openCart() {
        click(CART_LINK);
        return new CartPage(driver).waitUntilLoaded();
    }

    /** Выход через боковое меню. */
    public LoginPage logout() {
        openMenu();
        click(LOGOUT_LINK);
        return new LoginPage(driver);
    }

    /** Сброс состояния приложения через боковое меню. */
    public void resetAppState() {
        openMenu();
        click(RESET_LINK);
        click(CLOSE_MENU_BUTTON);
    }

    /**
     * Открывает боковое меню и дожидается окончания анимации.
     * Если первое нажатие не сработало (страница ещё не готова к событиям), нажимает повторно.
     */
    private void openMenu() {
        for (int attempt = 1; attempt <= MENU_OPEN_ATTEMPTS; attempt++) {
            click(MENU_BUTTON);
            if (isVisibleWithin(OPENED_MENU, MENU_OPEN_TIMEOUT)) {
                return;
            }
        }
        throw new IllegalStateException("Боковое меню не открылось после " + MENU_OPEN_ATTEMPTS + " попыток");
    }
}