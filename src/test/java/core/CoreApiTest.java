package core;

import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract public class CoreApiTest {
    protected static final Logger logger = LoggerFactory.getLogger(CoreApiTest.class);

    @BeforeEach
    public void setUp() {
        logger.info("Настройка окружения для API-тестов");
        // Дополнительные настройки для API-тестов можно добавить здесь
    }
}