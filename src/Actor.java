import java.util.List;
public class Actor {
    String name;
    List<Pair<String, Type>> filmography;
    String biography;
    public Actor(String name, List<Pair<String, Type>> filmography, String biography) {
        this.name = name;
        this.filmography = filmography;
        this.biography = biography;
    }
    public static class Pair<T, U> {
        T name;
        U type;
        public Pair(T name, U type) {
            this.name = name;
            this.type = type;
        }
    }
    public enum Type {
        MOVIE,
        SERIES
    }
}
