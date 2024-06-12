package task1.pages;

import core.CorePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LambdaPage extends CorePage {
    private static final Logger logger = LoggerFactory.getLogger(LambdaPage.class);

    @FindBy(xpath = "//h2")
    private WebElement header;

    @FindBy(xpath = "//span[@class='ng-binding'][contains(text(), '5 of 5 remaining')]")
    private WebElement remainingText;

    @FindBy(xpath = "//li")
    private List<WebElement> todoItems;

    @FindBy(id = "sampletodotext")
    private WebElement newTodoInput;

    @FindBy(id = "addbutton")
    private WebElement addButton;

    public LambdaPage() {
        driver.get("https://lambdatest.github.io/sample-todo-app/");
        PageFactory.initElements(driver, this);
    }

    private WebElement waitForElement(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public String getHeaderText() {
        wait.until(ExpectedConditions.visibilityOf(header));
        return header.getText();
    }

    public boolean isRemainingTextPresent() {
        wait.until(ExpectedConditions.visibilityOf(remainingText));
        return remainingText.isDisplayed();
    }

    public boolean isItemNotChecked(int itemIndex) {
        try {
            WebElement item = todoItems.get(itemIndex - 1);
            WebElement itemSpan = item.findElement(By.xpath(".//span"));
            boolean isNotChecked = itemSpan.getAttribute("class").contains("done-false");
            logger.info("Элемент {}: класс спана содержит 'done-false': {}", itemIndex, isNotChecked);
            return isNotChecked;
        } catch (Exception e) {
            logger.error("Не удалось проверить, что элемент {} не отмечен", itemIndex, e);
            return false;
        }
    }

    public void checkItem(int itemIndex) {
        try {
            WebElement itemCheckbox = todoItems.get(itemIndex - 1).findElement(By.xpath(".//input[@type='checkbox']"));
            click(itemCheckbox);
        } catch (Exception e) {
            logger.error("Не удалось отметить элемент {}", itemIndex, e);
        }
    }

    public void addNewTodoItem(String todoText) {
        wait.until(ExpectedConditions.visibilityOf(newTodoInput)).sendKeys(todoText);
        click(addButton);
        waitForElement(By.xpath("//li/span[contains(text(), '" + todoText + "')]"));
    }

    public boolean isTodoItemPresent(String todoText) {
        try {
            waitForElement(By.xpath("//li/span[contains(text(), '" + todoText + "')]"));
            return true;
        } catch (Exception e) {
            logger.error("Элемент списка '{}' не найден", todoText, e);
            return false;
        }
    }

    public void checkTodoItem(String todoText) {
        try {
            WebElement itemCheckbox = waitForElement(By.xpath("//li[span[text()='" + todoText + "']]//input[@type='checkbox']"));
            click(itemCheckbox);
        } catch (Exception e) {
            logger.error("Не удалось отметить элемент списка '{}'", todoText, e);
        }
    }

    public boolean isTodoItemChecked(String todoText) {
        try {
            WebElement itemSpan = waitForElement(By.xpath("//li[span[text()='" + todoText + "']]//span"));
            boolean isChecked = itemSpan.getAttribute("class").contains("done-true");
            logger.info("Элемент '{}': класс спана содержит 'done-true': {}", todoText, isChecked);
            return isChecked;
        } catch (Exception e) {
            logger.error("Не удалось проверить, что элемент списка '{}' отмечен", todoText, e);
            return false;
        }
    }

    public boolean checkHeaderText(String expectedText) {
        String actualText = getHeaderText();
        logger.info("Проверка заголовка: ожидалось '{}', получено '{}'", expectedText, actualText);
        return expectedText.equals(actualText);
    }

}
