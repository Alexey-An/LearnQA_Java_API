package test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static lib.AssertionsLib.*;

@Epic("Тест для эндпоинта https://playground.learnqa.ru/api/user/")
@Feature("Редактирование данных пользователя")
public class UserPutTest extends BaseTestCase {

    @Tag("Negative")
    @Description("Редактируем данные пользователя, не пройдя авторизацию")
    @Story("Редактируем данные пользователя, не пройдя авторизацию")
    @Test
    public void testEditUserWithoutAuthorization() {
        //ARRANGE
        Map<String, String> userTestData = DataGenerator.generateDefaultUser();
        Response createUserResponse = coreRequests.registerNewUser(targetURL, userTestData);
        String userId = createUserResponse.jsonPath().get("id");
        String url = targetURL + userId;
        removeEntriesFromUserData(userTestData, "username", "firstName", "email", "password");
        userTestData.replace("lastName", "Smith");

        //ACT
        Response editUserResponse = coreRequests.editUser(url, userTestData);

        //ASSERT
        assertUserEditNotAllowed(editUserResponse);
    }

    @Tag("Positive")
    @Description("Редактируем данные одного пользователя, пройдя авторизацию другим пользователем")
    @Story("Редактируем данные одного пользователя, пройдя авторизацию другим пользователем")
    @Test
    public void testEditSomeOtherUserAfterAuthorization() {
        //ARRANGE
        Map<String, String> editorUserData = DataGenerator.generateDefaultUser();
        Map<String, String> targetUserData = DataGenerator.generateDefaultUser();
        coreRequests.registerNewUser(targetURL, editorUserData);
        Response createTargetUserResponse = coreRequests.registerNewUser(targetURL, targetUserData);
        targetUserData.replace("firstName", "WE_REPLACE_THIS_FIRSTNAME");
        targetUserData.replace("email", targetUserData.get("email")
                .replaceFirst(".*@", "replaced_email_") + DataGenerator.getRandomEmail()
        );
        removeEntriesFromUserData(targetUserData, "username", "lastName", "password");

        //ACT
        Response authResponse = coreRequests.loginAsUserWithEmail(
                loginURL, editorUserData.get("email"), editorUserData.get("password")
        );
        String url = targetURL + createTargetUserResponse.jsonPath().get("id");
        Response editUserResponse = coreRequests.editUser(url, authResponse.getCookie("auth_sid"),
                authResponse.getHeader("x-csrf-token"), targetUserData
        );

        //ASSERT
        assertUserEditAllowed(editUserResponse);
    }

    @Tag("Negative")
    @Description("После авторизации редактируем свой email, заменив его некорректным, без символа @")
    @Story("После авторизации редактируем свой email, заменив его некорректным, без символа @")
    @Test
    public void testEditUsersEmail() {
        //ARRANGE
        Map<String, String> userData = DataGenerator.generateDefaultUser();
        Response createUserResponse = coreRequests.registerNewUser(targetURL, userData);

        //ACT
        Response authResponse = coreRequests.loginAsUserWithEmail(
                loginURL, userData.get("email"), userData.get("password")
        );
        userData.replace("email",
                userData.get("email").replaceFirst("@", "")
        );
        removeEntriesFromUserData(userData, "username", "firstName", "lastName", "password");
        String url = targetURL + createUserResponse.jsonPath().get("id");
        Response editUserResponse = coreRequests.editUser(url, authResponse.getCookie("auth_sid"),
                authResponse.getHeader("x-csrf-token"), userData
        );

        //ASSERT
        assertInvalidEmailFormat(editUserResponse);
    }

    @Tag("Negative")
    @Description("Выполняем авторизацию и редактируем имя пользователя, заменив его строкой длиною один символ")
    @Story("Выполняем авторизацию и редактируем имя пользователя, заменив его строкой длиною один символ")
    @Test
    public void testEditUsersFirstName() {
        //ARRANGE
        Map<String, String> userData = DataGenerator.generateDefaultUser();
        Response createUserResponse = coreRequests.registerNewUser(targetURL, userData);

        //ACT
        Response authResponse = coreRequests.loginAsUserWithEmail(
                loginURL, userData.get("email"), userData.get("password")
        );
        userData.replace("firstName", "F");

        removeEntriesFromUserData(userData, "username", "email", "lastName", "password");
        String url = targetURL + createUserResponse.jsonPath().get("id");
        Response editUserResponse = coreRequests.editUser(url, authResponse.getCookie("auth_sid"),
                authResponse.getHeader("x-csrf-token"), userData
        );

        //ASSERT
        assertFieldValueTooShort(editUserResponse);
    }

}
