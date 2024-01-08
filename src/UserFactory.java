//package org.example;
import java.time.LocalDate;

public class UserFactory {

    public static User<?> createUser(AccountType type, String username, Credentials credentials, String name, String country, int age, char gender, LocalDate birthDate, Integer experience) {
        User.Information.InformationBuilder informationBuilder = new User.Information.InformationBuilder()
                .setCredentials(credentials)
                .setName(name)
                .setCountry(country)
                .setAge(age)
                .setGender(gender)
                .setBirthDate(birthDate);

        User.Information userInformation;

        try {
            userInformation = informationBuilder.build();
        } catch (InformationIncompleteException e) {
            e.printStackTrace();
            return null;
        }

        switch (type) {
            case Regular:
                return new Regular<>(userInformation, AccountType.Regular, username, experience);
            case Contributor:
                return new Contributor<>(userInformation, AccountType.Contributor, username, experience);
            case Admin:
                return new Admin<>(userInformation, AccountType.Admin, username, experience);
            default:
                throw new IllegalArgumentException("InvalidUserException");
        }
    }
}