import java.util.ArrayList;
import java.util.List;

public class IMDB {
    private static IMDB instance = null;
    private List<User> users;
    private List<Actor> actors;
    private List<Request> requests;
    private List<Production> productions;

    // Constructor
    private IMDB() {
        this.users = new ArrayList<>();
        this.actors = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.productions = new ArrayList<>();
    }

    public static IMDB getInstance() {
        if (instance == null) {
            instance = new IMDB();
        }
        return instance;
    }

    public void run() {
        loadDataJSON();
        login();
        startFlow();
    }

    private void loadDataJSON() {
        // load data from JSON files
        // initialize lists of users, actors, requests, movies
    }

    private void login() {
        // login user
    }

    private void startFlow() {
        // start flow based on user role
    }

    // Other methods to manage user choices

    public static void main(String[] args) {
        IMDB imdb = IMDB.getInstance();
        imdb.run();
    }

    public static class RequestsHolder {
        private static List<String> requests = new ArrayList<>();

        public static void addRequest(String request) {
            requests.add(request);
        }

        public static void removeRequest(String request) {
            requests.remove(request);
        }

        public static List<String> getRequests() {
            return new ArrayList<>(requests);
        }
    }


}

