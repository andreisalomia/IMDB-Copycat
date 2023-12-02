// abstract class
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.ArrayList;
import java.time.LocalDateTime;
public abstract class User<T extends Comparable<T>> {
    public static class InformationBuilder {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private char gender;
        private LocalDateTime birthDate;


        public InformationBuilder setCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this;
        }

        public InformationBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public InformationBuilder setCountry(String country) {
            this.country = country;
            return this;
        }

        public InformationBuilder setAge(int age) {
            this.age = age;
            return this;
        }

        public InformationBuilder setGender(char gender) {
            this.gender = gender;
            return this;
        }

        public InformationBuilder setBirthDate(LocalDateTime birthDate) {
            String formattedDate = birthDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            this.birthDate = LocalDateTime.parse(formattedDate);
            return this;
        }
    }
    public class Information {
        private Credentials credentials;
        private String name;
        private String country;
        private int age;
        private char gender;
        private LocalDateTime birthDate;

        private Information(InformationBuilder builder) {
            this.credentials = builder.credentials;
            this.name = builder.name;
            this.country = builder.country;
            this.age = builder.age;
            this.gender = builder.gender;
            this.birthDate = builder.birthDate;
        }
    }

    private Information userInformation;
    private AccountType accountType;
    private String username;
    private int experience;
    private List<String> notifications;
    private SortedSet<T> favourites;

    public Information createInformation(InformationBuilder builder) {
        return new Information(builder);
    }

    public User(Information userInformation, AccountType accountType, String username) {
        this.userInformation = userInformation;
        this.accountType = accountType;
        this.username = username;
        this.experience = 0;
        this.notifications = new ArrayList<>();
        this.favourites = new TreeSet<>();
    }

    public void addToFavourites(T favourite) {
        this.favourites.add(favourite);
    }

    public void removeFromFavourites(T favourite) {
        this.favourites.remove(favourite);
    }

    // update experience of user
    public void updateExperience(int experience){}

    public void logout(){}
}