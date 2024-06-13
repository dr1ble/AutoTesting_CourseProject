package task5.tests;

import core.CoreTest;
import core.TestLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task5.pages.ReqresPage;

@ExtendWith(TestLogger.class)
@Feature("Проверка API и UI ReqresIn")
public class ReqresInFrontTests extends CoreTest {

    private ReqresPage reqresInPage;
    private static final Logger logger = LoggerFactory.getLogger(ReqresInFrontTests.class);

    @BeforeEach
    public void setUpEach() {
        Allure.step("Открыть страницу ReqresIn и инициализировать страницу", () -> {
            reqresInPage = new ReqresPage();
        });
    }

    @Test
    @DisplayName("Проверка ответов API и UI для различных конечных точек")
    @Story("Полная проверка ответов API и UI")
    @Description("Этот тест проверяет, что результаты нажатия кнопок соответствуют ответам API")
    public void test() {
        compareUIAndAPI("List users", "get");
        compareUIAndAPI("Single user", "get");
        compareUIAndAPI("Single user not found", "get");
        compareUIAndAPI("List <resource>", "get");
        compareUIAndAPI("Single <resource>", "get");
        compareUIAndAPI("Single <resource> not found", "get");
        compareUIAndAPI("Create", "post");
        compareUIAndAPI("Update", "put");
        compareUIAndAPI("Update", "patch");
        compareUIAndAPI("Delete", "delete");
        compareUIAndAPI("Register - successful", "post");
        compareUIAndAPI("Register - unsuccessful", "post");
        compareUIAndAPI("Login - successful", "post");
        compareUIAndAPI("Login - unsuccessful", "post");
        compareUIAndAPI("Delayed response", "get");
    }

    private void compareUIAndAPI(String buttonName, String httpMethod) {
        Allure.step("Проверить API ответ для " + buttonName + " с методом " + httpMethod.toUpperCase(), () -> {
            try {
                reqresInPage.clickOnButtonAndCheckAPI(buttonName, httpMethod);
                logger.info("Проверен ответ для '{}', соответствующий API", buttonName);
            } catch (AssertionError e) {
                logger.error("Тест провален: Проверка API ответов для различных конечных точек - {}", buttonName, e);
                throw e;
            }
        });
    }
}
