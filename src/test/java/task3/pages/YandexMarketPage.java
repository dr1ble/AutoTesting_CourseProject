package task3.pages;

import core.CorePage;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class YandexMarketPage extends CorePage {
    private static final Logger logger = LoggerFactory.getLogger(YandexMarketPage.class);

    // Locators for page elements
    @FindBy(xpath = "//button[.//span[text()='Каталог']]")
    private WebElement catalogButton;

    @FindBy(xpath = "//a[.//span[text()='Все для гейминга']]")
    private WebElement allForGamingCategory;

    @FindBy(xpath = "//li//a[text()='Игровые телефоны']")
    private WebElement gamingPhonesCategory;

    @FindBy(xpath = "//div[@data-apiary-widget-name='@light/Organic']")
    private List<WebElement> gamingPhones;

    @FindBy(xpath = "//label[@role='checkbox']//span[text()='Samsung']")
    private WebElement samsungCheckbox;

    public YandexMarketPage() {
        driver.get("https://market.yandex.ru/");
        PageFactory.initElements(driver, this);
    }

    public void openCatalog() {
        wait.until(ExpectedConditions.elementToBeClickable(catalogButton)).click();
        logger.info("Opened catalog menu.");
    }

    public void selectGamingPhonesCategory() {
        wait.until(ExpectedConditions.visibilityOf(allForGamingCategory));
        goToElement(allForGamingCategory);
        goToElement(gamingPhonesCategory);
        wait.until(ExpectedConditions.visibilityOf(gamingPhonesCategory));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", gamingPhonesCategory);
        wait.until(ExpectedConditions.elementToBeClickable(gamingPhonesCategory));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", gamingPhonesCategory);

        logger.info("Selected gaming phones category.");
    }

    public void logFirstFivePhones() {
        wait.until(ExpectedConditions.visibilityOfAllElements(gamingPhones));
        for (int i = 0; i < 5 && i < gamingPhones.size(); i++) {
            WebElement item = gamingPhones.get(i);
            String name = item.findElement(By.xpath(".//h3[@data-auto='snippet-title']")).getText();
            String price = item.findElement(By.xpath(".//span[@data-auto='snippet-price-current']/span[1]")).getText();
            logger.info("Phone {}: Name = {}, Price = {}", i + 1, name, price);
        }
    }

    public void filterByManufacturerSamsung() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(samsungCheckbox)).click();
        Thread.sleep(4000);
        logger.info("Filtered by manufacturer Samsung.");
        goToElement(gamingPhones.get(0));
    }

    public boolean areAllPhonesFromSamsung() {
        wait.until(ExpectedConditions.visibilityOfAllElements(gamingPhones));
        for (WebElement item : gamingPhones) {
            String name = item.findElement(By.xpath(".//h3[@data-auto='snippet-title']")).getText();
            if (!name.toLowerCase().contains("samsung")) {
                return false;
            }
        }
        return true;
    }
}
