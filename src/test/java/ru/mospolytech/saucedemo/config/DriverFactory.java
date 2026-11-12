package ru.mospolytech.saucedemo.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Создаёт и настраивает экземпляр браузера Chrome.
 * Драйвер подбирается автоматически встроенным механизмом Selenium Manager.
 */
public final class DriverFactory {

    // Скрываем служебные предупреждения Selenium (например, о версии CDP), чтобы не засорять вывод тестов.
    // Ссылка хранится в поле, иначе настройка логгера может быть потеряна при сборке мусора.
    private static final Logger SELENIUM_LOGGER = Logger.getLogger("org.openqa.selenium");

    static {
        SELENIUM_LOGGER.setLevel(Level.SEVERE);
    }

    private DriverFactory() {
    }

    public static WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();

        // Отключаем менеджер паролей Chrome: его всплывающее окно
        // «Смените пароль» перекрывает страницу и ломает тесты
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments(
                "--incognito",
                "--disable-features=PasswordLeakDetection",
                "--disable-notifications",
                "--window-size=1920,1080");

        if (Config.HEADLESS) {
            options.addArguments("--headless=new");
        }

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        if (!Config.HEADLESS) {
            driver.manage().window().maximize();
        }
        return driver;
    }
}