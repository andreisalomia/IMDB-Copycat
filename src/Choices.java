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
}
