package test;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import lib.AssertionsLib;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;


public class UserDeleteTest extends BaseTestCase {

    @Tag("Negative")
    @Description("Удаляем пользователя, id которого 2")
    @Test
    public void testDeleteUserId2() {
        //ARRANGE
        String email = "vinkotov@example.com";
        String password = "1234";
        String userId = "2";

        //ACT
        Response authResponse = coreRequests.loginAsUserWithEmail(loginURL, email, password);
        Response deleteUserResponse = coreRequests.deleteUser(targetURL, authResponse.getCookie("auth_sid"),
                authResponse.getHeader("x-csrf-token"),  userId
        );

        //ASSERT
        AssertionsLib.assertDeleteNotAllowed(deleteUserResponse);
    }

    @Tag("Positive")
    @Description("Удаляем пользователя")
    @Test
    public void testDeleteUser() {
        //ARRANGE
        Map<String, String> userTestData = DataGenerator.generateDefaultUser();
        Response createUserResponse = coreRequests.registerNewUser(targetURL, userTestData);
        String userId = createUserResponse.jsonPath().get("id");

        //ACT
        Response authResponse = coreRequests.loginAsUserWithEmail(loginURL,
                userTestData.get("email"), userTestData.get("password")
        );
        Response deleteUserResponse = coreRequests.deleteUser(targetURL, authResponse.getCookie("auth_sid"),
                authResponse.getHeader("x-csrf-token"),  userId
        );
        Response requestDeletedUserResponse = coreRequests.getUser(targetURL, userId);


        //ASSERT
        Assertions.assertEquals(200, deleteUserResponse.getStatusCode(),
                String.format("Ошибка при удалении пользователя %s", userId));
        AssertionsLib.assertDeleteUserSuccess(requestDeletedUserResponse);
    }

    @Tag("Negative")
    @Description("Удаляем другого пользователя, не себя, после авторизации")
    @Test
    public void testDeleteSomeOtherUser() {
        //ARRANGE
        Map<String, String> editorUserData = DataGenerator.generateDefaultUser();
        Map<String, String> targetUserData = DataGenerator.generateDefaultUser();
        Response createEditorUserResponse = coreRequests.registerNewUser(targetURL, editorUserData);
        Response createTargetUserResponse = coreRequests.registerNewUser(targetURL, targetUserData);
        String targetUserId = createTargetUserResponse.jsonPath().get("id");

        //ACT
        Response authResponse = coreRequests.loginAsUserWithEmail(
                loginURL, editorUserData.get("email"), editorUserData.get("password")
        );
        Response deleteUserResponse = coreRequests.deleteUser(targetURL, authResponse.getCookie("auth_sid"),
                authResponse.getHeader("x-csrf-token"),  targetUserId
        );
        Response requestDeletedUserResponse = coreRequests.getUser(targetURL, targetUserId);

        //ASSERT
        Assertions.assertEquals(targetUserData.get("username"), requestDeletedUserResponse.jsonPath().get("username"),
                "Пользователь не должен был быть удален!");
        Assertions.assertTrue(requestDeletedUserResponse.getBody().asPrettyString().contains("username"),
                "Пользователь не должен был быть удален!");
    }

}
