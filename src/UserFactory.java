import java.time.LocalDate;

public class UserFactory {

    public static User<?> createUser(AccountType type, String username, Credentials credentials, String name, String country, int age, char gender, LocalDate birthDate, int experience) {
        User.Information userInformation = new User.Information(credentials, name, country, age, gender, birthDate);

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