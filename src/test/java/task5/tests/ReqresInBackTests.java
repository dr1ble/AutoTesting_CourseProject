package task5.tests;

import core.CoreApiTest;
import core.TestLogger;
import io.qameta.allure.*;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import task5.data.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(TestLogger.class)
@Feature("ReqresIn Backend tests")
public class ReqresInBackTests extends CoreApiTest {

    private static final Logger logger = LoggerFactory.getLogger(ReqresInBackTests.class);

    @Test
    @DisplayName("Полный тест API ReqresIn")
    @Description("Этот тест выполняет все проверки для различных конечных точек API ReqresIn")
    public void completeApiTest() {
        try {
            getListUsers();
            getSingleUser();
            getUserNotFound();
            getListResources();
            getSingleResource();
            getResourceNotFound();
            createUser();
            updateUserPut();
            updateUserPatch();
            deleteUser();
            registerSuccessful();
            registerUnsuccessful();
            loginSuccessful();
            loginUnsuccessful();
            getUsersWithDelay();
            logger.info("Тест 'Полный тест API ReqresIn' завершен успешно");
        } catch (AssertionError e) {
            logger.error("Тест 'Полный тест API ReqresIn' завершен с ошибкой", e);
            throw e;
        }
    }

    @Step("[GET] Получить пользователей со 2-й страницы")
    public void getListUsers() {
        List<UserData> users = given().
                when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UsersListSchema.json"))
                .body("page", equalTo(2))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .extract().jsonPath().getList("data", UserData.class);
        assertThat(users).extracting(UserData::getId).isNotNull();
        assertThat(users).extracting(UserData::getFirst_name).contains("Tobias");
        assertThat(users).extracting(UserData::getLast_name).contains("Funke");
        logger.info("Тест '[GET] Получить пользователей со 2-й страницы' завершен успешно");
    }

    @Step("[GET] Получить пользователя с id = 2")
    public void getSingleUser() {
        UserData user = given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UserSingleSchema.json"))
                .extract().jsonPath().getObject("data", UserData.class);
        assertThat(user.getId()).isEqualTo(2);
        assertThat(user.getEmail()).isEqualTo("janet.weaver@reqres.in");
        assertThat(user.getFirst_name()).isEqualTo("Janet");
        assertThat(user.getLast_name()).isEqualTo("Weaver");
        assertThat(user.getAvatar()).isEqualTo("https://reqres.in/img/faces/2-image.jpg");
        logger.info("Тест '[GET] Получить пользователя с id = 2' завершен успешно");
    }

    @Step("[GET] Получить пользователя с неверным id")
    public void getUserNotFound() {
        given().
                when()
                .get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
        logger.info("Тест '[GET] Получить пользователя с неверным id' завершен успешно");
    }

    @Step("[GET] Получить список ресурсов")
    public void getListResources() {
        List<ResourceData> resources = given().
                when()
                .get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/ResourcesListSchema.json"))
                .body("page", equalTo(1))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .extract().jsonPath().getList("data", ResourceData.class);

        assertThat(resources).extracting(ResourceData::getId).isNotNull();
        assertThat(resources).extracting(ResourceData::getName).contains("cerulean");
        assertThat(resources).extracting(ResourceData::getYear).contains(2000);
        assertThat(resources).extracting(ResourceData::getColor).contains("#98B2D1");
        assertThat(resources).extracting(ResourceData::getPantone_value).contains("15-4020");
        logger.info("Тест '[GET] Получить список ресурсов' завершен успешно");
    }

    @Step("[GET] Получить ресурс с id = 2")
    public void getSingleResource() {
        ResourceData resource = given().
                when()
                .get("https://reqres.in/api/unknown/2")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/ResourceSingleSchema.json"))
                .extract().jsonPath().getObject("data", ResourceData.class);

        assertThat(resource).isNotNull();
        assertThat(resource.getId()).isEqualTo(2);
        assertThat(resource.getName()).isEqualTo("fuchsia rose");
        assertThat(resource.getYear()).isEqualTo(2001);
        assertThat(resource.getColor()).isEqualTo("#C74375");
        assertThat(resource.getPantone_value()).isEqualTo("17-2031");
        logger.info("Тест '[GET] Получить ресурс с id = 2' завершен успешно");
    }

    @Step("[GET] Получить ресурс с неверным id")
    public void getResourceNotFound() {
        given().
                when()
                .get("https://reqres.in/api/unknown/23")
                .then()
                .statusCode(404)
                .body(equalTo("{}"));
        logger.info("Тест '[GET] Получить ресурс с неверным id' завершен успешно");
    }

    @Step("[POST] Создать пользователя с именем: morpheus | должность: leader")
    public void createUser() {
        UserRequest userRequest =
                UserRequest.builder()
                        .name("morpheus")
                        .job("leader")
                        .build();
        UserResponse userResponse = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UserCreateResponseSchema.json"))
                .extract().as(UserResponse.class);
        assertThat(userResponse.getName()).isEqualTo(userRequest.getName());
        assertThat(userResponse.getJob()).isEqualTo(userRequest.getJob());
        logger.info("Тест '[POST] Создать пользователя с именем: morpheus | должность: leader' завершен успешно");
    }

    @Step("[PUT] Обновить пользователя методом PUT")
    public void updateUserPut() {
        UserRequest userRequest =
                UserRequest.builder()
                        .name("morpheus")
                        .job("zion resident")
                        .build();

        UserResponse userResponse = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .put("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UserUpdateResponseSchema.json"))
                .extract().as(UserResponse.class);

        assertThat(userResponse.getName()).isEqualTo(userRequest.getName());
        assertThat(userResponse.getJob()).isEqualTo(userRequest.getJob());
        logger.info("Тест '[PUT] Обновить пользователя методом PUT' завершен успешно");
    }

    @Step("[PATCH] Обновить пользователя методом PATCH")
    public void updateUserPatch() {
        UserRequest userRequest =
                UserRequest.builder()
                        .name("morpheus")
                        .job("zion resident")
                        .build();

        UserResponse userResponse = given()
                .contentType(ContentType.JSON)
                .body(userRequest)
                .when()
                .patch("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UserUpdateResponseSchema.json"))
                .extract().as(UserResponse.class);

        assertThat(userResponse.getName()).isEqualTo(userRequest.getName());
        assertThat(userResponse.getJob()).isEqualTo(userRequest.getJob());
        logger.info("Тест '[PATCH] Обновить пользователя методом PATCH' завершен успешно");
    }

    @Step("[DELETE] Удалить пользователя")
    public void deleteUser() {
        given().
                when()
                .delete("https://reqres.in/api/users/2")
                .then()
                .statusCode(204)
                .body(equalTo(""));
        logger.info("Тест '[DELETE] Удалить пользователя' завершен успешно");
    }

    @Step("[POST] Успешная регистрация")
    public void registerSuccessful() {
        RegisterAndLoginRequest request =
                RegisterAndLoginRequest.builder()
                        .email("eve.holt@reqres.in")
                        .password("pistol")
                        .build();

        RegisterAndLoginResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/RegisterSuccessfulResponseSchema.json"))
                .extract().as(RegisterAndLoginResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(4);
        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
        logger.info("Тест '[POST] Успешная регистрация' завершен успешно");
    }

    @Step("[POST] Неуспешная регистрация")
    public void registerUnsuccessful() {
        RegisterAndLoginRequest request =
                RegisterAndLoginRequest.builder()
                        .email("sydney@fife")
                        .build();

        RegisterAndLoginResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/RegisterUnsuccessfulResponseSchema.json"))
                .extract().as(RegisterAndLoginResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getError()).isEqualTo("Missing password");
        logger.info("Тест '[POST] Неуспешная регистрация' завершен успешно");
    }

    @Step("[POST] Успешный вход в систему")
    public void loginSuccessful() {
        RegisterAndLoginRequest request =
                RegisterAndLoginRequest.builder()
                        .email("eve.holt@reqres.in")
                        .password("cityslicka")
                        .build();

        RegisterAndLoginResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/LoginSuccessfulResponseSchema.json"))
                .extract().as(RegisterAndLoginResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
        logger.info("Тест '[POST] Успешный вход в систему' завершен успешно");
    }

    @Step("[POST] Неуспешный вход в систему")
    public void loginUnsuccessful() {
        RegisterAndLoginRequest request =
                RegisterAndLoginRequest.builder()
                        .email("peter@klaven")
                        .build();

        RegisterAndLoginResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(400)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/LoginUnsuccessfulResponseSchema.json"))
                .extract().as(RegisterAndLoginResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getError()).isEqualTo("Missing password");
        logger.info("Тест '[POST] Неуспешный вход в систему' завершен успешно");
    }

    @Step("[GET] Получить список пользователей с задержкой 3 секунды")
    public void getUsersWithDelay() {
        List<UserData> users = given()
                .when()
                .get("https://reqres.in/api/users?delay=3")
                .then()
                .statusCode(200)
                .time(greaterThan(3000L))
                .and().time(lessThan(5000L))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("jsonSchemas/UsersListSchema.json"))
                .body("page", equalTo(1))
                .body("per_page", equalTo(6))
                .body("total", equalTo(12))
                .body("total_pages", equalTo(2))
                .extract().jsonPath().getList("data", UserData.class);

        assertThat(users).extracting(UserData::getId).isNotNull();
        assertThat(users).extracting(UserData::getFirst_name).contains("George");
        assertThat(users).extracting(UserData::getLast_name).contains("Bluth");
        logger.info("Тест '[GET] Получить список пользователей с задержкой 3 секунды' завершен успешно");
    }
}
