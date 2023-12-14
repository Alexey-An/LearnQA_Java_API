import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


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

    @Test
    public void testLongTimeJob() throws InterruptedException {
        JsonPath response = RestAssured.given()
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath();
        String token = response.getString("token");
        long secondsToWait = response.getLong("seconds") * 1000;
        
        String jobStatusNotReady = RestAssured.given()
                .param("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .jsonPath()
                .get("status")
                .toString();
        assertEquals("Job is NOT ready", jobStatusNotReady);

        Thread.sleep(secondsToWait);

        JsonPath jobStatusReady = RestAssured.given()
                .param("token", token)
                .get("https://playground.learnqa.ru/ajax/api/longtime_job")
                .andReturn()
                .jsonPath();
        assertEquals("Job is ready", jobStatusReady.get("status"));
        assertEquals("42", jobStatusReady.get("result"));
    }

    @Test
    public void testCheckAuth() throws IOException {
        List<String> passwords = new ArrayList<>();

        Document document = Jsoup.connect("https://en.wikipedia.org/wiki/List_of_the_most_common_passwords").get();
        Element splashDataTable = document.getElementsByClass("wikitable")
                .stream()
                .filter(element -> element.getElementsByTag("caption")
                        .text()
                        .equalsIgnoreCase("Top 25 most common passwords by year according to SplashData")
                )
                .findAny()
                .orElseThrow(() -> new RuntimeException("Таблица не найдена!!!"));

        List<Element> tableRows = splashDataTable.getElementsByTag("tr")
                .stream()
                .skip(1)
                .collect(Collectors.toList());

        for (Element row : tableRows) {
            passwords.addAll(row.getElementsByTag("td")
                    .stream()
                    .skip(1)
                    .map(Element::text)
                    .collect(Collectors.toList())
            );
        }

        Map<String, String> loginAndPswd = new HashMap<>();
        loginAndPswd.put("login", "super_admin");

        for (String password : passwords) {
            loginAndPswd.put("password", password);
            String authCookie = RestAssured.given()
                    .body(loginAndPswd)
                    .post("https://playground.learnqa.ru/ajax/api/get_secret_password_homework")
                    .andReturn()
                    .getCookie("auth_cookie");

            Response response = RestAssured.given()
                    .body(loginAndPswd)
                    .cookies("auth_cookie", authCookie)
                    .post("https://playground.learnqa.ru/ajax/api/check_auth_cookie")
                    .andReturn();

            if (response.getBody().asPrettyString().contains("<body>You are authorized</body>")) {
                System.out.println(password);
                response.getBody().prettyPrint();
                break;
            }
        }
    }
}
