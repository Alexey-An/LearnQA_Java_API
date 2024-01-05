package lib;

import java.util.Map;

public class BaseTestCase {

    protected final ApiCoreRequests coreRequests = new ApiCoreRequests();
    protected final String targetURL = "https://playground.learnqa.ru/api/user/";
    protected final String loginURL = "https://playground.learnqa.ru/api/user/login";

    protected void removeEntriesFromUserData(Map<String, String> userData, String... fields) {
        for (String field : fields) {
            userData.remove(field);
        }
    }
}
