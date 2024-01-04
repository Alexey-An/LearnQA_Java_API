package lib;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class ApiCoreRequests {

    @Step("Выполняем POST-запрос")
    public Response makePostRequest(String url, Map<String, String> requestBody) {
        return RestAssured.given()
                .body(requestBody)
                .post(url)
                .andReturn();
    }

    @Step("Выполняем авторизацию")
    public Response loginAsUserWithEmail(String url, String email, String password) {
        Map<String, String> authData = new HashMap<>();
        authData.put("email", email);
        authData.put(password, password);
        return RestAssured.given()
                .body(authData)
                .post(url)
                .andReturn();
    }

    @Step("Выполняем GET-запрос после авторизации")
    public Response makeGetRequest(String url, String cookie, String xCsrfToken) {
        return RestAssured.given()
                .cookie("auth_sid", cookie)
                .header(new Header("x-csrf-token", xCsrfToken))
                .get(url)
                .andReturn();
    }

}
