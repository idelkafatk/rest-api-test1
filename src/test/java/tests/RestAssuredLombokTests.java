package tests;

import models.lombok.LoginLombokModel;
import models.lombok.LoginLombokResponseModel;
import models.pojo.LoginTestResponseModel;
import org.junit.jupiter.api.Test;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static specs.LoginRequestSpecs.loginRequestSpec;
import static specs.LoginResponseSpecs.loginResponseSpec;

public class RestAssuredLombokTests {
    LoginLombokModel body = new LoginLombokModel();

    @Test
    void successAuthTest() {
        body.setEmail("eve.holt@reqres.in");
        body.setPassword("ityslicka");

        given()
                .body(body)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void unSuccessAuthTest() {
        body.setEmail("eve.holt@reqres.in");

        given()
                .filter(withCustomTemplates())
                .log().uri()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void userNotFoundTest() {

        given()
                .filter(withCustomTemplates())
                .log().uri()
                .when()
                .get("https://reqres.in//api/users/23")
                .then()
                .log().status()
                .statusCode(404);
    }

    @Test
    void getListOfUsersTest() {

        given()
                .filter(withCustomTemplates())
                .log().uri()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().status()
                .statusCode(200);
    }

    @Test
    void successRegisterTest() {
        body.setEmail("eve.holt@reqres.in");
        body.setPassword("ityslicka");

        LoginTestResponseModel response = given()
                .spec(loginRequestSpec)
                .body(body)
                .when()
                .post()
                .then()
                .spec(loginResponseSpec)
                .extract().as(LoginTestResponseModel.class);
        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }

    @Test
    void successAuthWithSpecsTest() {
        body.setEmail("eve.holt@reqres.in");
        body.setPassword("ityslicka");

        LoginLombokResponseModel response = given()
                .spec(loginRequestSpec)
                .body(body)
                .when()
                .post()
                .then()
                .spec(loginResponseSpec)
                .extract().as(LoginLombokResponseModel.class);

        assertThat(response.getToken()).isEqualTo("QpwL5tke4Pnpja7X4");
    }
}
