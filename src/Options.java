//package org.example;
import java.util.ArrayList;
import java.util.List;

public class Options {
    public static int regularOptions() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose action: \n");
        imdb.userInterface.displayOutput("\t1) View productions details\n");
        imdb.userInterface.displayOutput("\t2) View actors details\n");
        imdb.userInterface.displayOutput("\t3) View notifications\n");
        imdb.userInterface.displayOutput("\t4) Search for actor/movie/series\n");
        imdb.userInterface.displayOutput("\t5) Add/Delete actor/movie/series from favourites\n");
        imdb.userInterface.displayOutput("\t6) Add/Delete request\n");
        imdb.userInterface.displayOutput("\t7) Add/Delete review\n");
        imdb.userInterface.displayOutput("\t8) Logout\n");

        return returnChoice(8);
    }

    public static int contributorOptions() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose action: \n");
        imdb.userInterface.displayOutput("\t1) View productions details\n");
        imdb.userInterface.displayOutput("\t2) View actors details\n");
        imdb.userInterface.displayOutput("\t3) View notifications\n");
        imdb.userInterface.displayOutput("\t4) Search for actor/movie/series\n");
        imdb.userInterface.displayOutput("\t5) Add/Delete actor/movie/series from favourites\n");
        imdb.userInterface.displayOutput("\t6) Add/Delete request\n");
        imdb.userInterface.displayOutput("\t7) Add/Delete actor/production from the system\n");
        imdb.userInterface.displayOutput("\t8) View and solve requests\n");
        imdb.userInterface.displayOutput("\t9) Update information about actors/productions\n");
        imdb.userInterface.displayOutput("\t10) Logout\n");
        return returnChoice(10);
    }

    public static int filterProductionsOptions() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose filter: \n");
        imdb.userInterface.displayOutput("\t1) Filter by genre\n");
        imdb.userInterface.displayOutput("\t2) Filter by number of ratings\n");
        imdb.userInterface.displayOutput("\t3) Exit\n");
        imdb.userInterface.displayOutput("\t4) Clear filters\n");
        imdb.userInterface.displayOutput("\t5) Select production to display details\n");
        return returnChoice(5);
    }

    public static int displayProductionsChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("\t1) View all productions\n");
        imdb.userInterface.displayOutput("\t2) Filter productions\n");
        return returnChoice(2);
    }

    private static int returnChoice(int margin) {
        IMDB imdb = IMDB.getInstance();
        try {
            int choice = ((TerminalUI) imdb.userInterface).getNumber();
            validateUserInput(choice, margin);
            return choice;
        } catch (IllegalCommandException e) {
            imdb.userInterface.displayOutput("Error: " + e.getMessage() + "\n");
            return -1;
        }
    }

    private static void validateUserInput(int userInput, int margin) throws IllegalCommandException {
        if (userInput < 1 || userInput > margin) {
            throw new IllegalCommandException("Invalid input. Please enter a number in the given range.");
        }
    }

    public static int displayActorsChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) List all Actors\n");
        imdb.userInterface.displayOutput("\t2) Sort alphabetically\n");
        return returnChoice(2);
    }

    public static int addDeleteFavouritesChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Add actor/movie/series to favourites\n");
        imdb.userInterface.displayOutput("\t2) Delete actor/movie/series from favourites\n");
        imdb.userInterface.displayOutput("\t3) List all favorites\n");
        return returnChoice(3);
    }

    public static int chooseActorProduction() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Choose actor\n");
        imdb.userInterface.displayOutput("\t2) Choose production\n");
        return returnChoice(2);
    }

    public static int chooseAddDelete() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Add actor/production\n");
        imdb.userInterface.displayOutput("\t2) Delete actor/production\n");
        return returnChoice(2);
    }

    public static int addDeleteRequestChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Add actor/movie/series request\n");
        imdb.userInterface.displayOutput("\t2) Delete actor/movie/series request\n");
        return returnChoice(2);
    }

    public static int issueTypeChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose issue type: \n");
        imdb.userInterface.displayOutput("\t1) Delete Account\n");
        imdb.userInterface.displayOutput("\t2) Actor Issue\n");
        imdb.userInterface.displayOutput("\t3) Movie Issue\n");
        imdb.userInterface.displayOutput("\t4) Others\n");
        return returnChoice(4);
    }

    public static int addDeleteRatingChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Add production rating\n");
        imdb.userInterface.displayOutput("\t2) Delete production rating\n");
        imdb.userInterface.displayOutput("\t3) Exit\n");
        return returnChoice(3);
    }

    public static int startOptions() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Login\n");
        imdb.userInterface.displayOutput("\t2) Close the app\n");
        return returnChoice(2);
    }

    public static int viewSolveRequestsChoices(Staff user) {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose request or exit: \n");
        List<Request> requests = user.userRequests;
        for (int i = 0; i < requests.size(); i++) {
            imdb.userInterface.displayOutput("\t" + (i + 1) + ") " + requests.get(i).description + "\n");
        }
        imdb.userInterface.displayOutput("\t" + (requests.size() + 1) + ") Exit\n");
        return returnChoice(requests.size() + 1);
    }

    public static int updateInfoActorChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Update actor name\n");
        imdb.userInterface.displayOutput("\t2) Update actor biography\n");
        imdb.userInterface.displayOutput("\t3) Update actor performances\n");
        imdb.userInterface.displayOutput("\t4) Exit\n");
        return returnChoice(4);
    }

    public static int updateInfoMovieChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Update movie title\n");
        imdb.userInterface.displayOutput("\t2) Update movie description\n");
        imdb.userInterface.displayOutput("\t3) Update movie duration\n");
        imdb.userInterface.displayOutput("\t4) Update movie release date\n");
        imdb.userInterface.displayOutput("\t5) Update movie genres\n");
        imdb.userInterface.displayOutput("\t6) Update movie actors\n");
        imdb.userInterface.displayOutput("\t7) Update movie directors\n");
        imdb.userInterface.displayOutput("\t8) Exit\n");
        return returnChoice(8);
    }

    public static int updateInfoSeriesChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Update series title\n");
        imdb.userInterface.displayOutput("\t2) Update series description\n");
        imdb.userInterface.displayOutput("\t3) Update series release year\n");
        imdb.userInterface.displayOutput("\t4) Update series genres\n");
        imdb.userInterface.displayOutput("\t5) Update series actors\n");
        imdb.userInterface.displayOutput("\t6) Update series directors\n");
        imdb.userInterface.displayOutput("\t7) Update series number of seasons\n");
        imdb.userInterface.displayOutput("\t8) Update series episodes\n");
        imdb.userInterface.displayOutput("\t9) Exit\n");
        return returnChoice(9);
    }

    public static int adminOptions() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose action: \n");
        imdb.userInterface.displayOutput("\t1) View productions details\n");
        imdb.userInterface.displayOutput("\t2) View actors details\n");
        imdb.userInterface.displayOutput("\t3) View notifications\n");
        imdb.userInterface.displayOutput("\t4) Search for actor/movie/series\n");
        imdb.userInterface.displayOutput("\t5) Add/Delete actor/movie/series from favourites\n");
        imdb.userInterface.displayOutput("\t6) Add/Delete actor/production from the system\n");
        imdb.userInterface.displayOutput("\t7) View and solve requests\n");
        imdb.userInterface.displayOutput("\t8) Update information about actors/productions\n");
        imdb.userInterface.displayOutput("\t9) Add/Delete user account\n");
        imdb.userInterface.displayOutput("\t10) Logout\n");
        return returnChoice(10);
    }

    public static int addDeleteUserChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Add user\n");
        imdb.userInterface.displayOutput("\t2) Delete user\n");
        return returnChoice(2);
    }

    public static int updateInfoChoices() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Update\n");
        imdb.userInterface.displayOutput("\t2) Exit\n");
        return returnChoice(2);
    }

    public static int viewPersonalAllRequests() {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) View personal requests\n");
        imdb.userInterface.displayOutput("\t2) View all requests\n");
        return returnChoice(2);
    }
}