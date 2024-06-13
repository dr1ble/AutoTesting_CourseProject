package core;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

abstract public class CoreTest {

    public static WebDriver driver;
    protected static final Logger logger = LoggerFactory.getLogger(CoreTest.class);

    @BeforeEach
    public void setUp(){
        try {
            logger.info("Установка chromedriver");
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
            driver.manage().window().maximize();
            logger.info("Установка chromedriver успешна");
            driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
            driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
            CorePage.setDriver(driver);

        } catch (Exception e) {
            logger.error("Ошибка при установке chromedriver", e);
            throw new RuntimeException(e);
        }
    }

}
