package ru.mospolytech.saucedemo.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mospolytech.saucedemo.config.Config;
import ru.mospolytech.saucedemo.data.User;
import ru.mospolytech.saucedemo.pages.LoginPage;

import static ru.mospolytech.saucedemo.data.TestData.ERROR_LOCKED_OUT;
import static ru.mospolytech.saucedemo.data.TestData.ERROR_NOT_LOGGED_IN;

@Feature("Разграничение доступа")
@Story("US-02. Ограничение доступа")
@DisplayName("Разграничение доступа")
class AccessControlTests extends BaseTest {

    @Test
    @DisplayName("TC-05. Вход заблокированного пользователя")
    @Description("Заблокированная учётная запись не получает доступ к каталогу")
    @Severity(SeverityLevel.CRITICAL)
    void lockedOutUserCannotLogin() {
        LoginPage loginPage = openLoginPage().loginExpectingError(User.LOCKED_OUT.username(), User.PASSWORD);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(loginPage.getErrorMessage()).as("Сообщение об ошибке").isEqualTo(ERROR_LOCKED_OUT);
        softly.assertThat(loginPage.currentUrl()).as("Каталог не открыт").doesNotContain("inventory");
        softly.assertAll();
    }

    @Test
    @DisplayName("TC-06. Доступ к каталогу без авторизации")
    @Description("Прямой переход на /inventory.html без входа возвращает на страницу входа")
    @Severity(SeverityLevel.CRITICAL)
    void inventoryIsNotAccessibleWithoutLogin() {
        getDriver().get(Config.BASE_URL + "inventory.html");
        LoginPage loginPage = new LoginPage(getDriver());

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(loginPage.isOpened()).as("Открыта страница входа").isTrue();
        softly.assertThat(loginPage.getErrorMessage()).as("Сообщение об ошибке").isEqualTo(ERROR_NOT_LOGGED_IN);
        softly.assertAll();
    }
}
