package lib;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DataGenerator {

    public static String getRandomEmail() {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return timeStamp + "@mail.com";
    }

    public static Map<String, String> generateDefaultUser() {
        User defaultUser = new User.UserBuilder()
                .build();
        return getUserData(defaultUser);
    }

    public static Map<String, String> generateUserDataWithIncorrectEmail(String incorrectEmail) {
        User defaultUser = new User.UserBuilder()
                .email(incorrectEmail)
                .build();
        return getUserData(defaultUser);
    }

    public static Map<String, String> generateUserDataOneFieldMissing(String missingField) {
        User defaultUser = new User.UserBuilder()
                .build();
        Map<String, String> userData = getUserData(defaultUser);
        userData.remove(missingField);
        return userData;
    }

    public static Map<String, String> generateUserDataWithUserName(String userName) {
        User defaultUser = new User.UserBuilder()
                .userName(userName)
                .build();
        return getUserData(defaultUser);
    }

    private static Map<String, String> getUserData(User defaultUser) {
        Map<String, String> userData = new HashMap<>();
        userData.put("username", defaultUser.getUserName());
        userData.put("firstName", defaultUser.getFirstName());
        userData.put("lastName", defaultUser.getLastName());
        userData.put("email", defaultUser.getEmail());
        userData.put("password", defaultUser.getPassword());
        return userData;
    }


}
