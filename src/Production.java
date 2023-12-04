//package org.example;
import java.util.List;

public abstract class Production implements Comparable<Object> {
    public String title;
    public List<String> directors;
    public List<String> actors;
    public List<Genre> genres;
    public List<Rating> ratings;
    public String description;
    public double averageRating;

    public Production(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.description = description;
        this.averageRating = calculateAverageRating(ratings);
    }

    public abstract void displayInfo();

    // sort by title alphabetically

    @Override
    public int compareTo(Object o) {
        if(o instanceof Production) {
            Production other = (Production) o;
            return this.title.compareTo(other.title);
        }
        return 0;
    }

    double calculateAverageRating(List<Rating> ratings) {
        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.rating;
        }
        return sum / ratings.size();
    }
}
