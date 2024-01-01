//package org.example;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.time.LocalDate;

public class Admin<T extends Comparable<T>> extends Staff<T>{
    public Admin(Information info, AccountType type, String username, Integer experience) {
        super(info, type, username, experience);
    }

    public static void addUser() {
        // add user using IMDB class
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose account type:\n1. Regular\n2. Contributor\n3. Admin\n");
        int accountType = ((TerminalUI) imdb.userInterface).getNumber();
        if (accountType < 1 || accountType > 3) {
            imdb.userInterface.displayOutput("Invalid account type.\n");
            return;
        }
//        cast accountType to enum AccountType
        AccountType type = AccountType.values()[accountType - 1];
        imdb.userInterface.displayOutput("Enter first and last name: ");
        String name = ((TerminalUI) imdb.userInterface).getInput();
        imdb.userInterface.displayOutput("Enter email: ");
        String email = ((TerminalUI) imdb.userInterface).getEmail();
//        break the name into first and last name and create an username of tipe firstname_lastname_randomNumber
        String username = name.split(" ")[0] + "_" + name.split(" ")[1] + "_" + (int) (Math.random() * 1000);
//        generate a random password made of 8 characters
        String password = generateRandomPassword(8);
        Credentials credentials = new Credentials(email, password);
//        get country
        imdb.userInterface.displayOutput("Enter country: ");
        String country = ((TerminalUI) imdb.userInterface).getInput();
//        get age
        imdb.userInterface.displayOutput("Enter age: ");
        int age = ((TerminalUI) imdb.userInterface).getNumber();
//        get gender F/M
        imdb.userInterface.displayOutput("Enter gender: ");
        char gender = ((TerminalUI) imdb.userInterface).getInput().charAt(0);
//        get birthdate: year, month, day
        imdb.userInterface.displayOutput("Enter birth year: ");
        int year = ((TerminalUI) imdb.userInterface).getNumber();
        imdb.userInterface.displayOutput("Enter birth month: ");
        int month = ((TerminalUI) imdb.userInterface).getNumber();
        imdb.userInterface.displayOutput("Enter birth day: ");
        int day = ((TerminalUI) imdb.userInterface).getNumber();
//        create a LocalDate object
        LocalDate birthDate = LocalDate.of(year, month, day);
        User<?> user = UserFactory.createUser(type, username, credentials, name, country, age, gender, birthDate, 0);
        imdb.users.add(user);
        Parser.addUserToJson(user);
        Parser.updateLists();
    }

    public static void removeUser() {
        IMDB imdb = IMDB.getInstance();
//            show the Admin a list of all the users that he can delete
        int i = 1;
//            create a list of all the users that can be deleted so that the number the user chooses can be respected
        List<User> users = new ArrayList<>();
        for (User u : imdb.users) {
            if (u.accountType != AccountType.Admin) {
                imdb.userInterface.displayOutput(i + ". " + u.username + "\n");
                i++;
                users.add(u);
            }
        }
        imdb.userInterface.displayOutput("Choose user: ");
        int userNumber = ((TerminalUI) imdb.userInterface).getNumber();
        if (userNumber < 1 || userNumber >= i) {
            imdb.userInterface.displayOutput("Invalid user number.\n");
            return;
        }
        User u = users.get(userNumber - 1);
//            print the contributions of the user that will be deleted
        if (u.accountType == AccountType.Admin) {
            imdb.userInterface.displayOutput("You cannot delete an admin.\n");
            return;
        }
//            If we want to delete a contributor all of his contributions must be added to the admin with "admin" username
        if (u.accountType == AccountType.Contributor) {
//                    Get user with "admin" username
            for (User user1 : imdb.users) {
                if (user1.username.equals("admin")) {
                    ((Staff<?>) user1).contributions.addAll(((Staff<?>) u).contributions);
                    Parser.updateContributions((Staff<?>) user1);
                }
            }
        }
        imdb.users.remove(u);
        Parser.removeUser(u);
        Parser.updateLists();
        removeAssosiatedData(u);
    }

    private static void removeAssosiatedData(User<?> user) {
        // remove all data associated with user
//        first remove the ratings
        IMDB imdb = IMDB.getInstance();
//        remove the ratings from the productions
        for (Production p : imdb.productions) {
            for (Rating r : p.ratings) {
                if (r.username.equals(user.username)) {
                    p.ratings.remove(r);
                    break;
                }
            }
        }
        Parser.writeRatings();
        Parser.updateLists();
//        remove the requests from imdb.requests and from each users list of requests (if the user is a contributor or an admin)
//        also remove the requests from the RequestsHolder class
        for (Request r : imdb.requests) {
            if (r.userFrom.equals(user.username)) {
                imdb.requests.remove(r);
                break;
            }
        }
        for (User u : imdb.users) {
            if (u.accountType == AccountType.Contributor || u.accountType == AccountType.Admin) {
                for (Request r : ((Staff<?>) u).userRequests) {
                    if (r.userFrom.equals(user.username)) {
                        ((Staff<?>) u).userRequests.remove(r);
                        break;
                    }
                }
            }
        }
        for (Request r : IMDB.RequestsHolder.getRequests()) {
            if (r.userFrom.equals(user.username)) {
                IMDB.RequestsHolder.removeRequest(r);
                break;
            }
        }
        Parser.writeRequests();
        Parser.updateLists();
//        search through the notifications of every user and delete those that are from the user that is being deleted
        for (User u : imdb.users) {
            for (Object notification : u.notifications) {
                String find = (String) notification;
                if (find.contains(user.username)) {
                    u.notifications.remove(notification);
                    Parser.updateNotifications(u);
                    break;
                }
            }
        }
        Parser.updateLists();
    }

    public static String generateRandomPassword(int length) {
        String ALLOWED_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+;";
        Random random = new Random();
        StringBuilder password = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALLOWED_CHARACTERS.length());
            password.append(ALLOWED_CHARACTERS.charAt(randomIndex));
        }
        return password.toString();
    }

    public static void viewSolveRequestsAll(Admin user) {
        IMDB imdb = IMDB.getInstance();
        int i = 1;
        List<Request> requests = new ArrayList<>();
        requests.addAll(IMDB.RequestsHolder.getRequests());
        for (Request r : requests) {
            imdb.userInterface.displayOutput(i + ". " + r.userFrom + " " + r.type + " " + r.description + "\n");
            i++;
        }
        imdb.userInterface.displayOutput("Choose request: ");
        int requestNumber = ((TerminalUI) imdb.userInterface).getNumber();
        if (requestNumber < 1 || requestNumber >= i) {
            imdb.userInterface.displayOutput("Invalid request number.\n");
            return;
        }
        Request r = requests.get(requestNumber - 1);
//        ask admin if he wants to delete a request or to mark one as solved
        imdb.userInterface.displayOutput("1. Delete request\n2. Mark request as solved\n3. Go back\n");
        int option = ((TerminalUI) imdb.userInterface).getNumber();
        if (option < 1 || option > 3) {
            imdb.userInterface.displayOutput("Invalid option.\n");
            return;
        }
        if (option == 1) {
//            remove from imdb.requests and from RequestsHolder
            imdb.requests.remove(r);
            IMDB.RequestsHolder.removeRequest(r);
            Parser.writeRequests();
            Parser.updateLists();
            return;
        }
        if (option == 2) {
//            remove from imdb.requests and from RequestsHolder
            imdb.requests.remove(r);
            IMDB.RequestsHolder.removeRequest(r);
            Parser.writeRequests();
            Parser.updateLists();
        }
        if (option == 3) {
            return;
        }
    }

    @Override
    public void updateProduction(Staff user, String title) {
        IMDB imdb = IMDB.getInstance();
        if(title == null) {
            imdb.userInterface.displayOutput("Enter the production title: ");
            title = imdb.userInterface.getInput();
        }
        Production p = null;
        for (Production prod : imdb.productions) {
            if (prod.title.equals(title)) {
                p = prod;
            }
        }
        if (p == null) {
            imdb.userInterface.displayOutput("No production found with the given title.\n");
            return;
        }
        int option;
        if (p instanceof Movie) {
            option = Options.updateInfoMovieChoices();
            switch (option) {
                case 1:
                    Update.updateMovieTitle((Movie) p);
                    break;
                case 2:
                    Update.updateMovieDescription((Movie) p);
                    break;
                case 3:
                    Update.updateMovieDuration((Movie) p);
                    break;
                case 4:
                    Update.updateMovieReleaseDate((Movie) p);
                    break;
                case 5:
                    Update.updateMovieGenre((Movie) p);
                    break;
                case 6:
                    Update.updateMovieActors((Movie) p);
                    break;
                case 7:
                    Update.updateMovieDirectors((Movie) p);
                    break;
                case 8:
                    Flow.startAdminFlow((Admin) user);
                    break;
            }
        } else {
            option = Options.updateInfoSeriesChoices();
            switch (option) {
                case 1:
                    Update.updateSeriesTitle((Series) p);
                    break;
                case 2:
                    Update.updateSeriesDescription((Series) p);
                    break;
                case 3:
                    Update.updateSeriesReleaseYear((Series) p);
                    break;
                case 4:
                    Update.updateSeriesGenre((Series) p);
                    break;
                case 5:
                    Update.updateSeriesActors((Series) p);
                    break;
                case 6:
                    Update.updateSeriesDirectors((Series) p);
                    break;
                case 7:
                    Update.updateSeriesNrSeasons((Series) p);
                    break;
                case 8:
                    Update.updateSeriesSeasons((Series) p);
                    break;
                case 9:
                    Flow.startAdminFlow((Admin) user);
                    break;
            }
        }
    }

    @Override
    public void updateActor(Staff user, String title) {
        IMDB imdb = IMDB.getInstance();
        if (title == null) {
            imdb.userInterface.displayOutput("Enter the actor name: ");
            title = imdb.userInterface.getInput();
        }
        Actor a = null;
        for (Actor actor : imdb.actors) {
            if (actor.name.equals(title)) {
                a = actor;
            }
        }
        if (a == null) {
            imdb.userInterface.displayOutput("No actor found with the given name.\n");
            return;
        }
        int option = Options.updateInfoActorChoices();
        switch (option) {
            case 1:
                Update.updateActorName(a);
                break;
            case 2:
                Update.updateActorBio(a);
                break;
            case 3:
//               display all actor performances and remember the number the user chooses
                Update.updateActorPerformance(a);
                break;
            case 4:
                Flow.startAdminFlow((Admin) user);
                break;
        }
    }

    public void removeRequest(Request r) {
        // remove request using Request class
        IMDB imdb = IMDB.getInstance();
        imdb.requests.remove(r);
        Parser.writeRequests();
        if (r.type == RequestTypes.ACTOR_ISSUE || r.type == RequestTypes.MOVIE_ISSUE) {
            for (User user : imdb.users) {
                if (user.username.equals(r.userTo)) {
                    if (user instanceof Admin) {
                        ((Admin) user).userRequests.remove(r);
                    } else if (user instanceof Contributor) {
                        ((Contributor) user).userRequests.remove(r);
                    }
                    break;
                }
            }
        }
        if(r.type == RequestTypes.OTHERS || r.type == RequestTypes.DELETE_ACCOUNT) {
            IMDB.RequestsHolder.removeRequest(r);
        }
    }

}
