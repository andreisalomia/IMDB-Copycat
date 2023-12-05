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
        if(notifs.isEmpty()) {
            imdb.userInterface.displayOutput("No notifications!\n");
            return;
        }
        for(String notif : notifs) {
            imdb.userInterface.displayOutput(notif + "\n");
        }
        imdb.userInterface.displayOutput("\n");
    }

    public static void searchForActorMovieSeries() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Enter actor/movie/series name: ");
        String input = imdb.userInterface.getInput();
        try {
            for(Production prod : imdb.productions) {
                if(prod.title.equals(input)) {
                    prod.displayInfo();
                    return;
                }
            }
            for(Actor actor : imdb.actors) {
                if(actor.name.equals(input)) {
                    actor.displayInfo();
                    return;
                }
            }
            throw new Exception();
        }
        catch (Exception e) {
            imdb.userInterface.displayOutput("No actor/movie/series found with the given name.\n");
        }
    }

    public static void addDeleteFavourites(User user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.addDeleteFavouritesChoices();
        int item = Options.chooseActorProduction();
        switch (choice) {
            case 1:
                if(item == 1) {
                    imdb.userInterface.displayOutput("Enter actor name: ");
                    String input = imdb.userInterface.getInput();
                    try {
                        for(Actor actor : imdb.actors) {
                            if(actor.name.equals(input)) {
                                user.addActorToFavorites(actor);
                                return;
                            }
                        }
                        throw new Exception();
                    }
                    catch (Exception e) {
                        imdb.userInterface.displayOutput("No actor found with the given name.\n");
                    }
                }
                else if(item == 2) {
                    imdb.userInterface.displayOutput("Enter production title: ");
                    String input = imdb.userInterface.getInput();
                    try {
                        for(Production prod : imdb.productions) {
                            if(prod.title.equals(input)) {
                                user.addProductionToFavorites(prod);
                                return;
                            }
                        }
                        throw new Exception();
                    }
                    catch (Exception e) {
                        imdb.userInterface.displayOutput("No production found with the given title.\n");
                    }
                }
                break;
            case 2:
                if (item == 1) {
                    imdb.userInterface.displayOutput("Enter actor name: ");
                    String input = imdb.userInterface.getInput();
                    try {
                        for(Actor actor : imdb.actors) {
                            if(actor.name.equals(input)) {
                                user.removeActorFromFavourites(actor);
                                return;
                            }
                        }
                        throw new Exception();
                    }
                    catch (Exception e) {
                        imdb.userInterface.displayOutput("No actor found with the given name.\n");
                    }
                }
                else if(item == 2) {
                    imdb.userInterface.displayOutput("Enter production title: ");
                    String input = imdb.userInterface.getInput();
                    try {
                        for(Production prod : imdb.productions) {
                            if(prod.title.equals(input)) {
                                user.removeProductionFromFavourites(prod);
                                return;
                            }
                        }
                        throw new Exception();
                    }
                    catch (Exception e) {
                        imdb.userInterface.displayOutput("No production found with the given title.\n");
                    }
                }
                break;
        }
    }
}
