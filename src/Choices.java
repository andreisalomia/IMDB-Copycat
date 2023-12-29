import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class Choices {
    public static void viewProductionDetails() {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.displayProductionsChoices();
        switch (choice) {
            case 1:
                Filter.listAllProductions();
                break;
            case 2:
                Filter.filterProductions();
                break;
        }
    }

    public static void viewActorDetails() {
        int choice = Options.displayActorsChoices();
        switch (choice) {
            case 1:
                Filter.listAllActors();
                break;
            case 2:
                // Sort alphabetically
                Filter.listActorsAlphabetically();
                break;
        }
    }

    public static void viewNotifications(User user) {
        IMDB imdb = IMDB.getInstance();
        List<String> notifs = user.notifications;
        if (notifs.isEmpty()) {
            imdb.userInterface.displayOutput("No notifications!\n");
            return;
        }
        for (String notif : notifs) {
            imdb.userInterface.displayOutput(notif + "\n");
        }
        imdb.userInterface.displayOutput("\n");
    }

    public static void searchForActorMovieSeries() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Enter actor/movie/series name: ");
        String input = imdb.userInterface.getInput();
        try {
            for (Production prod : imdb.productions) {
                if (prod.title.equals(input)) {
                    prod.displayInfo();
                    return;
                }
            }
            for (Actor actor : imdb.actors) {
                if (actor.name.equals(input)) {
                    actor.displayInfo();
                    return;
                }
            }
            throw new Exception();
        } catch (Exception e) {
            imdb.userInterface.displayOutput("No actor/movie/series found with the given name.\n");
        }
    }

    public static void addDeleteFavourites(User user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.addDeleteFavouritesChoices();
        if (choice == -1) {
            addDeleteFavourites(user);
        } else if (choice == 3) {
            Filter.listFavorites(user);
            return;
        }
        int item = Options.chooseActorProduction();
        switch (choice) {
            case 1:
                if (item == 1) {
                    imdb.userInterface.displayOutput("Enter actor name: ");
//                    Check if actor already exists in the list
                    String input = imdb.userInterface.getInput();
                    if (Filter.actorCheckExists(user, input)) {
                        imdb.userInterface.displayOutput("Actor already exists in the list.\n");
                        return;
                    }
                    try {
                        for (Actor actor : imdb.actors) {
                            if (actor.name.equals(input)) {
                                user.addActorToFavorites(actor);
                                return;
                            }
                        }
                        throw new Exception();
                    } catch (Exception e) {
                        imdb.userInterface.displayOutput("No actor found with the given name.\n");
                    }
                } else if (item == 2) {
                    imdb.userInterface.displayOutput("Enter production title: ");
                    String input = imdb.userInterface.getInput();
                    if (Filter.productionCheckExists(user, input)) {
                        imdb.userInterface.displayOutput("Production already exists in the list.\n");
                        return;
                    }
                    try {
                        for (Production prod : imdb.productions) {
                            if (prod.title.equals(input)) {
                                user.addProductionToFavorites(prod);
                                return;
                            }
                        }
                        throw new Exception();
                    } catch (Exception e) {
                        imdb.userInterface.displayOutput("No production found with the given title.\n");
                    }
                }
                break;
            case 2:
                if (item == 1) {
                    imdb.userInterface.displayOutput("Enter actor name: ");
                    String input = imdb.userInterface.getInput();
//                    Check if actor already exists in the list
                    if (!Filter.actorCheckExists(user, input)) {
                        imdb.userInterface.displayOutput("Actor does not exist in the list.\n");
                        return;
                    }
                    try {
                        for (Actor actor : imdb.actors) {
                            if (actor.name.equals(input)) {
                                user.removeActorFromFavourites(actor);
                                return;
                            }
                        }
                        throw new Exception();
                    } catch (Exception e) {
                        imdb.userInterface.displayOutput("No actor found with the given name.\n");
                    }
                } else if (item == 2) {
                    imdb.userInterface.displayOutput("Enter production title: ");
                    String input = imdb.userInterface.getInput();
                    if (!Filter.productionCheckExists(user, input)) {
                        imdb.userInterface.displayOutput("Production does not exist in the list.\n");
                        return;
                    }
                    try {
                        for (Production prod : imdb.productions) {
                            if (prod.title.equals(input)) {
                                user.removeProductionFromFavourites(prod);
                                return;
                            }
                        }
                        throw new Exception();
                    } catch (Exception e) {
                        imdb.userInterface.displayOutput("No production found with the given title.\n");
                    }
                }
                break;
        }
    }

    public static void addDeleteRequest(User user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.addDeleteRequestChoices();
        if (choice == -1) {
            addDeleteRequest(user);
        }
        switch (choice) {
            case 1:
                int issueType = Options.issueTypeChoices();
                imdb.userInterface.displayOutput("Please describe the problem. If you chose movie/actor issue please mention the title/name: ");
                String input = imdb.userInterface.getInput();
                Request request = Request.RequestBuilder(user, input, issueType);
                if (user instanceof Regular) {
                    ((Regular) user).createRequest(request);
                } else if (user instanceof Contributor) {
//                    Check if the request is regarding an actor or a production that the contributor himself added to the system
                    for (Actor actor : imdb.actors) {
                        if (actor.name.equals(request.problemName)) {
                            if (((Contributor) user).contributions.contains(actor)) {
                                imdb.userInterface.displayOutput("You can only create requests regarding actors that were not added by you.\n");
                                return;
                            }
                        }
                    }
                    for (Production prod : imdb.productions) {
                        if (prod.title.equals(request.problemName)) {
                            if (((Contributor) user).contributions.contains(prod)) {
                                imdb.userInterface.displayOutput("You can only create requests regarding productions that were not added by you.\n");
                                return;
                            }
                        }
                    }
                    ((Contributor) user).createRequest(request);
                }
                break;
            case 2:
//                show the user his sent request and make him choose which of them by typing the number
                int index = 1;
                List<Request> userRequests = new ArrayList<>();
                for (Iterator<Request> iterator = imdb.requests.iterator(); iterator.hasNext(); ) {
                    Request r = iterator.next();
                    if (r.userFrom.equals(user.username)) {
                        imdb.userInterface.displayOutput(index + ". " + r.description + "\n");
                        index++;
                        userRequests.add(r);
                    }
                }
                imdb.userInterface.displayOutput("Choose request: ");
                int requestNumber = ((TerminalUI) imdb.userInterface).getNumber();
                if (requestNumber < 1 || requestNumber > index) {
                    imdb.userInterface.displayOutput("Invalid request number.\n");
                    return;
                }
                Request r = userRequests.get(requestNumber - 1);
                if (user instanceof Regular) {
                    ((Regular) user).removeRequest(r);
                } else if (user instanceof Contributor) {
                    ((Contributor) user).removeRequest(r);
                }
                break;
        }


    }

    public static void addDeleteRating(Regular user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.addDeleteRatingChoices();
        if (choice == -1) {
            addDeleteRating(user);
        }
        switch (choice) {
            case 1:
                imdb.userInterface.displayOutput("Enter production title: ");
                String input = imdb.userInterface.getInput();
                try {
                    for (Production prod : imdb.productions) {
                        if (prod.title.equals(input)) {
                            imdb.userInterface.displayOutput("Enter rating: ");
                            int rating = ((TerminalUI) imdb.userInterface).getNumber();
                            if (rating < 1 || rating > 10) {
                                imdb.userInterface.displayOutput("Invalid rating.\n");
                                return;
                            }
                            imdb.userInterface.displayOutput("Enter message: ");
                            String message = imdb.userInterface.getInput();
                            Rating r = new Rating(user.username, rating, message);
                            user.addRating(r, prod);
                            return;
                        }
                    }
                    throw new Exception();
                } catch (Exception e) {
                    imdb.userInterface.displayOutput("No production found with the given title.\n");
                }
                break;
            case 2:
                imdb.userInterface.displayOutput("Enter production title: ");
                input = imdb.userInterface.getInput();
                try {
                    for (Production prod : imdb.productions) {
                        if (prod.title.equals(input)) {
                            Production.removeRating(prod, user);
                            return;
                        }
                    }
                    throw new Exception();
                } catch (Exception e) {
                    imdb.userInterface.displayOutput("No production found with the given title.\n");
                }
                break;
            case 3:
                break;
        }
    }

    public static void addDeleteActorProduction(User user) {
        if (user instanceof Contributor || user instanceof Admin) {
            IMDB imdb = IMDB.getInstance();
            int choice = Options.chooseAddDelete();
            int item = Options.chooseActorProduction();
            boolean found = false;
            switch (choice) {
                case 1:
                    if (item == 1) {
                        imdb.userInterface.displayOutput("Enter actor name: ");
                        String input = imdb.userInterface.getInput();
//                        Check if actor is already in the system
                        for (Actor actor : imdb.actors) {
                            if (actor.name.equals(input)) {
                                imdb.userInterface.displayOutput("Actor already exists in the system.\n");
                                return;
                            }
                        }
                        ((Staff<?>) user).addActorSystem(input);
                    } else {
                        imdb.userInterface.displayOutput("Enter production title: ");
                        String input = imdb.userInterface.getInput();
//                        Check if production is already in the system
                        for (Production prod : imdb.productions) {
                            if (prod.title.equals(input)) {
                                imdb.userInterface.displayOutput("Production already exists in the system.\n");
                                return;
                            }
                        }
                        ((Staff<?>) user).addProductionSystem(input);
                    }
                    break;
                case 2:
                    if (item == 1) {
                        imdb.userInterface.displayOutput("Enter actor name: ");
                        String input = imdb.userInterface.getInput();
                        for (Actor actor : imdb.actors) {
                            if (actor.name.equals(input)) {
//                                if the user is a contributor then he can only remove an actor that he added
                                if (user instanceof Contributor) {
                                    if (!((Contributor) user).contributions.contains(actor)) {
                                        imdb.userInterface.displayOutput("You can only remove actors that you added.\n");
                                        return;
                                    }
                                }
                                ((Staff<?>) user).removeActorSystem(actor);
                                found = true;
                                return;
                            }
                        }
                        if (!found) {
                            imdb.userInterface.displayOutput("No actor found with the given name.\n");
                        }
                    } else {
                        imdb.userInterface.displayOutput("Enter production title: ");
                        String input = imdb.userInterface.getInput();
                        for (Production prod : imdb.productions) {
                            if (prod.title.equals(input)) {
//                                if the user is a contributor then he can only remove a production that he added
                                if (user instanceof Contributor) {
                                    if (!((Contributor) user).contributions.contains(prod)) {
                                        imdb.userInterface.displayOutput("You can only remove productions that you added.\n");
                                        return;
                                    }
                                }
                                ((Staff<?>) user).removeProductionSystem(prod);
                                found = true;
                                return;
                            }
                        }
                        if (!found) {
                            imdb.userInterface.displayOutput("No production found with the given title.\n");
                        }
                    }
                    break;
            }
        }
    }

    public static void viewSolveRequests(Admin user) {
    }
    public static void viewSolveRequests(Contributor user) {
        IMDB imdb = IMDB.getInstance();
        if (user.userRequests.isEmpty()) {
            imdb.userInterface.displayOutput("No requests!\n");
            return;
        }
        int choice = Options.viewSolveRequestsChoices(user);
        if (choice == user.userRequests.size() + 1) {
            return;
        }
        Request r = (Request) user.userRequests.get(choice - 1);
//        ask the user if he wants to delete the request or to solve it
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Delete request\n");
        imdb.userInterface.displayOutput("\t2) Solve request\n");
        int option = ((TerminalUI) imdb.userInterface).getNumber();
        if (option == 1) {
//            TODO: notify the user that the request was deleted
            user.removeRequest(r);
            imdb.requests.remove(r);
            Parser.writeRequests();
            Parser.updateLists();
        } else if (option == 2) {
            user.removeRequest(r);
            imdb.requests.remove(r);
            Parser.writeRequests();
            Parser.updateLists();
//            TODO: notify user that the request was solved
            user.solveRequest(r);
        }
    }

    public static void updateInfoActorProduction(Staff user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.chooseActorProduction();
        if (choice == 1) {
            imdb.userInterface.displayOutput("Enter the actor name: ");
            String input = imdb.userInterface.getInput();
            Actor a = null;
            for (Actor actor : imdb.actors) {
                if (actor.name.equals(input)) {
                    a = actor;
                }
            }
            if (a == null) {
                imdb.userInterface.displayOutput("No actor found with the given name.\n");
                return;
            }
//            if the user is a contributor then he can only update an actor that he added
            if (user instanceof Contributor) {
                if (!((Contributor) user).contributions.contains(a)) {
                    imdb.userInterface.displayOutput("You can only update actors that you added.\n");
                    return;
                }
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
            }
        }
        else if (choice == 2) {
            imdb.userInterface.displayOutput("Enter the production title: ");
            String input = imdb.userInterface.getInput();
            Production p = null;
            for (Production prod : imdb.productions) {
                if (prod.title.equals(input)) {
                    p = prod;
                }
            }
            if (p == null) {
                imdb.userInterface.displayOutput("No production found with the given title.\n");
                return;
            }
//            if the user is a contributor then he can only update a production that he added
            if (user instanceof Contributor) {
                if (!((Contributor) user).contributions.contains(p)) {
                    imdb.userInterface.displayOutput("You can only update productions that you added.\n");
                    return;
                }
            }
            int option;
            if(p instanceof Movie) {
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
                }
            }
            else {
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
                }
            }
        }
    }

    public static void addDeleteUser(Admin user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.addDeleteUserChoices();
        if (choice == 1) {
//            add a user to the system
        }
        else if (choice == 2) {
//            delete a user from the system
//            show the Admin a list of all the users that he can delete
            int i = 1;
            for(User u : imdb.users) {
                if(u.accountType != AccountType.Admin) {
                    imdb.userInterface.displayOutput(i + ". " + u.username + "\n");
                    i++;
                }
            }
            imdb.userInterface.displayOutput("Choose user: ");
            int userNumber = ((TerminalUI) imdb.userInterface).getNumber();
            if(userNumber < 1 || userNumber > i) {
                imdb.userInterface.displayOutput("Invalid user number.\n");
                return;
            }
            User u = imdb.users.get(userNumber - 1);
            if(u.accountType == AccountType.Admin) {
                imdb.userInterface.displayOutput("You cannot delete an admin.\n");
                return;
            }
//            If we want to delete a contributor all of his contributions must be added to the
            if(u.accountType == AccountType.Contributor) {

            }
        }
    }
}
