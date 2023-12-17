import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class Week3Tests {

    @Test
    public void testStringLength() {
        String longString = "Случайная строка длиннее 15 символов";
        String veryShort = "VERY_SHORT";
        String shortString = "Короткая строка";
        assertTrue(longString.length() > 15);
        assertFalse(veryShort.length() > 15);
        assertFalse(shortString.length() > 15);
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
