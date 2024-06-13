package task4.pages;

import core.CorePage;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DrHeadPage extends CorePage {
    private static final Logger logger = LoggerFactory.getLogger(DrHeadPage.class);

    @Getter
    private String currentItemName;

    @FindBy(xpath = "//div[@class='header-search search-block']//input[@id='top-search-line']")
    private WebElement searchField;

    @FindBy(xpath = "//button[@class='button search-block__button' and @aria-label='Найти']")
    private WebElement searchButton;

    @FindBy(xpath = "//div[@class='link header-nav__item dropdown dropdown_hover']/a[@class='link dropdown-value' and contains(., 'Наушники') and contains(., 'personal аудио')]")
    private WebElement headphonesMenuSection;

    @FindBy(xpath = "//a[@class='link header-catalog-main-nav__item' and text()='Беспроводные']")
    private WebElement wirelessHeadphonesCategory;

    @FindBy(xpath = "//div[@class='localization-close js-location-question-close close-link']")
    private WebElement closeChooseCityButton;

    @FindBy(xpath = "//div[contains(@class, 'catalog-list__item') and contains(@class, 'product')]")
    private List<WebElement> productListItems;

    @FindBy(xpath = "//button[@class='button button_base buy-button add-cart']")
    private WebElement buyButton;

    @FindBy(xpath = "//div[@class='custom-alert__buttons']//button[@class='button button_primary js-cart-btn' and text()='Перейти в корзину']")
    private WebElement goToCartButton;

    @FindBy(xpath = "//td[@class='cart-table__title']//a[@class='link']")
    private WebElement cartItem;

    @FindBy(xpath = "//button[@class='button button-remove']")
    private WebElement removeCartItemButton;

    @FindBy(xpath = "//td[@class='cart-table__title']/a[contains(text(),'Вы удалили')]")
    private WebElement removedItemMessage;

    @FindBy(xpath = "//label[@class='check' and .//span[text()='AKAI']]")
    private WebElement akaiBrandFilter;

    public DrHeadPage() {
        driver.get("https://doctorhead.ru/");
        PageFactory.initElements(driver, this);
    }

    public void closeChooseCity() {
        click(closeChooseCityButton);
        logger.info("Выпадающее меню с выбором города закрыто");
    }

    public void openHeadphonesMenuSection() {
        goToElement(headphonesMenuSection);
        logger.info("Выпадающее меню с наушниками и personal аудио открыто");
    }

    public void goToWirelessHeadphonesCategory() {
        click(wirelessHeadphonesCategory);
        logger.info("Категория с беспроводными наушниками открыта");
    }

    public boolean areFirstFiveHeadphonesWireless() {
        wait.until(ExpectedConditions.visibilityOfAllElements(productListItems));
        goToElement(productListItems.get(0));
        for (int i = 0; i < 5 && i < productListItems.size(); i++) {
            WebElement item = productListItems.get(i);
            String itemText = item.getText().toLowerCase();
            logger.info("Товар {}: {}", i + 1, getProductInfo(item));
            if (!itemText.contains("беспроводные")) {
                return false;
            }
        }
        return true;
    }

    private String getProductInfo(WebElement productItem) {
        String title = productItem.findElement(By.xpath(".//a[contains(@class, 'product-title')]")).getText();
        String category = productItem.findElement(By.xpath(".//span[contains(@class, 'product-category')]")).getText();

        String price;
        try {
            price = productItem.findElement(By.xpath(".//div[contains(@class, 'product-price')]//span[@class='span-like nowrap']")).getText();
        } catch (NoSuchElementException e) {
            price = null;
        }

        if (price != null) {
            return String.format("Название: %s, Категория: %s, Цена: %s", title, category, price);
        } else {
            return String.format("Название: %s, Категория: %s", title, category);
        }
    }


    public void searchForItem(String query) {
        wait.until(ExpectedConditions.visibilityOf(searchField));
        searchField.clear();
        searchField.sendKeys(query);
        logger.info("Введен текст поиска: {}", query);
        click(searchButton);
    }

    public void addItemToCart() throws InterruptedException {
        wait.until(ExpectedConditions.elementToBeClickable(buyButton));
        currentItemName = productListItems.get(0).findElement(By.xpath(".//a[contains(@class, 'product-title')]")).getText();
        click(buyButton);
        logger.info("Товар добавлен в корзину: {}", currentItemName);
        Thread.sleep(1000);
    }

    public void goToCart() {
        wait.until(ExpectedConditions.visibilityOf(goToCartButton));
        click(goToCartButton);
        logger.info("Переход к корзине");
    }

    public boolean isItemInCart(String searchQuery) {
        wait.until(ExpectedConditions.visibilityOf(cartItem));
        String cartItemName = cartItem.getText();
        logger.info("Товар в корзине: {}", cartItemName);

        // Создаем паттерн, который ищет слово, игнорируя регистр
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(searchQuery) + "\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(cartItemName);

        return matcher.find();
    }

    public void removeItemFromCart() {
        click(removeCartItemButton);
        logger.info("Товар удален из корзины");
    }

    public boolean isItemRemoved(String searchQuery) {
        wait.until(ExpectedConditions.visibilityOf(removedItemMessage));
        String removedMessage = removedItemMessage.getText();
        logger.info("Сообщение об удалении товара: {}", removedMessage);

        // Создаем паттерн, который ищет слово, игнорируя регистр
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(searchQuery) + "\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(removedMessage);

        return matcher.find();
    }


    public void applyBrandFilter() throws InterruptedException {
        wait.until(ExpectedConditions.visibilityOf(akaiBrandFilter));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", akaiBrandFilter);
        wait.until(ExpectedConditions.elementToBeClickable(akaiBrandFilter));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", akaiBrandFilter);
        logger.info("Фильтр по бренду AKAI применен");
    }

    public boolean areFirstFiveItemsFromBrand(String brand) {
        wait.until(ExpectedConditions.visibilityOfAllElements(productListItems));
        goToElement(productListItems.get(0));
        for (int i = 0; i < 5 && i < productListItems.size(); i++) {
            WebElement item = productListItems.get(i);
            String itemText = item.getText().toLowerCase();
            logger.info("Товар {}: {}", i + 1, getProductInfo(item));
            if (!itemText.contains(brand.toLowerCase())) {
                return false;
            }
        }
        return true;
    }
}
