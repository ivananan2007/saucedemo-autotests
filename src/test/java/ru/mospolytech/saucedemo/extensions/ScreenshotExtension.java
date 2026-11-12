package ru.mospolytech.saucedemo.extensions;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import ru.mospolytech.saucedemo.tests.BaseTest;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * После каждого теста делает снимок экрана, прикрепляет его к отчёту Allure
 * и сохраняет в папку target/screenshots. Срабатывает до закрытия браузера.
 */
public class ScreenshotExtension implements AfterTestExecutionCallback {

    private static final Path SCREENSHOTS_DIR = Path.of("target", "screenshots");

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (!(context.getRequiredTestInstance() instanceof BaseTest test)) {
            return;
        }
        WebDriver driver = test.getDriver();
        if (driver == null) {
            return;
        }
        boolean failed = context.getExecutionException().isPresent();
        try {
            byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(failed ? "Скриншот в момент падения" : "Скриншот в конце теста",
                    "image/png", new ByteArrayInputStream(png), ".png");

            Files.createDirectories(SCREENSHOTS_DIR);
            String fileName = (failed ? "FAILED_" : "PASSED_")
                    + context.getDisplayName().replaceAll("[^\\p{L}\\p{N}_.-]+", "_") + ".png";
            Files.write(SCREENSHOTS_DIR.resolve(fileName), png);
        } catch (Exception e) {
            System.err.println("Не удалось сделать скриншот: " + e.getMessage());
        }
    }
}
