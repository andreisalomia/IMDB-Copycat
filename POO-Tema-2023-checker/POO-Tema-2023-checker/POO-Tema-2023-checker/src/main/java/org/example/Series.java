import java.util.Map;
import java.util.List;

public class Series extends Production{
    int releaseYear;
    int numberOfSeasons;
    Map<String, List<Episode>> episodes;

    public Series(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description, int releaseYear, int numberOfSeasons, Map<String, List<Episode>> episodes) {
        super(title, directors, actors, genres, ratings, description);
        this.releaseYear = releaseYear;
        this.numberOfSeasons = numberOfSeasons;
        this.episodes = episodes;
    }

    @Override
    public void displayInfo() {
        System.out.println("Title: " + title);
        if (directors != null) System.out.println("Directors: " + directors);
        if (actors != null) System.out.println("Actors: " + actors);
        if (genres != null) System.out.println("Genres: " + genres);
        if (ratings != null) System.out.println("Ratings: " + ratings);
        if (description != null) System.out.println("Description: " + description);
        if (releaseYear != 0) System.out.println("Release Year: " + releaseYear);
        if (numberOfSeasons != 0) System.out.println("Number of Seasons: " + numberOfSeasons);
        if (averageRating != 0) System.out.println("Average Rating: " + averageRating);
    }

}
