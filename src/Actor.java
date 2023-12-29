
import java.util.List;
public class Actor implements Comparable<Object> {
    public String name;
    public List<Pair<String, Type>> filmography;
    public String biography;
    public Actor(String name, List<Pair<String, Type>> filmography, String biography) {
        this.name = name;
        this.filmography = filmography;
        this.biography = biography;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof Actor) {
            Actor other = (Actor) o;
            return this.name.compareTo(other.name);
        }
        return 0;
    }

    public void displayInfo() {
        StringBuilder builder = new StringBuilder();
        builder.append("Actor: ").append(name).append("\n");

        if (biography != null)
            builder.append("Biography: ").append(biography).append("\n");

        if (filmography != null && !filmography.isEmpty()) {
            builder.append("Filmography:\n");
            for (Pair<String, Type> entry : filmography) {
                builder.append("\t").append(entry.name).append(" (").append(entry.type).append(")\n");
            }
        }

        builder.append("----------------------------\n"); // Separator for better readability
        UserInterface userInterface = IMDB.getInstance().userInterface;
        userInterface.displayOutput(builder.toString());
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
