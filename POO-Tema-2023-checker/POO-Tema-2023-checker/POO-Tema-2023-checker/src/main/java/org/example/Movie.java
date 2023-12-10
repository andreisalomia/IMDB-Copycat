import java.util.List;
public class Movie extends Production {
    int duration;
    int releaseYear;

    public Movie(String title, List<String> directors, List<String> actors, List<Genre> genres, List<Rating> ratings, String description, int duration, int releaseYear) {
        super(title, directors, actors, genres, ratings, description);
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    @Override
    public void displayInfo() {
        System.out.println("Title: " + title);
        if (directors != null) System.out.println("Directors: " + directors);
        if (actors != null) System.out.println("Actors: " + actors);
        if (genres != null) System.out.println("Genres: " + genres);
        if (ratings != null) System.out.println("Ratings: " + ratings);
        if (description != null) System.out.println("Description: " + description);
        if (duration != 0) System.out.println("Duration: " + duration);
        if (releaseYear != 0) System.out.println("Release Year: " + releaseYear);
        if (averageRating != 0) System.out.println("Average Rating: " + averageRating);
    }

}
