import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class Week3Tests {

    @ParameterizedTest
    @ValueSource(strings = {"Short string", "Random very long string"})
    public void testStringLength(String testString) {
        assertTrue(testString.length() > 15);
    }

    @Test
    public void testHomeworkCookie() {
        Response response = RestAssured.given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        response.cookies().forEach((k, v) -> System.out.printf("%s=%s", k,v));

        assertTrue(response.cookies().containsKey("HomeWork"));
        assertEquals("hw_value", response.cookies().get("HomeWork"));
    }

}
