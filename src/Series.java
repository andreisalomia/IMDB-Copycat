//package org.example;

import java.util.Map;
import java.util.List;

public class Series extends Production {
    public int releaseYear;
    public int numberOfSeasons;
    public Map<String, List<Episode>> episodes;

    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description, int releaseYear, int numberOfSeasons, Map<String, List<Episode>> episodes) {
        super(title, directors, actors, genres, ratings, description);
        this.releaseYear = releaseYear;
        this.numberOfSeasons = numberOfSeasons;
        this.episodes = episodes;
    }

    @Override
    public void displayInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("Title: ").append(title).append("\n");
        builder.append("Type: Series\n");

        if (directors != null)
            builder.append("Directors: ").append(directors).append("\n");

        if (actors != null)
            builder.append("Actors: ").append(actors).append("\n");

        if (genres != null)
            builder.append("Genres: ").append(genres).append("\n");

        if (ratings != null)
            builder.append("Ratings: ").append(ratings).append("\n");

        if (description != null)
            builder.append("Description: ").append(description).append("\n");

        if (releaseYear != 0)
            builder.append("Release Year: ").append(releaseYear).append("\n");

        if (numberOfSeasons != 0)
            builder.append("Number of Seasons: ").append(numberOfSeasons).append("\n");

        if (averageRating != 0)
            builder.append(String.format("Average Rating: %.2f\n", averageRating));

        builder.append("----------------------------\n"); // Separator for better readability
        System.out.println(builder.toString());
    }

}
