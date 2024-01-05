package lib;

import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionsLib {

    public static void assertUserGetResponseIsCorrect(Response getUserDataResponse) {
        assertNotEquals(404, getUserDataResponse.getStatusCode(), "Пользователь не найден!");
        assertEquals(200, getUserDataResponse.getStatusCode(), "Ошибка выполнения запроса");
        assertFalse(getUserDataResponse.getBody().asPrettyString().contains("email"),
                "Ответ не должен содержать email пользователя");
        assertFalse(getUserDataResponse.getBody().asPrettyString().contains("firstName"),
                "Ответ не должен содержать имя пользователя");
        assertFalse(getUserDataResponse.getBody().asPrettyString().contains("lastName"),
                "Ответ не должен содержать фамилю пользователя");
        assertTrue(getUserDataResponse.getBody().asPrettyString().contains("username"),
                "Ответ не содержить логин пользователя!");
        assertFalse(getUserDataResponse.getBody().asPrettyString().contains("id"),
                "Ответ не должен содержать id пользователя");
    }

    public static void assertUserCreatedSuccessfully(Response createUserResponse) {
        Assertions.assertEquals(200, createUserResponse.getStatusCode(),
                "Ошибка при создании пользователя!");
        Assertions.assertNotNull(createUserResponse.jsonPath().get("id"),
                "Ошибка при создании пользователя!");
    }

    public static void assertUserEditNotAllowed(Response editUserResponse) {
        Assertions.assertEquals(400, editUserResponse.getStatusCode(),
                "Код ответа должен быть 400");
        Assertions.assertTrue(
                editUserResponse.getBody().asPrettyString().contains("Auth token not supplied"),
                "Сервер предоставил некорректный ответ"
        );
    }

    public static void assertUserEditAllowed(Response editUserResponse) {
        Assertions.assertEquals(200, editUserResponse.getStatusCode(),
                "Код ответа должен быть 200");
    }

    public static void assertFieldValueTooShort(Response editUserResponse) {
        Assertions.assertEquals(400, editUserResponse.getStatusCode(),
                "Код ответа должен быть 400");
        Assertions.assertTrue(
                editUserResponse.getBody().asPrettyString().contains("\"error\": \"Too short value for field"),
                "Сервер предоставил некорректный ответ"
        );
    }

    public static void assertInvalidEmailFormat(Response editUserResponse) {
        Assertions.assertEquals(400, editUserResponse.getStatusCode(),
                "Код ответа должен быть 400");
        Assertions.assertTrue(
                editUserResponse.getBody().asPrettyString().contains("Invalid email format"),
                "Сервер предоставил некорректный ответ"
        );
    }



}
