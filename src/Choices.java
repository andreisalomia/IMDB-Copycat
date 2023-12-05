import java.util.List;
import java.util.Scanner;

public class Choices {
    public static void viewProductionDetails(IMDB imdb) {
        int choice = Options.displayProductionsChoices(imdb);
        switch (choice) {
            case 1:
                Filter.listAllProductions(imdb);
                break;
            case 2:
                Filter.filterProductions(imdb);
                break;
        }
    }

    public static void viewActorDetails(IMDB imdb) {
        int choice = Options.displayActorsChoices(imdb);
        switch (choice) {
            case 1:
                Filter.listAllActors(imdb);
                break;
            case 2:
                // Sort alphabetically
                Filter.listActorsAlphabetically(imdb);
                break;
        }
    }

    public static void viewNotifications(Regular user, IMDB imdb) {
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
}
