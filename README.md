# Автоматизированное тестирование Sauce Demo

Итоговый проект по дисциплине «Основы тестирования».
Автор: Изотов И.А., группа 251-671, Московский политехнический университет.

Объект тестирования: [Sauce Demo](https://www.saucedemo.com) — демонстрационный интернет-магазин.

## Стек

| Компонент | Инструмент |
| --- | --- |
| Язык | Java 17+ |
| Автоматизация браузера | Selenium WebDriver 4 |
| Тестовый фреймворк | JUnit 5 |
| Проверки | AssertJ (в том числе мягкие проверки SoftAssertions) |
| Сборка | Maven |
| Отчёт | Allure Report |
| Браузер | Google Chrome (драйвер подбирается автоматически через Selenium Manager) |

## Запуск

Требуется: JDK 17 или новее, Maven (встроен в IntelliJ IDEA), Google Chrome.

```bash
# запустить все тесты (откроется браузер)
mvn clean test

# запустить без окна браузера
mvn clean test -Dheadless=true

# открыть отчёт Allure в браузере
mvn allure:serve
```

Упавшие тесты не прерывают сборку (`testFailureIgnore`), чтобы отчёт содержал все результаты, в том числе найденные дефекты.
Скриншоты каждого теста сохраняются в `target/screenshots` и прикрепляются к отчёту Allure.

## Структура проекта

```
src/test/java/ru/mospolytech/saucedemo/
├── config/       настройки (адрес, тайм-ауты) и создание браузера
├── data/         тестовые данные: пользователи, товары, сообщения, варианты сортировки
├── model/        модель товара
├── pages/        Page Object — по классу на каждую страницу приложения
├── extensions/   расширение JUnit для скриншотов
└── tests/        тестовые классы, сгруппированные по функциям
```

Тесты не содержат локаторов: все действия со страницами вынесены в классы `pages`.
Каждый тест независим — открывает новый браузер и закрывает его после выполнения.
Вместо фиксированных пауз используются явные ожидания (`WebDriverWait`).

## Соответствие тест-кейсов и автотестов

| Тест-кейс | Класс | Метод |
| --- | --- | --- |
| TC-01. Успешный вход | LoginTests | successfulLogin |
| TC-02. Вход с пустым логином | LoginTests | loginWithEmptyUsername |
| TC-03. Вход с пустым паролем | LoginTests | loginWithEmptyPassword |
| TC-04. Неверные учётные данные | LoginTests | loginWithInvalidCredentials |
| TC-05. Заблокированный пользователь | AccessControlTests | lockedOutUserCannotLogin |
| TC-06. Каталог без авторизации | AccessControlTests | inventoryIsNotAccessibleWithoutLogin |
| TC-07. Отображение каталога | CatalogTests | catalogDisplaysAllProducts |
| TC-08. Сортировка по цене | CatalogTests | sortByPriceLowToHigh |
| TC-09. Сортировка Z–A | CatalogTests | sortByNameZtoA |
| TC-10. Карточка товара | CatalogTests | productPageMatchesCatalog |
| TC-11. Добавление в корзину | CartTests | addProductsToCart |
| TC-12. Удаление из корзины | CartTests | removeProductFromCart |
| TC-13. Оформление заказа | CheckoutTests | completeOrderWithTotalCheck |
| TC-14. Валидация полей заказа | CheckoutTests | requiredFieldsValidation (3 варианта) |
| TC-15. Выход из системы | MenuTests | logout |
| TC-16. Сброс состояния | MenuTests | resetAppState |
| TC-17. Сортировка под problem_user | CatalogTests | sortByPriceForProblemUser |
| TC-18. Изображения под problem_user | CatalogTests | productImagesAreUniqueForProblemUser |
| TC-19. Оформление под problem_user | CheckoutTests | checkoutForProblemUser |
| TC-20. Время входа performance_glitch_user | LoginTests | loginTimeForPerformanceGlitchUser |
