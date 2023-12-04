import java.awt.*;

public class Flow {
    public static void startRegularFlow(Regular user, IMDB imdb) {
        int choice = Options.regularOptions(imdb);
        switch (choice) {
            case 1:
                // View productions details
                Choices.viewProductionDetails(imdb);
                break;
            case 2:
                // View actors details
                Choices.viewActorDetails(imdb);
                break;
            case 3:
                // View notifications
                break;
            case 4:
                // Search for actor/movie/series
                break;
            case 5:
                // Add/Delete actor/movie/series from favourites
                break;
            case 6:
                // Add/Delete request
                break;
            case 7:
                // Add/Delete review
                break;
            case 8:
                // Logout
                return;

        }
        startRegularFlow(user, imdb);
    }

    public static void startAdminFlow(Admin user, IMDB imdb) {

    }

    public static void startContributorFlow(Contributor user, IMDB imdb) {

    }
}
