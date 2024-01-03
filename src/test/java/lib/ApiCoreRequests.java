package lib;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.Map;

public class ApiCoreRequests {

    @Step("Выполняем POST-запрос")
    public Response makePostRequest(String url, Map<String, String> requestBody) {
        return RestAssured.given()
                .body(requestBody)
                .post(url)
                .andReturn();
    }

}