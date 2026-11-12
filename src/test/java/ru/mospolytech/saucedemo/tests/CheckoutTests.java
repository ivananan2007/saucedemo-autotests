package ru.mospolytech.saucedemo.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.mospolytech.saucedemo.data.User;
import ru.mospolytech.saucedemo.pages.CheckoutCompletePage;
import ru.mospolytech.saucedemo.pages.CheckoutInfoPage;
import ru.mospolytech.saucedemo.pages.CheckoutOverviewPage;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static ru.mospolytech.saucedemo.data.TestData.*;

@Feature("Оформление заказа")
@Story("US-06. Оформление заказа")
@DisplayName("Оформление заказа")
class CheckoutTests extends BaseTest {

    @Test
    @DisplayName("TC-13. Сквозное оформление заказа с проверкой суммы")
    @Description("Полный путь покупателя: корзина → данные → проверка суммы → подтверждение заказа")
    @Severity(SeverityLevel.BLOCKER)
    void completeOrderWithTotalCheck() {
        CheckoutOverviewPage overview = loginAs(User.STANDARD)
                .addToCart(BACKPACK)
                .addToCart(BIKE_LIGHT)
                .openCart()
                .checkout()
                .fillCustomerInfo(FIRST_NAME, LAST_NAME, POSTAL_CODE)
                .continueToOverview();

        BigDecimal itemTotal = overview.getItemTotal();
        BigDecimal tax = overview.getTax();
        BigDecimal total = overview.getTotal();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(itemTotal).as("Item total").isEqualByComparingTo("39.98");
        softly.assertThat(tax).as("Tax").isEqualByComparingTo("3.20");
        softly.assertThat(total).as("Total").isEqualByComparingTo("43.18");
        softly.assertThat(total).as("Total = Item total + Tax").isEqualByComparingTo(itemTotal.add(tax));

        CheckoutCompletePage complete = overview.finish();
        softly.assertThat(complete.getHeader()).as("Сообщение об успешном заказе").isEqualTo(ORDER_COMPLETE_HEADER);
        softly.assertThat(complete.getCartBadgeCount()).as("Корзина очищена").isZero();
        softly.assertAll();
    }

    static Stream<Arguments> emptyFieldCases() {
        return Stream.of(
                Arguments.of("Имя", "", LAST_NAME, POSTAL_CODE, ERROR_FIRST_NAME_REQUIRED),
                Arguments.of("Фамилия", FIRST_NAME, "", POSTAL_CODE, ERROR_LAST_NAME_REQUIRED),
                Arguments.of("Почтовый индекс", FIRST_NAME, LAST_NAME, "", ERROR_POSTAL_CODE_REQUIRED));
    }

    @ParameterizedTest(name = "TC-14.{index}. Пустое поле «{0}»")
    @MethodSource("emptyFieldCases")
    @DisplayName("TC-14. Валидация обязательных полей при оформлении")
    @Description("При пустом обязательном поле выводится сообщение об ошибке и переход не выполняется")
    @Severity(SeverityLevel.NORMAL)
    void requiredFieldsValidation(String field, String firstName, String lastName,
                                  String postalCode, String expectedError) {
        CheckoutInfoPage info = loginAs(User.STANDARD)
                .addToCart(BACKPACK)
                .openCart()
                .checkout()
                .fillCustomerInfo(firstName, lastName, postalCode);
        info.clickContinue();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(info.getErrorMessage()).as("Сообщение об ошибке для поля «%s»", field).isEqualTo(expectedError);
        softly.assertThat(info.currentUrl()).as("Остаёмся на первом шаге").contains("checkout-step-one");
        softly.assertAll();
    }

    @Test
    @DisplayName("TC-19. Оформление заказа под problem_user")
    @Description("Введённые данные покупателя должны сохраняться в полях, после «Continue» открывается шаг проверки заказа")
    @Severity(SeverityLevel.CRITICAL)
    void checkoutForProblemUser() {
        CheckoutInfoPage info = loginAs(User.PROBLEM)
                .addToCart(BACKPACK)
                .openCart()
                .checkout()
                .fillCustomerInfo(FIRST_NAME, LAST_NAME, POSTAL_CODE);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(info.getFirstNameValue()).as("Поле First Name").isEqualTo(FIRST_NAME);
        softly.assertThat(info.getLastNameValue()).as("Поле Last Name").isEqualTo(LAST_NAME);
        softly.assertThat(info.getPostalCodeValue()).as("Поле Postal Code").isEqualTo(POSTAL_CODE);

        info.clickContinue();
        softly.assertThat(info.waitForUrlContains("checkout-step-two")).as("Открыт шаг «Checkout: Overview»").isTrue();
        softly.assertAll();
    }
}
