package ru.mospolytech.saucedemo.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mospolytech.saucedemo.data.User;
import ru.mospolytech.saucedemo.pages.CartPage;
import ru.mospolytech.saucedemo.pages.InventoryPage;

import static ru.mospolytech.saucedemo.data.TestData.*;

@Feature("Корзина")
@Story("US-05. Управление корзиной")
@DisplayName("Корзина")
class CartTests extends BaseTest {

    @Test
    @DisplayName("TC-11. Добавление товаров в корзину")
    @Description("После добавления двух товаров счётчик равен 2, кнопки меняются на «Remove», товары есть в корзине")
    @Severity(SeverityLevel.CRITICAL)
    void addProductsToCart() {
        InventoryPage inventory = loginAs(User.STANDARD)
                .addToCart(BACKPACK)
                .addToCart(BIKE_LIGHT);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(inventory.getCartBadgeCount()).as("Счётчик корзины").isEqualTo(2);
        softly.assertThat(inventory.getButtonText(BACKPACK)).as("Кнопка у рюкзака").isEqualTo("Remove");
        softly.assertThat(inventory.getButtonText(BIKE_LIGHT)).as("Кнопка у фонаря").isEqualTo("Remove");

        CartPage cart = inventory.openCart();
        softly.assertThat(cart.getItemNames()).as("Товары в корзине").containsExactlyInAnyOrder(BACKPACK, BIKE_LIGHT);
        softly.assertThat(cart.getItemPrice(BACKPACK)).as("Цена рюкзака").isEqualByComparingTo(BACKPACK_PRICE);
        softly.assertThat(cart.getItemPrice(BIKE_LIGHT)).as("Цена фонаря").isEqualByComparingTo(BIKE_LIGHT_PRICE);
        softly.assertAll();
    }

    @Test
    @DisplayName("TC-12. Удаление товара из корзины")
    @Description("После удаления товара в корзине остаётся только второй товар, счётчик уменьшается до 1")
    @Severity(SeverityLevel.CRITICAL)
    void removeProductFromCart() {
        CartPage cart = loginAs(User.STANDARD)
                .addToCart(BACKPACK)
                .addToCart(BIKE_LIGHT)
                .openCart()
                .removeItem(BACKPACK);

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(cart.getItemNames()).as("Товары в корзине").containsExactly(BIKE_LIGHT);
        softly.assertThat(cart.getCartBadgeCount()).as("Счётчик корзины").isEqualTo(1);
        softly.assertAll();
    }
}
