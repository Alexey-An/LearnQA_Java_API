package lib;

import java.util.Random;

public class User {
    private final String userName;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

    public static class UserBuilder {

        private final Random random = new Random();
        private String firstName = random.nextInt(2) == 0 ? "Jane" : "John";
        private String userName = firstName;
        private String lastName = "Doe" + random.nextInt(100);
        private String email = firstName + "_" + lastName + "@mail.com";
        private String password = "1234554321";

        public User build() {
            return new User(this);
        }

        public UserBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public UserBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }
    }


    private User(UserBuilder builder) {
        userName = builder.userName;
        firstName = builder.firstName;
        lastName = builder.lastName;
        email = builder.email;
        password = builder.password;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}