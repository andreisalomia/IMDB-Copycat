import java.util.List;
public class Actor implements Comparable<Object>, User.MarketItem {
    String name;
    List<Pair<String, Type>> filmography;
    String biography;
    public Actor(String name, List<Pair<String, Type>> filmography, String biography) {
        this.name = name;
        this.filmography = filmography;
        this.biography = biography;
    }

    @Override
    public String getMarketItemName() {
        return this.name;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Actor) {
            Actor other = (Actor) o;
            return this.name.compareTo(other.name);
        }
        return 0;
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
