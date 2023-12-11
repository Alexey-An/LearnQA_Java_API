import io.restassured.RestAssured;
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
}
