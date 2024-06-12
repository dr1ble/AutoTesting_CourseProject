package task1.tests;

import core.CoreTest;
import core.TestLogger;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task1.pages.LambdaPage;

@ExtendWith(TestLogger.class)
@Feature("LambdaTest Application Tests")
public class LambdaPageTests extends CoreTest {

    private LambdaPage lambdaPage;
    private static final Logger logger = LoggerFactory.getLogger(LambdaPageTests.class);

    @BeforeEach
    public void setUpEach() {
        Allure.step("Открыть пример приложения LambdaTest и инициализировать страницу", () -> {
            lambdaPage = new LambdaPage();
        });
    }

    @Test
    @DisplayName("Тестирование сайта LambdaTest")
    @Description("Этот тест выполняет серию проверок и действий прописанных в тест-кейсе для сайтаLambdaTest.")
    public void test() {
        logger.info("Начало комплексного теста для примера приложения LambdaTest");
        verifyHeaderText("LambdaTest Sample App");
        verifyTextIsDisplayed("5 of 5 remaining");
        verifyItemNotChecked(1);
        checkFirstElement();

        for (int i = 2; i <= 5; i++) {
            verifyItemNotChecked(i);
            checkAndVerifyItem(i);
        }

        String newItemText = "New Todo Item";
        addAndVerifyNewItem(newItemText);
        checkAndVerifyNewItem(newItemText);
    }


    private void verifyHeaderText(String headerText) {
        Allure.step("Проверить, что заголовок соответсвует " + headerText, () -> {
            boolean isHeader = lambdaPage.checkHeaderText(headerText);
            logger.info("Заголовок '{}' отображается: {}", headerText, isHeader);
            Assertions.assertTrue(isHeader, "Текст '" + headerText + "' не отображается.");
        });
    }

    private void checkFirstElement() {
        Allure.step("Отметить первый элемент", () -> {
            lambdaPage.checkItem(1);
            logger.info("Галочка у 1 элемента поставлена");
        });
    }

    private void verifyTextIsDisplayed(String expectedText) {
        Allure.step("Проверить, что текст '" + expectedText + "' отображается", () -> {
            boolean isDisplayed = lambdaPage.isRemainingTextPresent();
            logger.info("Текст '{}' отображается: {}", expectedText, isDisplayed);
            Assertions.assertTrue(isDisplayed, "Текст '" + expectedText + "' не отображается.");
        });
    }

    private void verifyItemNotChecked(int itemIndex) {
        Allure.step("Проверить, что элемент " + itemIndex + " не отмечен", () -> {
            boolean isNotChecked = lambdaPage.isItemNotChecked(itemIndex);
            logger.info("Элемент {} не отмечен: {}", itemIndex, isNotChecked);
            Assertions.assertTrue(isNotChecked, "Элемент " + itemIndex + " должен быть не отмечен, но он отмечен.");
        });
    }

    private void checkAndVerifyItem(int itemIndex) {
        Allure.step("Отметить элемент " + itemIndex, () -> {
            lambdaPage.checkItem(itemIndex);
            boolean isChecked = !lambdaPage.isItemNotChecked(itemIndex);
            logger.info("Элемент {} отмечен: {}", itemIndex, isChecked);
            Assertions.assertFalse(lambdaPage.isItemNotChecked(itemIndex), "Элемент " + itemIndex + " должен быть отмечен, но он не отмечен.");
        });
    }


    private void addAndVerifyNewItem(String newItemText) {
        Allure.step("Добавить новый элемент списка дел: " + newItemText, () -> {
            lambdaPage.addNewTodoItem(newItemText);
            boolean isNewItemAdded = lambdaPage.isTodoItemPresent(newItemText);
            logger.info("Новый элемент списка дел добавлен: {}", isNewItemAdded);
            Assertions.assertTrue(isNewItemAdded, "Новый элемент списка дел не был добавлен в список.");
        });
    }


    //a
    private void checkAndVerifyNewItem(String newItemText) {
        Allure.step("Отметить новый элемент списка дел", () -> {
            lambdaPage.checkTodoItem(newItemText);
            boolean isNewItemChecked = lambdaPage.isTodoItemChecked(newItemText);
            logger.info("Новый элемент списка дел отмечен: {}", isNewItemChecked);
            Assertions.assertTrue(isNewItemChecked, "Новый элемент списка дел должен быть отмечен, но он не отмечен.");
        });
    }
}
