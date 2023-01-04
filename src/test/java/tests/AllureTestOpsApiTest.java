package tests;

import api.AuthApi;
import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import models.lombok.TestCaseBody;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import utils.RandomUtils;

import static api.AuthApi.ALLURE_TESTOPS_SESSION;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class AllureTestOpsApiTest {
    public final static String
            USERNAME = "allure8",
            PASSWORD = "allure8",
            TOKEN = "5aab4a32-4692-4bdd-bcf9-f2cc7add8f9a";

    /*
        1. Auth in TesTops with cookie
        2. Create Test Case
        3. Check TestCase name and ID
     */

    @BeforeAll
    static void beforeAll() {
        Configuration.baseUrl= "https://allure.autotests.cloud";
        RestAssured.baseURI = "https://allure.autotests.cloud";
        RestAssured.filters(withCustomTemplates());
    }
    @Test
    void successCreateTestCasesWithApi() {

        AuthApi authApi = new AuthApi();
        String xsrfToken = authApi.getXsrfToken(TOKEN);
        String authCookie = authApi.getAuthorizationCookie(TOKEN, USERNAME, PASSWORD);
        String testcaseName = RandomUtils.testcaseName;
        TestCaseBody testCaseBody = new TestCaseBody();
        testCaseBody.setName(testcaseName);

        int testCaseId = given()
                .when()
                .log().all()
                .header("X-XSRF-TOKEN", xsrfToken)
                .cookies("XSRF-TOKEN", xsrfToken,
                        ALLURE_TESTOPS_SESSION, authCookie)
                .body(testCaseBody)
                .contentType(JSON)
                .queryParam("projectId", "1722")
                .post("/api/rs/testcasetree/leaf")
//                .post("/api/rs/testcasetree/leaf?projectId=1722")
                .then()
                .log().body()
                .statusCode(200)
                .body("name", is(testcaseName))
                .body("automated", is(false))
                .body("external", is(false))
                .extract()
                .path("id");

        open("/favicon.ico");
        getWebDriver().manage().addCookie(new Cookie(ALLURE_TESTOPS_SESSION, authCookie));

        open("/project/1722/test-cases/" + testCaseId);
        $(".TestCaseLayout__name").shouldHave(text(testcaseName));
    }
}
