package ru.mospolytech.saucedemo.model;

import java.math.BigDecimal;

/**
 * Данные товара, считанные со страницы каталога.
 *
 * @param name           название
 * @param description    описание
 * @param priceText      цена в том виде, как она отображается (например, "$29.99")
 * @param imageSrc       адрес изображения
 * @param imageDisplayed отображается ли изображение
 * @param buttonText     текст кнопки ("Add to cart" или "Remove")
 */
public record Product(String name, String description, String priceText,
                      String imageSrc, boolean imageDisplayed, String buttonText) {

    /** Цена в виде числа. */
    public BigDecimal price() {
        return new BigDecimal(priceText.replace("$", "").trim());
    }
}
