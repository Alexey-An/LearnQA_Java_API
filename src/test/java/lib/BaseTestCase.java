package lib;

import io.restassured.http.Headers;
import io.restassured.response.Response;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTestCase {

    protected String getResponseHeader(Response response, String header) {
        Headers headers =  response.getHeaders();

        assertTrue(headers.hasHeaderWithName(header), "Отсутствует заголовок: " + header);
        return response.getHeader(header);
    }

    protected String getCookie(Response response, String cookie) {
        Map<String, String> cookies = response.getCookies();

        assertTrue(cookies.containsKey(cookie), "Отсутствует куки: " + cookie);
        return response.getCookie(cookie);
    }
}
