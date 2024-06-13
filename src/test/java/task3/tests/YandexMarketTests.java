package task3.tests;

import core.CoreTest;
import core.TestLogger;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task3.pages.YandexMarketPage;

@ExtendWith(TestLogger.class)
@Feature("Тесты Яндекс.Маркета")
public class YandexMarketTests extends CoreTest {

    private YandexMarketPage yandexMarketPage;
    private static final Logger logger = LoggerFactory.getLogger(YandexMarketTests.class);

    @BeforeEach
    public void setUpEach() {
        Allure.step("Открытие страницы Яндекс.Маркета и инициализация страницы", () -> {
            yandexMarketPage = new YandexMarketPage();
        });
    }

    // ВАРИАНТ 2
    @Test
    @DisplayName("Тестирование категории игровых телефонов и фильтрации по производителю Samsung")
    @Description("Этот тест выполняет серию проверок и действий в категории игровых телефонов на Яндекс.Маркете и проверяет фильтрацию по Samsung.")
    public void test() {
        logger.info("Запуск комплексного теста для категории игровых телефонов на Яндекс.Маркете с фильтром по Samsung");

        Allure.step("Открыть меню каталога", yandexMarketPage::openCatalog);

        Allure.step("Выбрать категорию Игровые телефоны", yandexMarketPage::selectGamingPhonesCategory);

        Allure.step("Вывести в лог первые 5 игровых телефонов", yandexMarketPage::logFirstFivePhones);

        Allure.step("Применить фильтр по производителю Samsung", yandexMarketPage::filterByManufacturerSamsung);

        Allure.step("Проверить, что все отображенные телефоны от Samsung", () -> {
            yandexMarketPage.logFirstFivePhones();
            boolean allPhonesFromSamsung = yandexMarketPage.areAllPhonesFromSamsung();
            Assertions.assertTrue(allPhonesFromSamsung, "Не все отображенные телефоны от Samsung.");
        });
    }
}
