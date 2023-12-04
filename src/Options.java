public class Options {
    public static int regularOptions(IMDB imdb) {
        imdb.userInterface.displayOutput("Choose action: \n");
        imdb.userInterface.displayOutput("\t1) View productions details\n");
        imdb.userInterface.displayOutput("\t2) View actors details\n");
        imdb.userInterface.displayOutput("\t3) View notifications\n");
        imdb.userInterface.displayOutput("\t4) Search for actor/movie/series\n");
        imdb.userInterface.displayOutput("\t5) Add/Delete actor/movie/series from favourites\n");
        imdb.userInterface.displayOutput("\t6) Add/Delete request\n");
        imdb.userInterface.displayOutput("\t7) Add/Delete review\n");
        imdb.userInterface.displayOutput("\t8) Logout\n");

        return returnChoice(imdb, 8);
    }

    public static int filterProductionsOptions(IMDB imdb) {
        imdb.userInterface.displayOutput("Choose filter: \n");
        imdb.userInterface.displayOutput("\t1) Filter by genre\n");
        imdb.userInterface.displayOutput("\t2) Filter by number of ratings\n");
        imdb.userInterface.displayOutput("\t3) Exit\n");
        imdb.userInterface.displayOutput("\t4) Clear filters\n");
        imdb.userInterface.displayOutput("\t5) Select production to display details\n");
        return returnChoice(imdb, 5);
    }

    public static int displayProductionsChoices(IMDB imdb) {
        imdb.userInterface.displayOutput("\t1) View all productions\n");
        imdb.userInterface.displayOutput("\t2) Filter productions\n");
        return returnChoice(imdb, 2);
    }

    private static int returnChoice(IMDB imdb, int margin) {
        try {
            int choice = ((TerminalUI) imdb.userInterface).getNumber();
            validateUserInput(choice, margin);
            return choice;
        } catch (IllegalArgumentException e) {
            imdb.userInterface.displayOutput("Error: " + e.getMessage() + "\n");
            return -1; // Or any other default value
        }
    }

    private static void validateUserInput(int userInput, int margin) {
        // Check if the input is a valid option
        if (userInput < 1 || userInput > margin) {
            throw new IllegalArgumentException("Invalid input. Please enter a number in the given range.");
        }
    }

    public static int displayActorsChoices(IMDB imdb) {
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) List all Actors\n");
        imdb.userInterface.displayOutput("\t2) Sort alphabetically\n");
        return returnChoice(imdb, 2);
    }
}