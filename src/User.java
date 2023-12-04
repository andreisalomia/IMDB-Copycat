//package org.example;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public abstract class User<T extends Comparable<T>> {


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

    public Information userInformation;
    public AccountType accountType;
    public String username;
    public int experience;
    public List<String> notifications;
    public SortedSet<Comparable> favorites;

    public User(Information information, AccountType accountType, String username, int experience) {
        this.userInformation = information;
        this.accountType = accountType;
        this.username = username;
        this.experience = experience;
        this.notifications = new ArrayList<>();
        this.favorites = new TreeSet<>(new NameComparator());
    }

    //add to favourites
    public void addProductionToFavorites(Comparable production) {
        favorites.add(production);
    }

    public void addActorToFavorites(Comparable actor) {
        favorites.add(actor);
    }

    //remove from favourites
    public void removeFromFavourites(T item) {
        favorites.remove(item);
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
        if (!favorites.isEmpty()) {
            String favoritesString = favorites.stream()
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            joiner.add("favourites=" + favoritesString);
        } else {
            joiner.add("favourites=No Favorites");
        }

        return joiner.toString();
    }

    private class NameComparator implements Comparator<Comparable> {
        @Override
        public int compare(Comparable o1, Comparable o2) {
            String name1 = getName(o1);
            String name2 = getName(o2);
            return name1.compareTo(name2);
        }

        private String getName(Comparable obj) {
            if (obj instanceof Actor) {
                return ((Actor) obj).name;
            } else if (obj instanceof Production) {
                return ((Production) obj).title;
            }
            throw new IllegalArgumentException("Object is not an Actor or Production");
        }
    }

    // update experience of user
    public void updateExperience(int experience) {}

    public void logout() {}
}
