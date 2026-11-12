package ru.mospolytech.saucedemo.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.mospolytech.saucedemo.config.Config;

import java.time.Duration;

/**
 * Базовый класс всех страниц (паттерн Page Object).
 * Содержит общие действия с явными ожиданиями, чтобы тесты не зависели от скорости загрузки.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Config.TIMEOUT);
    }

    /** Ждёт, пока элемент станет видимым, и возвращает его. */
    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Нажимает на элемент. Если элемент временно перекрыт (например, во время анимации меню),
     * повторяет попытку до истечения тайм-аута.
     */
    protected void click(By locator) {
        wait.until(d -> {
            try {
                d.findElement(locator).click();
                return true;
            } catch (ElementNotInteractableException | StaleElementReferenceException e) {
                return false;
            }
        });
    }

    /** Нажимает на найденный элемент, дождавшись его кликабельности. */
    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }

    /** Очищает поле и вводит текст. */
    protected void type(By locator, String text) {
        WebElement field = visible(locator);
        field.clear();
        field.sendKeys(text);
    }

    /** Возвращает текст видимого элемента. */
    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    /** Возвращает текущее значение поля ввода. */
    protected String value(By locator) {
        return visible(locator).getDomProperty("value");
    }

    /** Проверяет наличие элемента на странице без ожидания. */
    protected boolean isPresent(By locator) {
        return !driver.findElements(locator).isEmpty();
    }

    /** Ждёт появления элемента; возвращает false, если он так и не появился. */
    protected boolean isVisibleWithin(By locator) {
        return isVisibleWithin(locator, Config.TIMEOUT);
    }

    /** Ждёт появления элемента не дольше указанного времени; возвращает false по тайм-ауту. */
    protected boolean isVisibleWithin(By locator, Duration timeout) {
        try {
            new WebDriverWait(driver, timeout).until(ExpectedConditions.visibilityOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /** Ждёт, пока адрес страницы будет содержать фрагмент; возвращает false по тайм-ауту. */
    public boolean waitForUrlContains(String fragment) {
        try {
            return wait.until(ExpectedConditions.urlContains(fragment));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}