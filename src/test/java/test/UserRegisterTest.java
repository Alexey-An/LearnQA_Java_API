package test;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import lib.BaseTestCase;
import lib.DataGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

@Epic("Создание нового пользователя")
@Feature("Создание нового пользователя - негативные проверки")
public class UserRegisterTest extends BaseTestCase {

    @Tag("Negative")
    @Description("Создание пользователя с некорректным email - без символа @")
    @Test
    public void createUserWithIncorrectEmail() {
        //ARRANGE
        Map<String, String> requestBody = DataGenerator.generateUserDataWithIncorrectEmail("wrong_email.com");

        //ACT
        Response response = coreRequests.makePostRequest(targetURL, requestBody);

        //ASSERT
        Assertions.assertEquals(400, response.getStatusCode(), "Должен быть код 400");
        Assertions.assertTrue(response.getBody().asPrettyString().contains(
                "Invalid email format"), "Неверный текст ответа");
    }

    @Tag("Negative")
    @Description("Создание пользователя без указания одного из полей")
    @ParameterizedTest
    @CsvSource({"username", "firstName", "lastName", "email", "password"})
    public void createUserWithOneFieldMissing(String missingField) {
        //ARRANGE
        Map<String, String> requestBody = DataGenerator.generateUserDataOneFieldMissing(missingField);

        //ACT
        Response response = coreRequests.makePostRequest(targetURL, requestBody);

        //ASSERT
        Assertions.assertEquals(400, response.getStatusCode(), "Должен быть код 400");
        Assertions.assertTrue(response.getBody().asPrettyString().contains(
                String.format("The following required params are missed: %s", missingField)), "Неверный текст ответа");
    }

    @Tag("Negative")
    @Description("Создание пользователя с очень коротким именем в один символ")
    @Test
    public void createUserWithSingleCharacterName() {
        //ARRANGE
        String veryShortUserName = "q";
        Map<String, String> requestBody = DataGenerator.generateUserDataWithUserName(veryShortUserName);

        //ACT
        Response response = coreRequests.makePostRequest(targetURL, requestBody);

        //ASSERT
        Assertions.assertEquals(400, response.getStatusCode(), "Должен быть код 400");
        Assertions.assertTrue(response.getBody().asPrettyString().contains(
                "The value of 'username' field is too short"), "Неверный текст ответа");
    }

    @Tag("Negative")
    @Description("Создание пользователя с очень длинным именем - длиннее 250 символов")
    @Test
    public void createUserWithVeryLongName() {
        //ARRANGE
        String veryLongUserName = "very_long_userName_very_long_userName_very_long_userName_very_long_userName_very_long_userName_very_long_userName_very_long_userName_very_long_userName_very_long_userName_very_long_userName_very_long_userName_very_long_userName_very_long_userName_very_long_userName";
        Map<String, String> requestBody = DataGenerator.generateUserDataWithUserName(veryLongUserName);

        //ACT
        Response response = coreRequests.makePostRequest(targetURL, requestBody);

        //ASSERT
        Assertions.assertEquals(400, response.getStatusCode(), "Должен быть код 400");
        Assertions.assertTrue(response.getBody().asPrettyString().contains(
                "The value of 'username' field is too long"), "Неверный текст ответа");
    }

}
