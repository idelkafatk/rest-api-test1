package tests;

import models.pojo.LoginTestModel;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class RestAssuredPojoTests {
    LoginTestModel body = new LoginTestModel();

    @Test
    void successAuthTest() {
        body.setEmail("eve.holt@reqres.in");
        body.setPassword("ityslicka");

        given()
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
