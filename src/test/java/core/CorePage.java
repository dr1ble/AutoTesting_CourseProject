package core;

import lombok.Setter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

abstract public class CorePage {
    @Setter
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(CorePage.class);

    public CorePage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        logger.info("Элемент '{}' был кликнут", element);
    }

    protected void goToElement(WebElement element) {
        logger.info("Переходи к элементу: {}", element);
        Actions action = new Actions(driver);
        action.moveToElement(element).build().perform();
    }

    public void switchWindow() {
        String originalWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
    }

}
