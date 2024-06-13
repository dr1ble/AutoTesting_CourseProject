package task2.pages;

import core.CorePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class MosPolyPageSchedule extends CorePage {

    private static final Logger logger = LoggerFactory.getLogger(MosPolyPageSchedule.class);

    @FindBy(xpath = "//input[@placeholder='группа ...']")
    private WebElement searchGroupField;

    @FindBy(xpath = "//div[contains(@class, 'found-groups')]")
    private WebElement groupsList;

    @FindBy(xpath = "//div[starts-with(@class,'schedule-day')]")
    private List<WebElement> weekDays;

    @FindBy(xpath = "//div[contains(@class, 'message') and contains(@class, 'not-print') and text()='Не нашлось расписание для группы']")
    private WebElement noScheduleMessage;

    public MosPolyPageSchedule() {
        PageFactory.initElements(driver, this);
    }

    public void enterGroupNumber(String groupNumber) {
        wait.until(ExpectedConditions.visibilityOf(searchGroupField));
        searchGroupField.clear();
        searchGroupField.sendKeys(groupNumber);
        logger.info("Введена группа с номером: {}", groupNumber);
    }

    public boolean isGroupInSearchResults(String groupNumber) {
        wait.until(ExpectedConditions.visibilityOfAllElements(groupsList));
        List<WebElement> webElements = groupsList.findElements(By.tagName("div"));
        return webElements.size() == 1 && webElements.get(0).getText().equals(groupNumber);
    }

    public boolean isNoScheduleMessageDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOf(noScheduleMessage));
            return noScheduleMessage.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickGroup() {
        click(groupsList.findElements(By.tagName("div")).get(0));
    }

    public boolean isCurrentDayHighlighted() {
        DayOfWeek currentDay = LocalDate.now().getDayOfWeek();
        String currentDayName = currentDay.getDisplayName(TextStyle.FULL, new Locale("ru"));
        WebElement currentDayElement = driver.findElement(
                By.xpath("//div[contains(@class, 'day') and contains(text(), '" + currentDayName + "') and contains(@class, 'highlighted')]"));
        return currentDayElement != null && currentDayElement.isDisplayed();
    }
}
