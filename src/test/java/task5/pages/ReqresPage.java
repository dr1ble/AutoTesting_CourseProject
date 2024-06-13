package task5.pages;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import core.CorePage;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static io.restassured.RestAssured.given;

public class ReqresPage extends CorePage {
    private static final Logger logger = LoggerFactory.getLogger(ReqresPage.class);

    @FindBy(xpath = "//li[@data-id]")
    private List<WebElement> buttonList;

    @FindBy(xpath = "//pre[@data-key='output-response']")
    private WebElement outputResponse;

    @FindBy(xpath = "//pre[@data-key='output-request']")
    private WebElement outputRequest;

    @FindBy(xpath = "//span[@class='url']")
    private WebElement urlRequest;

    @FindBy(xpath = "//span[contains(@class,'response-code')]")
    private WebElement responseCode;

    public ReqresPage() {
        driver.get("https://reqres.in/");
        PageFactory.initElements(driver, this);
    }

    public void clickOnButtonAndCheckAPI(String nameButton, String httpMethod) {
        for (WebElement button : buttonList) {
            WebElement request = button.findElement(By.xpath("./a"));
            if (request.getText().equalsIgnoreCase(nameButton) && button.getAttribute("data-http").equalsIgnoreCase(httpMethod)) {
                goToElement(button);
                button.click();
                wait.until(ExpectedConditions.visibilityOf(outputResponse));

                Assertions.assertEquals("active", button.getAttribute("class"), "Button was not clicked");

                String url = request.getAttribute("href").trim();
                String expectedResponse = outputResponse.getText().trim();
                int expectedStatusCode = Integer.parseInt(responseCode.getText().trim());
                String requestBody = outputRequest.getText().trim();

                switch (httpMethod.toLowerCase()) {
                    case "get":
                        Assertions.assertEquals(trimJson(sendGetRequest(url)), trimJson(expectedResponse), "Response did not match API result");
                        Assertions.assertEquals(getStatusCode(url), expectedStatusCode, "Status code did not match");
                        break;
                    case "post":
                        compareJsonResponses(trimJson(sendPostRequest(url, requestBody)), trimJson(expectedResponse));
                        Assertions.assertEquals(getPostStatusCode(url, requestBody), expectedStatusCode, "Status code did not match");
                        break;
                    case "delete":
                        Assertions.assertEquals(trimJson(sendDeleteRequest(url)), trimJson(expectedResponse), "Response did not match API result");
                        Assertions.assertEquals(getDeleteStatusCode(url), expectedStatusCode, "Status code did not match");
                        break;
                    case "put":
                        compareJsonResponses(trimJson(sendPutRequest(url, requestBody)), trimJson(expectedResponse));
                        Assertions.assertEquals(getPutStatusCode(url, requestBody), expectedStatusCode, "Status code did not match");
                        break;
                    case "patch":
                        compareJsonResponses(trimJson(sendPatchRequest(url, requestBody)), trimJson(expectedResponse));
                        Assertions.assertEquals(getPatchStatusCode(url, requestBody), expectedStatusCode, "Status code did not match");
                        break;
                    default:
                        Assertions.fail("An error occurred");
                        break;
                }

                Assertions.assertEquals(url, "https://reqres.in" + urlRequest.getText().trim(), "URL did not match");

                logger.info("Checked response of '{}' against the API", nameButton);
                return;
            }
        }

        Assertions.fail("Button '" + nameButton + "' not found");
    }

    private String sendGetRequest(String url) {
        return given()
                .when()
                .get(url)
                .then()
                .extract().response().asPrettyString().trim();
    }

    private int getStatusCode(String url) {
        return given()
                .when()
                .get(url)
                .then()
                .extract().statusCode();
    }

    private String sendPostRequest(String url, String requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(url)
                .then()
                .extract().response().asPrettyString().trim();
    }

    private int getPostStatusCode(String url, String requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(url)
                .then()
                .extract().statusCode();
    }

    private String sendDeleteRequest(String url) {
        return given()
                .when()
                .delete(url)
                .then()
                .extract().response().asPrettyString().trim();
    }

    private int getDeleteStatusCode(String url) {
        return given()
                .when()
                .delete(url)
                .then()
                .extract().statusCode();
    }

    private String sendPutRequest(String url, String requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(url)
                .then()
                .extract().response().asPrettyString().trim();
    }

    private int getPutStatusCode(String url, String requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .put(url)
                .then()
                .extract().statusCode();
    }

    private String sendPatchRequest(String url, String requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(url)
                .then()
                .extract().response().asPrettyString().trim();
    }

    private int getPatchStatusCode(String url, String requestBody) {
        return given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .patch(url)
                .then()
                .extract().statusCode();
    }

    private String trimJson(String json) {
        return json.replaceAll("\\s+", "");
    }

    private void compareJsonResponses(String expectedResponse, String actualResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode expectedJson = mapper.readTree(expectedResponse);
            JsonNode actualJson = mapper.readTree(actualResponse);

            ObjectNode expectedObjectNode = (ObjectNode) expectedJson;
            ObjectNode actualObjectNode = (ObjectNode) actualJson;

            expectedObjectNode.remove("id");
            expectedObjectNode.remove("createdAt");
            expectedObjectNode.remove("updatedAt");
            actualObjectNode.remove("id");
            actualObjectNode.remove("createdAt");
            actualObjectNode.remove("updatedAt");

            logger.debug("Expected JSON: {}", expectedObjectNode.toString());
            logger.debug("Actual JSON: {}", actualObjectNode.toString());

            Assertions.assertEquals(expectedJson, actualJson, "Response did not match API result");
        } catch (Exception e) {
            Assertions.fail("An error occurred while comparing responses: " + e.getMessage());
        }
    }
}
