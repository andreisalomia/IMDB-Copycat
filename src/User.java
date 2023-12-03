import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public abstract class User<T extends Comparable<T>> {

    public interface MarketItem {
        String getMarketItemName();
    }

    public static class Information {
        private Credentials credentials;
        String name;
        String country;
        int age;
        char gender;
        LocalDate birthDate;

        public Information(Credentials credentials, String name, String country, int age, char gender, LocalDate birthDate) {
            this.credentials = credentials;
            this.name = name;
            this.country = country;
            this.age = age;
            this.gender = gender;
            this.birthDate = birthDate;
        }

        public Credentials getCredentials() {
            return this.credentials;
        }

        public Credentials setCredentials(Credentials credentials) {
            this.credentials = credentials;
            return this.credentials;
        }

            @Override
            public String toString() {
                return String.format("Information{name='%s', country='%s', age=%d, gender=%c, birthDate=%s}",
                        name, country, age, gender, birthDate.format(DateTimeFormatter.ISO_DATE));
            }


    }

    Information userInformation;
    AccountType accountType;
    String username;
    int experience;
    List<String> notifications;
    SortedSet<MarketItem> favourites;

    public User(Information information, AccountType accountType, String username, int experience) {
        this.userInformation = information;
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        this.notifications = new ArrayList<>();
        this.favourites = new TreeSet<>(new NameComparator());
    }

    //add to favourites
    public void addProductionToFavourites(MarketItem production) {
        favourites.add(production);
    }

    public void addActorToFavourites(MarketItem actor) {
        favourites.add(actor);
    }

    //remove from favourites
    public void removeFromFavourites(T item) {
        favourites.remove(item);
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "User{", "}");

        // Append user information
        joiner.add("userInformation=" + (userInformation != null ? userInformation.toString() : "No Information"));

        // Append account type
        joiner.add("accountType=" + accountType);

        // Append username
        joiner.add("username='" + username + "'");

        // Append experience
        joiner.add("experience=" + experience);

        // Append notifications
        joiner.add("notifications=" + notifications);

        // Append favorites
        if (!favourites.isEmpty()) {
            String favoritesString = favourites.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            joiner.add("favourites=" + favoritesString);
        } else {
            joiner.add("favourites=No Favorites");
        }

        return joiner.toString();
    }

    private class NameComparator implements Comparator<MarketItem> {
        @Override
        public int compare(MarketItem o1, MarketItem o2) {
            String name1 = o1.getMarketItemName();
            String name2 = o2.getMarketItemName();
            return name1.compareTo(name2);
        }
    }

    // update experience of user
    public void updateExperience(int experience) {}

    public void logout() {}
}
