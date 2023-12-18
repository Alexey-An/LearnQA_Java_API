import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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
                .get("https://playground.learnqa.ru/api/homework_cookie")
                .andReturn();
        response.cookies().forEach((k, v) -> System.out.printf("%s=%s", k,v));

        assertTrue(response.cookies().containsKey("HomeWork"));
        assertEquals("hw_value", response.cookies().get("HomeWork"));
    }

    @Test
    public void testHomeworkHeader() {
        Response response = RestAssured.given()
                .get("https://playground.learnqa.ru/api/homework_header")
                .andReturn();
        response.headers().forEach(System.out::println);

        assertTrue(response.headers().hasHeaderWithName("x-secret-homework-header"));
        assertEquals("Some secret value", response.headers().getValue("x-secret-homework-header"));
    }

    @ParameterizedTest
    @CsvSource({
            "'Mozilla/5.0 (Linux; U; Android 4.0.2; en-us; Galaxy Nexus Build/ICL53F) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30', Mobile, No, Android",
            "'Mozilla/5.0 (iPad; CPU OS 13_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) CriOS/91.0.4472.77 Mobile/15E148 Safari/604.1', Mobile, Chrome, iOS",
            "'Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)', Googlebot, Unknown, Unknown",
            "'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.77 Safari/537.36 Edg/91.0.100.0', Web, Chrome, No",
            "'Mozilla/5.0 (iPad; CPU iPhone OS 13_2_3 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.0.3 Mobile/15E148 Safari/604.1', Mobile, No, iPhone"
    })
    public void testUserAgentCheck(String agent, String platform, String browser, String device) {
        Response response = RestAssured.given()
                .header(new Header("User-Agent", agent))
                .get("https://playground.learnqa.ru/ajax/api/user_agent_check")
                .andReturn();

        assertEquals(platform, response.jsonPath().get("platform"));
        assertEquals(browser, response.jsonPath().get("browser"));
        assertEquals(device, response.jsonPath().get("device"));
    }

}
