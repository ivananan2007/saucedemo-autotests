package ru.mospolytech.saucedemo.tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.mospolytech.saucedemo.data.SortOption;
import ru.mospolytech.saucedemo.data.User;
import ru.mospolytech.saucedemo.model.Product;
import ru.mospolytech.saucedemo.pages.InventoryPage;
import ru.mospolytech.saucedemo.pages.ProductPage;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.mospolytech.saucedemo.data.TestData.*;

@Feature("Каталог товаров")
@DisplayName("Каталог товаров")
class CatalogTests extends BaseTest {

    private static final String PRICE_FORMAT = "\\$\\d+\\.\\d{2}";

    @Test
    @Story("US-03. Просмотр и сортировка каталога")
    @DisplayName("TC-07. Отображение каталога товаров")
    @Description("В каталоге 6 товаров, у каждого есть название, описание, цена, изображение и кнопка «Add to cart»")
    @Severity(SeverityLevel.NORMAL)
    void catalogDisplaysAllProducts() {
        List<Product> products = loginAs(User.STANDARD).getProducts();

        assertThat(products).as("Количество товаров").hasSize(PRODUCTS_COUNT);
        SoftAssertions softly = new SoftAssertions();
        for (Product product : products) {
            String name = product.name();
            softly.assertThat(name).as("Название товара").isNotBlank();
            softly.assertThat(product.description()).as("Описание: " + name).isNotBlank();
            softly.assertThat(product.priceText()).as("Формат цены: " + name).matches(PRICE_FORMAT);
            softly.assertThat(product.imageDisplayed()).as("Изображение отображается: " + name).isTrue();
            softly.assertThat(product.buttonText()).as("Кнопка: " + name).isEqualTo("Add to cart");
        }
        softly.assertAll();
    }

    @Test
    @Story("US-03. Просмотр и сортировка каталога")
    @DisplayName("TC-08. Сортировка по цене по возрастанию")
    @Description("После выбора «Price (low to high)» цены расположены по неубыванию")
    @Severity(SeverityLevel.NORMAL)
    void sortByPriceLowToHigh() {
        verifySortByPriceLowToHigh(User.STANDARD);
    }

    @Test
    @Story("US-03. Просмотр и сортировка каталога")
    @DisplayName("TC-09. Сортировка по названию Z–A")
    @Description("После выбора «Name (Z to A)» названия расположены в обратном алфавитном порядке")
    @Severity(SeverityLevel.MINOR)
    void sortByNameZtoA() {
        List<String> names = loginAs(User.STANDARD)
                .sortBy(SortOption.NAME_Z_TO_A)
                .getProductNames();

        List<String> expected = names.stream().sorted(Comparator.reverseOrder()).toList();
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(names).as("Порядок названий").containsExactlyElementsOf(expected);
        softly.assertThat(names.get(0)).as("Первый товар").isEqualTo(RED_T_SHIRT);
        softly.assertAll();
    }

    @Test
    @Story("US-04. Карточка товара")
    @DisplayName("TC-10. Соответствие карточки товара каталогу")
    @Description("Название, описание и цена на странице товара совпадают с каталогом; кнопка «Back to products» возвращает в каталог")
    @Severity(SeverityLevel.NORMAL)
    void productPageMatchesCatalog() {
        InventoryPage inventory = loginAs(User.STANDARD);
        Product inCatalog = inventory.getProduct(BACKPACK);

        ProductPage productPage = inventory.openProduct(BACKPACK);
        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(productPage.getName()).as("Название").isEqualTo(inCatalog.name());
        softly.assertThat(productPage.getDescription()).as("Описание").isEqualTo(inCatalog.description());
        softly.assertThat(productPage.getPrice()).as("Цена").isEqualByComparingTo(inCatalog.price());
        softly.assertThat(productPage.getPrice()).as("Цена рюкзака").isEqualByComparingTo(BACKPACK_PRICE);

        InventoryPage backToCatalog = productPage.backToProducts();
        softly.assertThat(backToCatalog.getTitle()).as("Возврат в каталог").isEqualTo(PRODUCTS_TITLE);
        softly.assertAll();
    }

    @Test
    @Story("US-03. Просмотр и сортировка каталога")
    @DisplayName("TC-17. Сортировка по цене под problem_user")
    @Description("Сортировка «Price (low to high)» должна работать и для учётной записи problem_user")
    @Severity(SeverityLevel.NORMAL)
    void sortByPriceForProblemUser() {
        verifySortByPriceLowToHigh(User.PROBLEM);
    }

    @Test
    @Story("US-03. Просмотр и сортировка каталога")
    @DisplayName("TC-18. Уникальность изображений товаров под problem_user")
    @Description("У каждого из 6 товаров должно быть своё изображение")
    @Severity(SeverityLevel.MINOR)
    void productImagesAreUniqueForProblemUser() {
        List<String> imageSources = loginAs(User.PROBLEM).getProducts().stream()
                .map(Product::imageSrc)
                .toList();

        assertThat(imageSources)
                .as("Адреса изображений товаров")
                .hasSize(PRODUCTS_COUNT)
                .doesNotHaveDuplicates();
    }

    private void verifySortByPriceLowToHigh(User user) {
        List<Product> products = loginAs(user)
                .sortBy(SortOption.PRICE_LOW_TO_HIGH)
                .getProducts();
        List<BigDecimal> prices = products.stream().map(Product::price).toList();

        SoftAssertions softly = new SoftAssertions();
        softly.assertThat(prices).as("Цены по возрастанию").isSorted();
        softly.assertThat(products.get(0).name()).as("Первый товар").isEqualTo(ONESIE);
        softly.assertThat(products.get(products.size() - 1).name()).as("Последний товар").isEqualTo(FLEECE_JACKET);
        softly.assertAll();
    }
}
