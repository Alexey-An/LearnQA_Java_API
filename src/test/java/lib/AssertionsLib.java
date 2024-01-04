package lib;

import io.restassured.response.Response;
import static org.junit.jupiter.api.Assertions.*;

public class AssertionsLib {

    public static void checkUserGetResponseIsCorrect(Response getUserDataResponse) {
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
}
