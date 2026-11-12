package ru.mospolytech.saucedemo.config;

import java.time.Duration;

/**
 * Настройки запуска тестов. Любой параметр можно переопределить из командной строки,
 * например: mvn test -Dheadless=true
 */
public final class Config {

    /** Адрес тестируемого приложения. */
    public static final String BASE_URL = System.getProperty("baseUrl", "https://www.saucedemo.com/");

    /** Запуск браузера без окна (удобно для CI). */
    public static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "false"));

    /** Максимальное время явного ожидания элемента. */
    public static final Duration TIMEOUT = Duration.ofSeconds(Long.parseLong(System.getProperty("timeout", "10")));

    private Config() {
    }
}
