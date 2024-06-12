package core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

abstract public class CorePage {
    protected static WebDriver driver;
    protected static WebDriverWait wait;
    private static final Logger logger = LoggerFactory.getLogger(CorePage.class);

    public CorePage() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public static void setDriver(WebDriver webDriver){
        driver = webDriver;
    }

    protected void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
        logger.info("Элемент '{}' был кликнут", element);
    }

}
