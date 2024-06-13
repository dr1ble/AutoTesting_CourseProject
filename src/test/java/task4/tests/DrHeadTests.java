package task4.tests;

import core.CoreTest;
import core.TestLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task4.pages.DrHeadPage;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(TestLogger.class)
@Feature("DrHead Tests")
public class DrHeadTests extends CoreTest {

    private DrHeadPage drHeadPage;
    private static final Logger logger = LoggerFactory.getLogger(DrHeadTests.class);
    private String itemName = "наушники JBL";
    private String brand = "AKAI";
    private String midiKeyboardQuery = "MIDI-клавиатура";

    @BeforeEach
    public void setUpEach() {
        Allure.step("Открытие страницы DrHead и инициализация страницы", () -> {
            drHeadPage = new DrHeadPage();
        });
    }

    @Test
    @DisplayName("Тестирование поиска через меню для сайта DrHead")
    @Description("Этот тест выполняет серию проверок и действий, связанных с поиском товаров через выпадающее меню.")
    public void searchWithMenuTest() throws InterruptedException {
        logger.info("Начинаем тест поиска через меню для DrHead ");
        Allure.step("Закрываем выпадающий баннер с выбором города", drHeadPage::closeChooseCity);
        Thread.sleep(1500);
        Allure.step("Наводимся в верхнем меню категорию Наушники и personal аудио", drHeadPage::openHeadphonesMenuSection);
        Allure.step("В выпадающем меню выбираем категорию Наушники -> Беспроводные", drHeadPage::goToWirelessHeadphonesCategory);

        Allure.step("Проверяем, что первые 5 наушников являются беспроводными", () -> {
            assertTrue(drHeadPage.areFirstFiveHeadphonesWireless(), "Не все первые 5 наушников являются беспроводными.");
        });
    }


    @Test
    @DisplayName("Поиск товара, добавление в корзину и удаление")
    @Description("Этот тест выполняет поиск товара, добавление товара в корзину и его удаление.")
    public void searchAndAddToCartTest() throws InterruptedException {

        logger.info("Начинаем тест поиска, добавления в корзину и удаления для DrHead");
        Allure.step("Закрываем выпадающий баннер с выбором города", drHeadPage::closeChooseCity);
        Thread.sleep(1500);
        Allure.step("Вводим текст в поле поиска", () -> drHeadPage.searchForItem(itemName));
        Allure.step("Добавляем первый товар из списка в корзину", drHeadPage::addItemToCart);
        Thread.sleep(2000);
        String currentItemName = drHeadPage.getCurrentItemName();
        Allure.step("Переходим к корзине", drHeadPage::goToCart);
        Allure.step("Проверяем, что добавленный товар находится в корзине", () -> {
            assertTrue(drHeadPage.isItemInCart(currentItemName), "Товар не найден в корзине.");
        });
        Allure.step("Удаляем товар из корзины", drHeadPage::removeItemFromCart);
        Allure.step("Проверяем, что появилось сообщение об удалении товара", () -> {
            assertTrue(drHeadPage.isItemRemoved(currentItemName), "Сообщение об удалении товара не отображено.");
        });
    }

    @Test
    @DisplayName("Поиск и фильтрация товаров по бренду")
    @Description("Этот тест выполняет поиск товаров и фильтрацию по бренду, проверяя, что первые 5 товаров соответствуют бренду AKAI.")
    public void searchAndFilterByBrandTest() throws InterruptedException {
        logger.info("Начинаем тест поиска и фильтрации по бренду для DrHead");
        Allure.step("Закрываем выпадающий баннер с выбором города", drHeadPage::closeChooseCity);
        Thread.sleep(1500);
        Allure.step("Вводим текст в поле поиска", () -> drHeadPage.searchForItem(midiKeyboardQuery));
        Allure.step("Применяем фильтр по бренду AKAI", drHeadPage::applyBrandFilter);
        Thread.sleep(5000);
        Allure.step("Проверяем, что первые 5 товаров принадлежат бренду AKAI", () -> {
            assertTrue(drHeadPage.areFirstFiveItemsFromBrand(brand), "Не все первые 5 товаров принадлежат бренду AKAI.");
        });
    }
}
