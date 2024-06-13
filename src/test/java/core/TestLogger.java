package core;

import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.logging.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task1.tests.LambdaPageTests;

import static core.CoreTest.driver;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestLogger implements TestWatcher {
    private static final Logger logger = LoggerFactory.getLogger(LambdaPageTests.class);
    private static final String SCREENSHOT_DIR = "failedTestsScreenshots/";

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        logger.error("Тест провален: " + context.getDisplayName(), cause);
        captureScreenshot("Проваленный тест", context, true);
        quitDriver();
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        logger.info("Тест успешен: " + context.getDisplayName());
        quitDriver();
    }

    private void captureScreenshot(String status, ExtensionContext context, boolean saveToFile) {
        try {
            // Скриншот
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment(status + " screenshot", "image/png", "png", screenshot);

            if (saveToFile) {
                // Сохранение скриншота в файл
                File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuu-MMM-dd-HH-mm-ss");
                String pathName = SCREENSHOT_DIR + status + "-(" + context.getDisplayName().replaceAll(" ", "-").replaceAll("[\\/\\?]", "_") + ")-" + LocalDateTime.now().format(format) + ".png";
                FileUtils.copyFile(srcFile, new File(pathName));
            }
        } catch (IOException e) {
            logger.error("Ошибка при создании скриншота", e);
            throw new RuntimeException(e);
        }
    }

    private void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
