package ru.mospolytech.saucedemo.tests;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mospolytech.saucedemo.data.User;
import ru.mospolytech.saucedemo.pages.InventoryPage;
import ru.mospolytech.saucedemo.pages.LoginPage;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.mospolytech.saucedemo.data.TestData.*;

@Feature("Авторизация")
@Story("US-01. Вход в систему")
@DisplayName("Авторизация")
class LoginTests extends BaseTest {

    private static final long MAX_LOGIN_TIME_MS = 3000;

    @Test
    @DisplayName("TC-01. Успешный вход с корректными данными")
    @Description("Вход под standard_user открывает каталог с заголовком «Products» и значком корзины")
    @Severity(SeverityLevel.BLOCKER)
    void successfulLogin() {
        InventoryPage inventory = loginAs(User.STANDARD);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(inventory.currentUrl()).as("Адрес страницы").endsWith("/inventory.html");
        softly.assertThat(inventory.getTitle()).as("Заголовок страницы").isEqualTo(PRODUCTS_TITLE);
        softly.assertThat(inventory.isCartIconDisplayed()).as("Значок корзины отображается").isTrue();
        softly.assertAll();
    }

    @Test
    @DisplayName("TC-02. Вход с пустым логином")
    @Description("При пустом логине выводится сообщение об обязательности поля")
    @Severity(SeverityLevel.NORMAL)
    void loginWithEmptyUsername() {
        LoginPage loginPage = openLoginPage().loginExpectingError("", User.PASSWORD);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(loginPage.getErrorMessage()).as("Сообщение об ошибке").isEqualTo(ERROR_USERNAME_REQUIRED);
        softly.assertThat(loginPage.isOpened()).as("Остаёмся на странице входа").isTrue();
        softly.assertAll();
    }

    @Test
    @DisplayName("TC-03. Вход с пустым паролем")
    @Description("При пустом пароле выводится сообщение об обязательности поля")
    @Severity(SeverityLevel.NORMAL)
    void loginWithEmptyPassword() {
        LoginPage loginPage = openLoginPage().loginExpectingError(User.STANDARD.username(), "");

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(loginPage.getErrorMessage()).as("Сообщение об ошибке").isEqualTo(ERROR_PASSWORD_REQUIRED);
        softly.assertThat(loginPage.isOpened()).as("Остаёмся на странице входа").isTrue();
        softly.assertAll();
    }

    @Test
    @DisplayName("TC-04. Вход с неверными учётными данными")
    @Description("При несуществующей паре логин/пароль вход не выполняется")
    @Severity(SeverityLevel.CRITICAL)
    void loginWithInvalidCredentials() {
        LoginPage loginPage = openLoginPage().loginExpectingError(INVALID_USERNAME, INVALID_PASSWORD);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(loginPage.getErrorMessage()).as("Сообщение об ошибке").isEqualTo(ERROR_INVALID_CREDENTIALS);
        softly.assertThat(loginPage.currentUrl()).as("Каталог не открыт").doesNotContain("inventory");
        softly.assertAll();
    }

    @Test
    @DisplayName("TC-20. Время входа под performance_glitch_user")
    @Description("Каталог должен открываться не позднее чем через 3 секунды после нажатия «Login»")
    @Severity(SeverityLevel.MINOR)
    void loginTimeForPerformanceGlitchUser() {
        LoginPage loginPage = openLoginPage()
                .enterUsername(User.PERFORMANCE_GLITCH.username())
                .enterPassword(User.PASSWORD);

        long start = System.nanoTime();
        loginPage.clickLogin();
        InventoryPage inventory = new InventoryPage(getDriver()).waitUntilLoaded();
        long elapsedMs = Duration.ofNanos(System.nanoTime() - start).toMillis();

        Allure.addAttachment("Время входа, мс", String.valueOf(elapsedMs));
        assertThat(inventory.getTitle()).as("Заголовок страницы").isEqualTo(PRODUCTS_TITLE);
        assertThat(elapsedMs).as("Время входа, мс").isLessThanOrEqualTo(MAX_LOGIN_TIME_MS);
    }
}
