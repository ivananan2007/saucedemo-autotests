package ru.mospolytech.saucedemo.data;

/**
 * Встроенные учётные записи Sauce Demo.
 */
public enum User {
    STANDARD("standard_user"),
    LOCKED_OUT("locked_out_user"),
    PROBLEM("problem_user"),
    PERFORMANCE_GLITCH("performance_glitch_user");

    /** Общий пароль всех учётных записей. */
    public static final String PASSWORD = "secret_sauce";

    private final String username;

    User(String username) {
        this.username = username;
    }

    public String username() {
        return username;
    }
}
