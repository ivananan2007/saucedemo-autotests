package ru.mospolytech.saucedemo.data;

import java.math.BigDecimal;

/**
 * Тестовые данные: товары, данные покупателя, ожидаемые сообщения.
 */
public final class TestData {

    // Товары
    public static final String BACKPACK = "Sauce Labs Backpack";
    public static final String BIKE_LIGHT = "Sauce Labs Bike Light";
    public static final String ONESIE = "Sauce Labs Onesie";
    public static final String FLEECE_JACKET = "Sauce Labs Fleece Jacket";
    public static final String RED_T_SHIRT = "Test.allTheThings() T-Shirt (Red)";
    public static final int PRODUCTS_COUNT = 6;

    public static final BigDecimal BACKPACK_PRICE = new BigDecimal("29.99");
    public static final BigDecimal BIKE_LIGHT_PRICE = new BigDecimal("9.99");

    // Данные покупателя
    public static final String FIRST_NAME = "Ivan";
    public static final String LAST_NAME = "Izotov";
    public static final String POSTAL_CODE = "101000";

    // Неверные учётные данные
    public static final String INVALID_USERNAME = "invalid_user";
    public static final String INVALID_PASSWORD = "wrong_password";

    // Заголовки и сообщения
    public static final String PRODUCTS_TITLE = "Products";
    public static final String ORDER_COMPLETE_HEADER = "Thank you for your order!";
    public static final String ERROR_USERNAME_REQUIRED = "Epic sadface: Username is required";
    public static final String ERROR_PASSWORD_REQUIRED = "Epic sadface: Password is required";
    public static final String ERROR_INVALID_CREDENTIALS =
            "Epic sadface: Username and password do not match any user in this service";
    public static final String ERROR_LOCKED_OUT = "Epic sadface: Sorry, this user has been locked out.";
    public static final String ERROR_NOT_LOGGED_IN =
            "Epic sadface: You can only access '/inventory.html' when you are logged in.";
    public static final String ERROR_FIRST_NAME_REQUIRED = "Error: First Name is required";
    public static final String ERROR_LAST_NAME_REQUIRED = "Error: Last Name is required";
    public static final String ERROR_POSTAL_CODE_REQUIRED = "Error: Postal Code is required";

    private TestData() {
    }
}
