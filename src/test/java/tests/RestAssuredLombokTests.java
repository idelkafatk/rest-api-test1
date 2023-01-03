package tests;

import helpers.CustomApiListener;
import io.qameta.allure.restassured.AllureRestAssured;
import models.lombok.LoginLombokModel;
import org.junit.jupiter.api.Test;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class RestAssuredLombokTests {
    LoginLombokModel body = new LoginLombokModel();

    @Test
    void successAuthTest() {
        body.setEmail("eve.holt@reqres.in");
        body.setPassword("ityslicka");

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
        body.setPassword("pistol");

        given()
                .filter(withCustomTemplates())
                .log().uri()
                .contentType(JSON)
                .body(body)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

}
