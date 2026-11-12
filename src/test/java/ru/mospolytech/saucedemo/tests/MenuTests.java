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
import ru.mospolytech.saucedemo.pages.InventoryPage;
import ru.mospolytech.saucedemo.pages.LoginPage;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.mospolytech.saucedemo.data.TestData.BACKPACK;
import static ru.mospolytech.saucedemo.data.TestData.ERROR_NOT_LOGGED_IN;

@Feature("Боковое меню")
@Story("US-07. Выход и сброс состояния")
@DisplayName("Боковое меню")
class MenuTests extends BaseTest {

    @Test
    @DisplayName("TC-15. Выход из системы")
    @Description("После «Logout» открывается страница входа, каталог без повторного входа недоступен")
    @Severity(SeverityLevel.NORMAL)
    void logout() {
        LoginPage loginPage = loginAs(User.STANDARD).logout();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(loginPage.isOpened()).as("Открыта страница входа").isTrue();

        getDriver().get(Config.BASE_URL + "inventory.html");
        softly.assertThat(new LoginPage(getDriver()).getErrorMessage())
                .as("Каталог недоступен после выхода")
                .isEqualTo(ERROR_NOT_LOGGED_IN);
        softly.assertAll();
    }

    @Test
    @DisplayName("TC-16. Сброс состояния приложения")
    @Description("После «Reset App State» счётчик корзины исчезает, а кнопка товара снова «Add to cart»")
    @Severity(SeverityLevel.MINOR)
    void resetAppState() {
        InventoryPage inventory = loginAs(User.STANDARD).addToCart(BACKPACK);
        assertThat(inventory.getCartBadgeCount()).as("Предусловие: товар в корзине").isEqualTo(1);

        inventory.resetAppState();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(inventory.getCartBadgeCount()).as("Счётчик корзины после сброса").isZero();
        softly.assertThat(inventory.getButtonText(BACKPACK)).as("Кнопка у рюкзака после сброса").isEqualTo("Add to cart");
        softly.assertAll();
    }
}
