package task2.pages;

import core.CorePage;
import core.CorePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MosPolyPage extends CorePage {

    private static final Logger logger = LoggerFactory.getLogger(MosPolyPage.class);

    @FindBy(xpath = "//a[@title='Расписание']")
    private WebElement raspButton;

    @FindBy(xpath = "//a[@href='https://rasp.dmami.ru/']")
    private WebElement viewOnSiteLink;

    public MosPolyPage() {
        driver.get("https://mospolytech.ru/");
        PageFactory.initElements(driver, this);
    }

    public void openSchedule() {
        click(raspButton);
    }

    public void clickViewOnSiteLink() {
        goToElement(viewOnSiteLink);
        click(viewOnSiteLink);
    }
}
