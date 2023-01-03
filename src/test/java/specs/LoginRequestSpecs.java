package specs;

import io.restassured.specification.RequestSpecification;

import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.http.ContentType.JSON;

public class LoginRequestSpecs {
    public static RequestSpecification loginRequestSpec = with()
            .filter(withCustomTemplates())
            .baseUri("https://reqres.in")
            .basePath("/api/login")
            .log().uri()
            .contentType(JSON);
}
