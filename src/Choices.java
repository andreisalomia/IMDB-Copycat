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
}
