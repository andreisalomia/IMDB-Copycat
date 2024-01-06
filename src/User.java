//package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public abstract class User<T extends Comparable<T>> implements Observer {
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

        @Override
        public String toString() {
            return String.format("Information{name='%s', country='%s', age=%d, gender=%c, birthDate=%s}",
                    name, country, age, gender, birthDate.format(DateTimeFormatter.ISO_DATE));
        }

        public Credentials getCredentials() {
            return credentials;
        }

        public static class InformationBuilder {
            private Credentials credentials;
            private String name;
            private String country;
            private int age;
            private char gender;
            private LocalDate birthDate;

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

            public InformationBuilder setBirthDate(LocalDate birthDate) {
                this.birthDate = birthDate;
                return this;
            }

            public Information build() {
                return new Information(credentials, name, country, age, gender, birthDate);
            }
        }
    }

    public Information userInformation;
    public AccountType accountType;
    public String username;
    public Integer experience;
    public List<String> notifications;
    public SortedSet<Comparable> favorites;

    public User(Information information, AccountType accountType, String username, Integer experience) {
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
        Parser.insertFavProduction(this, production);
    }

    public void addActorToFavorites(Comparable actor) {
        favorites.add(actor);
        Parser.insertFavActor(this, actor);
    }

    //remove from favourites
    public void removeActorFromFavourites(Actor item) {
        favorites.remove(item);
        Parser.insertFavActor(this, item);
    }

    public void removeProductionFromFavourites(Production item) {
        favorites.remove(item);
        Parser.insertFavProduction(this, item);
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
    public void updateExperience(int experience) {
        if(this.accountType == AccountType.Admin) {
            return;
        }
        this.experience += experience;
        Parser.updateExperience(this);
        Parser.updateLists();
    }

    public void logout() {
        IMDB imdb = IMDB.getInstance();
        int option = Options.startOptions();
        if (option == 1) {
            imdb.restartApp();
        } else {
            System.exit(0);
        }
    }

    public List<String> getFavoriteActors() {
        List<String> favoriteActors = new ArrayList<>();
        for (Comparable item : favorites) {
            if (item instanceof Actor) {
                favoriteActors.add(((Actor) item).name);
            }
        }
        return favoriteActors;
    }

    public List<String> getFavoriteProductions() {
        List<String> favoriteProductions = new ArrayList<>();
        for (Comparable item : favorites) {
            if (item instanceof Production) {
                favoriteProductions.add(((Production) item).title);
            }
        }
        return favoriteProductions;
    }


    @Override
    public void update(String message) {
//        only add if the notification doesent already exist
        if(this.notifications.contains(message)) {
            return;
        }
        this.notifications.add(message);
        Parser.updateNotifications(this);
        Parser.updateLists();
    }
}
