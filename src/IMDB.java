import java.time.LocalDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;

import org.json.simple.*;
import org.json.simple.parser.*;

import java.io.*;

public class IMDB {
    private static IMDB instance = null;
    List<User> users;
    List<Actor> actors;
    List<Request> requests;
    List<Production> productions;

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
//        TODO: check if the lists have been correctly loaded
        for (User user : users) {
            System.out.println("User: " + user.username);

            // Print Credentials
            Credentials credentials = user.userInformation.getCredentials();
            if (credentials != null) {
                System.out.println("Credentials: " + credentials.toString());
            } else {
                System.out.println("No credentials");
            }

            System.out.println();  // Add a newline between users
        }

//        login();
//        startFlow();
    }

    public void loadDataJSON() {
        actors = Parser.parseActors("src/actors.json");
        productions = Parser.parseProductions("src/production.json");
        requests = Parser.parseRequests("src/requests.json");
        users = Parser.parseUsers("src/accounts.json");
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

