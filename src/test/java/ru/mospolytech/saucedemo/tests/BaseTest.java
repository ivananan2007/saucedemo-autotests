package ru.mospolytech.saucedemo.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import ru.mospolytech.saucedemo.config.DriverFactory;
import ru.mospolytech.saucedemo.data.User;
import ru.mospolytech.saucedemo.extensions.ScreenshotExtension;
import ru.mospolytech.saucedemo.pages.InventoryPage;
import ru.mospolytech.saucedemo.pages.LoginPage;

/**
 * Базовый класс тестов: каждый тест получает новый браузер и закрывает его после выполнения,
 * поэтому тесты не зависят друг от друга.
 */
@ExtendWith(ScreenshotExtension.class)
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = DriverFactory.createDriver();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }

    protected LoginPage openLoginPage() {
        return new LoginPage(driver).open();
    }

    /** Предусловие большинства тестов: вход под указанным пользователем. */
    protected InventoryPage loginAs(User user) {
        return openLoginPage()
                .loginAs(user.username(), User.PASSWORD)
                .waitUntilLoaded();
    }
}
