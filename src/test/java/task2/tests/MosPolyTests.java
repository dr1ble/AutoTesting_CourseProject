package task2.tests;

import core.CoreTest;
import core.TestLogger;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task2.pages.MosPolyPage;
import task2.pages.MosPolyPageSchedule;

@ExtendWith(TestLogger.class)
@Feature("Тестирование сайта Московский политех")
public class MosPolyTests extends CoreTest {
    private static final Logger logger = LoggerFactory.getLogger(MosPolyTests.class);
    private MosPolyPage mosPolytechPage;
    private MosPolyPageSchedule mosPolytechPageSchedules;
    private final String groupNumber = "231-361";

    @BeforeEach
    public void setUpEach() {
        Allure.step("Открыть страницу Mospolytech и инициализировать объекты страниц", () -> {
            mosPolytechPage = new MosPolyPage();
        });
    }

    @Test
    @DisplayName("Тестирование сайта MosPolytech")
    @Description("Этот тест выполняет серию проверок и действий на сайте MosPolytech.")
    public void test() {
        logger.info("Начало теста для сайта MosPolytech");

        Allure.step("Нажать на кнопку 'Расписание'", mosPolytechPage::openSchedule);
        Allure.step("Прокрутить и нажать на ссылку 'Смотрите на сайте'", mosPolytechPage::clickViewOnSiteLink);
        Allure.step("Переключиться на новую вкладку и инициализировать страницу расписания", () -> {
            mosPolytechPageSchedules = new MosPolyPageSchedule();
            mosPolytechPageSchedules.switchWindow();
        });

        Allure.step("Ввести номер группы в поле поиска", () -> {
            mosPolytechPageSchedules.enterGroupNumber(groupNumber);
        });

        Allure.step("Проверить, что группа есть в результатах поиска", () -> {
            boolean isGroupFound = mosPolytechPageSchedules.isGroupInSearchResults(groupNumber);
            logger.info("Группа '{}' найдена в результатах поиска: {}", groupNumber, isGroupFound);
            Assertions.assertTrue(isGroupFound, "Группа не найдена в результатах поиска");
        });

        Allure.step("Нажать на группу в результатах поиска", mosPolytechPageSchedules::clickGroup);

        Allure.step("Проверить, отображается ли расписания", () -> {
            boolean isNoScheduleMessageDisplayed = mosPolytechPageSchedules.isNoScheduleMessageDisplayed();
            if (isNoScheduleMessageDisplayed) {
                logger.warn("Расписание для группы '{}' отсутствует в период сессии", groupNumber);
                Assertions.fail("Расписание для группы '" + groupNumber + "' отсутствует в период сессии");
            } else {
                Allure.step("Проверить, что текущий день выделен", () -> {
                    boolean isCurrentDayHighlighted = mosPolytechPageSchedules.isCurrentDayHighlighted();
                    logger.info("Текущий день выделен: {}", isCurrentDayHighlighted);
                    Assertions.assertTrue(isCurrentDayHighlighted, "Текущий день не выделен в расписании");
                });
            }
        });
    }
}
