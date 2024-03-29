//package org.example;
import javax.swing.*;
import java.time.LocalDate;
import java.util.*;

public class IMDB {
    private static IMDB instance = null;
    public User currentUser;
    public UserInterface userInterface;
    public List<User> users;
    public List<Actor> actors;
    public List<Request> requests;
    public List<Production> productions;

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
        universalAdmin();
        chooseInterface();
        loadObservers();
        if (userInterface instanceof GUI) {
            login_GUI();
        }
        else {
            login();
            System.out.println("Logged in as " + currentUser.userInformation.name);
            startFlow(currentUser);
        }
    }

    public void login_GUI() {
        LoginGUI loginGUI = new LoginGUI(users);

        loginGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        loginGUI.setVisible(true);
    }

    private static void loadObservers() {
        IMDB imdb = IMDB.getInstance();
        for (Request request : imdb.requests) {
            if (request.type == RequestTypes.ACTOR_ISSUE || request.type == RequestTypes.MOVIE_ISSUE) {
                for (User user : imdb.users) {
                    if (user.username.equals(request.userTo)) {
                        if (user instanceof Admin) {
                            request.registerObserver((Admin) user);
                            request.notifyObservers("new_request");
                        } else if (user instanceof Contributor) {
                            request.registerObserver((Contributor) user);
                            request.notifyObservers("new_request");
                        }
                        break;
                    }
                }
            }
        }
        for(Production production : imdb.productions) {
            for(User user : imdb.users) {
                if(user instanceof Contributor && ((Contributor<?>) user).contributions.contains(production)) {
                    production.registerObserver(user);
                }
                if(user instanceof Admin<?> && ((Admin<?>) user).contributions.contains(production)) {
                    if(!user.username.equals("admin"))
                        production.registerObserver(user);
                }
                if(user instanceof Regular || (user instanceof Contributor<?> && !((Contributor<?>) user).contributions.contains(production))) {
                    for(Rating rating : production.ratings) {
                        if(rating.username.equals(user.username)) {
                            production.registerObserver(user);
                        }
                    }
                }
            }
        }
    }

    public void restartApp() {
        userInterface.displayOutput("Welcome back to IMDB! Please enter your email and password to login.\n");
        login();
        startFlow(currentUser);
    }

    public void loadDataJSON() {
        actors = Parser.parseActors("src/actors.json");
        productions = Parser.parseProductions("src/production.json");
        requests = Parser.parseRequests("src/requests.json");
        users = Parser.parseUsers("src/accounts.json");
        for (Request request : requests) {
            if (request.type == RequestTypes.ACTOR_ISSUE || request.type == RequestTypes.MOVIE_ISSUE) {
                for (User user : users) {
                    if (user.username.equals(request.userTo)) {
                        if (user instanceof Admin) {
                            ((Admin) user).userRequests.add(request);
                        } else if (user instanceof Contributor) {
                            ((Contributor) user).userRequests.add(request);
                        }
                        break;
                    }
                }
            }
        }
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

    public void startFlow(User user) {
        switch (user.accountType) {
            case Regular:
                Flow.startRegularFlow((Regular) user);
                break;
            case Admin:
                Flow.startAdminFlow((Admin) user);
                break;
            case Contributor:
                Flow.startContributorFlow((Contributor) user);
                break;
        }
    }


    private void login() {
        String email = ((TerminalUI) userInterface).getEmail();
        String password = ((TerminalUI) userInterface).getPassword();
        for (User user : users) {
            Credentials credentials = user.userInformation.getCredentials();
            if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                userInterface.displayOutput("Welcome back user " + user.username + "!\n");
                userInterface.displayOutput("Username: " + user.username + "\n");
                if(user.accountType == AccountType.Admin) {
                    userInterface.displayOutput("User experience: Admin" + "\n");
                } else {
                    userInterface.displayOutput("User experience: " + user.experience + "\n");
                }
                currentUser = user;
                return;
            }
        }
        userInterface.displayOutput("Invalid credentials. Please try again.\n");
        login();
    }

    public static void main(String[] args) {
        IMDB imdb = IMDB.getInstance();
        imdb.run();
    }

    private void universalAdmin() {
        for (User user : IMDB.getInstance().users) {
            if (user.username.equals("admin")) {
                return;
            }
        }
        Credentials credentials = new Credentials("admin", "admin");
        LocalDate birthDate = LocalDate.now();
        User<?> admin = UserFactory.createUser(AccountType.Admin, "admin", credentials, "admin", null, 0, 'M', birthDate, null);
        users.add(admin);
        Parser.addUserToJson(admin);
        Parser.parseUsers("src/accounts.json");
    }

    public void startPage(User user) {
        if (user.accountType == AccountType.Admin) {
            StartPage.startAdminPage((Admin) user);
        } else if (user.accountType == AccountType.Contributor) {
            StartPage.startContributorPage((Contributor) user);
        } else {
            StartPage.startRegularPage((Regular) user);
        }
    }

    public class RequestsHolder {
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

        public void printRequests() {
            for (Request request : requests) {
                userInterface.displayOutput(request.toString());
            }
        }
    }


}

