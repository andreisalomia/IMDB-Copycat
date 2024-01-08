//package org.example;

import java.util.ArrayList;
import java.util.List;

public abstract class Production implements Comparable<Object>, Subject {
    public String title;
    public List<String> directors;
    public List<String> actors;
    public List<Genre> genres;
    public List<Rating> ratings;
    public String description;
    public double averageRating;
    public List<User> observers = new ArrayList<>();

    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.description = description;
        this.averageRating = calculateAverageRating(ratings);
    }

    public void displayInfo() {}

    public static void removeRating(Production prod, User user) {
        IMDB imdb = IMDB.getInstance();
        boolean checkUser = false;
        for (Rating rating : prod.ratings) {
            if (rating.username.equals(user.username)) {
                prod.ratings.remove(rating);
                prod.averageRating = calculateAverageRating(prod.ratings);
                Parser.writeRatings();
                checkUser = true;
                break;
            }
        }
        if (!checkUser) {
            imdb.userInterface.displayOutput("You can't delete this rating because you didn't rate this production.\n");
        }

    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Production) {
            Production other = (Production) o;
            return this.title.compareTo(other.title);
        }
        return 0;
    }

    public static double calculateAverageRating(List<Rating> ratings) {
        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.rating;
        }
        return sum / ratings.size();
    }

    @Override
    public void registerObserver(User observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(User observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String type) {
            for (User user : observers) {
                if ((user instanceof Contributor || user instanceof Admin) && ((Staff<?>) user).contributions.contains(this)) {
                    user.update("Production " + this.title + " that you added has a new rating from " + ratings.get(ratings.size() - 1).username + ".\n");
                }
            }
            for (User user : observers) {
                if (user instanceof Regular<?> || ((user instanceof Contributor || user instanceof Admin) && !((Staff<?>) user).contributions.contains(this))) {
                    if(!user.username.equals(ratings.get(ratings.size() - 1).username))
                        user.update("Production " + this.title + " that you rated has a new rating from " + ratings.get(ratings.size() - 1).username + ".\n");
                }
            }
    }
}