package test;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.AssertionsLib;
import lib.BaseTestCase;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class UserGetTest extends BaseTestCase {

    private final String userId2TargetURL = "https://playground.learnqa.ru/api/user/2";
    private final String userId88721Email = "a.anosov@gmail.com";
    private final String userId88721Password = "125125521";

    @Tag("Negative")
    @Description("Авторизовывается одним пользователем, но получить данные пользователя с другим ID")
    @Test
    public void testGetUserData() {
        //ARRANGE
        Response authResponse = coreRequests.loginAsUserWithEmail(loginURL, userId88721Email, userId88721Password);
        String authCookie = authResponse.getCookie("auth_sid");
        String xCsrfToken = authResponse.getHeader("x-csrf-token");

        //ACT
        Response getUserDataResponse = coreRequests.makeGetRequest(userId2TargetURL, authCookie, xCsrfToken);

        //ASSERT
        AssertionsLib.assertUserGetResponseIsCorrect(getUserDataResponse);
    }

}
