//package org.example;
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

    public static void searchForActorMovieSeries(User user) {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Enter actor/movie/series name: ");
        String input = imdb.userInterface.getInput();
        try {
            for (Production prod : imdb.productions) {
                if (prod.title.equals(input)) {
                    prod.displayInfo();
//            if the user is a contributor and he added the actor/production to the system then he can update it
                    if (user instanceof Contributor) {
                        if (((Contributor) user).contributions.contains(prod)) {
                            int choice = Options.updateInfoChoices();
                            if (choice == 1) {
                                ((Contributor) user).updateProduction((Contributor) user, prod.title);
                            }
                        }
                    }
//                    if the user is an admin then he can update the actor/production
                    if (user instanceof Admin) {
                        int choice = Options.updateInfoChoices();
                        if (choice == 1) {
                            ((Admin) user).updateProduction((Admin) user, prod.title);
                        }
                    }
                    return;
                }
            }
            for (Actor actor : imdb.actors) {
                if (actor.name.equals(input)) {
                    actor.displayInfo();
//            if the user is a contributor and he added the actor/production to the system then he can update it
                    if (user instanceof Contributor) {
                        if (((Contributor) user).contributions.contains(actor)) {
                            int choice = Options.updateInfoChoices();
                            if (choice == 1) {
                                ((Contributor) user).updateActor((Contributor) user, actor.name);
                            }
                        }
                    }
//                    if the user is an admin then he can update the actor/production
                    if (user instanceof Admin) {
                        int choice = Options.updateInfoChoices();
                        if (choice == 1) {
                            ((Admin) user).updateActor((Admin) user, actor.name);
                        }
                    }
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
//                        show the user a list of actors that he can delete, if the user is a contributor the list will have only actors that he himself added, if
//                        the user is an admin it will have all the actors in the system
                        List<Actor> actors = new ArrayList<>();
                        int index = 1;
                        for (Actor actor : imdb.actors) {
                            if (user instanceof Contributor) {
                                if (((Contributor) user).contributions.contains(actor)) {
                                    imdb.userInterface.displayOutput(index + ". " + actor.name + "\n");
                                    index++;
                                    actors.add(actor);
                                }
                            } else {
                                imdb.userInterface.displayOutput(index + ". " + actor.name + "\n");
                                index++;
                                actors.add(actor);
                            }
                        }
                        imdb.userInterface.displayOutput("Choose actor: ");
                        int actorNumber = ((TerminalUI) imdb.userInterface).getNumber();
                        if (actorNumber < 1 || actorNumber >= index) {
                            imdb.userInterface.displayOutput("Invalid actor number.\n");
                            return;
                        }
                        Actor actor = actors.get(actorNumber - 1);
                        ((Staff<?>) user).removeActorSystem(actor);
                    } else {
//                        show the user a list of productions that he can delete, if the user is a contributor the list will have only productions that he himself added, if
//                        the user is an admin it will have all the productions in the system
                        List<Production> productions = new ArrayList<>();
                        int index = 1;
                        for (Production prod : imdb.productions) {
                            if (user instanceof Contributor) {
                                if (((Contributor) user).contributions.contains(prod)) {
                                    imdb.userInterface.displayOutput(index + ". " + prod.title + "\n");
                                    index++;
                                    productions.add(prod);
                                }
                            } else {
                                imdb.userInterface.displayOutput(index + ". " + prod.title + "\n");
                                index++;
                                productions.add(prod);
                            }
                        }
                        imdb.userInterface.displayOutput("Choose production: ");
                        int productionNumber = ((TerminalUI) imdb.userInterface).getNumber();
                        if (productionNumber < 1 || productionNumber >= index) {
                            imdb.userInterface.displayOutput("Invalid production number.\n");
                            return;
                        }
                        Production prod = productions.get(productionNumber - 1);
                        ((Staff<?>) user).removeProductionSystem(prod);
                    }
                    break;
            }
        }
    }

    public static void viewSolveRequests(Admin user) {
//        ask the admin if he wants to see personal requests or all requests
        IMDB imdb = IMDB.getInstance();
        int choice = Options.viewPersonalAllRequests();
        if(choice == 1) {
            if(user.userRequests.isEmpty()) {
                imdb.userInterface.displayOutput("No requests!\n");
                return;
            }
            viewSolveRequestsContributor(user);
            return;
        }
        if (choice == 2) {
            if (IMDB.RequestsHolder.getRequests().isEmpty()) {
                imdb.userInterface.displayOutput("No requests!\n");
                return;
            }
            Admin.viewSolveRequestsAll(user);
            return;
        }

    }

    public static void viewSolveRequestsContributor(Staff user) {
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
        user.solveRequest(r);
    }

    public static void updateInfoActorProduction(Staff user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.chooseActorProduction();
        if (choice == 1) {
            user.updateActor(user, null);
        } else if (choice == 2) {
//            call update production method
            user.updateProduction(user, null);
        }
    }

    public static void addDeleteUser(Admin user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.addDeleteUserChoices();
        if (choice == 1) {
//            add a user to the system
            Admin.addUser();
        } else if (choice == 2) {
//            delete a user from the system
            Admin.removeUser();
        }
    }
}
