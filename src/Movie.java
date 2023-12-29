//package org.example;
import java.util.List;
public class Movie extends Production {
    public int duration;
    public int releaseYear;

    public Movie(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description, int duration, int releaseYear) {
        super(title, directors, actors, genres, ratings, description);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    @Override
    public void displayInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("Title: ").append(title).append("\n");

        builder.append("Type: Movie\n");
        if (directors != null) builder.append("Directors: ").append(directors).append("\n");
        if (actors != null) builder.append("Actors: ").append(actors).append("\n");
        if (genres != null) builder.append("Genres: ").append(genres).append("\n");
        if (ratings != null) builder.append("Ratings: ").append(ratings).append("\n");
        if (description != null) builder.append("Description: ").append(description).append("\n");
        if (duration != 0) builder.append("Duration: ").append(duration).append("\n");
        if (releaseYear != 0) builder.append("Release Year: ").append(releaseYear).append("\n");
        if (averageRating != 0) builder.append("Average Rating: ").append(averageRating).append("\n");

        IMDB.getInstance().userInterface.displayOutput(builder.toString());
    }

}
