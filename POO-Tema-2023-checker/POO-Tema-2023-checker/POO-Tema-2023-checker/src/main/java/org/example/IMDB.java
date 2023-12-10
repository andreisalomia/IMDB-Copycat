import java.util.*;
public class IMDB {
    private static IMDB instance = null;
    private UserInterface userInterface;
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
        login();
//        startFlow();
    }

    public void loadDataJSON() {
        actors = Parser.parseActors("src/main/resources/input/actors.json");
        productions = Parser.parseProductions("src/main/resources/input/production.json");
        requests = Parser.parseRequests("src/main/resources/input/requests.json");
        users = Parser.parseUsers("src/main/resources/input/accounts.json");
    }

    private void chooseInterface() {
        System.out.println("Choose an interface: ");
        System.out.println("1. Terminal");
        System.out.println("2. GUI");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                userInterface = new TerminalUI();
                System.out.println("Terminal interface selected");
                break;
            case 2:
                userInterface = new GUI();
                System.out.println("GUI interface selected");
                break;
            default:
                System.out.println("Invalid choice");
                chooseInterface();
        }
    }


    private void login() {
        chooseInterface();
        userInterface.displayOutput("Welcome back to IMDB! Please enter your email and password to login.\n");
        String email = ((TerminalUI) userInterface).getEmail();
        String password = ((TerminalUI) userInterface).getPassword();

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
        private static List<Request> requests = new ArrayList<>();

        public static void addRequest(Request request) {
            requests.add(request);
        }

        public static void removeRequest(Request request) {
            requests.remove(request);
        }

        public static List<Request> getRequests() {
            return new ArrayList<>(requests);
        }
    }


}

