import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class FirstApiTest {
    @Test
    public void testGetText() {
        Response response = RestAssured.get("https://playground.learnqa.ru/api/get_text");
        response.prettyPrint();
    }

    @Test
    public void testGetJson() {
        JsonPath response = RestAssured.get("https://playground.learnqa.ru/api/get_json_homework").jsonPath();
        System.out.println(response.get("messages[1].message").toString());
    }

    @Test
    public void testLongRedirect() {
        Response response = RestAssured.given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        System.out.println(response.getStatusCode());
        Header location = response.getHeaders()
                .asList()
                .stream()
                .filter(header -> "location".equalsIgnoreCase(header.getName()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("заголовок location не найден!"));
        System.out.printf("%s: %s%n", location.getName(), location.getValue());
    }

    @Test
    public void testLongRedirect2() {
        Response response = RestAssured.given()
                .redirects()
                .follow(false)
                .get("https://playground.learnqa.ru/api/long_redirect")
                .andReturn();
        int redirectsCount = 0;
        while (response.getStatusCode() == 301) {
            Header location = response.getHeaders()
                    .asList()
                    .stream()
                    .filter(header -> "location".equalsIgnoreCase(header.getName()))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException("заголовок location не найден!"));
            response = RestAssured.given()
                    .redirects()
                    .follow(false)
                    .get(location.getValue())
                    .andReturn();
            redirectsCount++;
        }
        System.out.printf("Количество перенаправлений: %d%n", redirectsCount);
    }
}
