import java.awt.*;

public class Flow {
    public static void startRegularFlow(Regular user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.regularOptions();
        switch (choice) {
            case 1:
                // View productions details
                Choices.viewProductionDetails();
                break;
            case 2:
                // View actors details
                Choices.viewActorDetails();
                break;
            case 3:
                // View notifications
                Choices.viewNotifications(user);
                break;
            case 4:
                // Search for actor/movie/series
                Choices.searchForActorMovieSeries();
                break;
            case 5:
                // Add/Delete actor/movie/series from favourites
                Choices.addDeleteFavourites(user);
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
        startRegularFlow(user);
    }

    public static void startAdminFlow(Admin user) {

    }

    public static void startContributorFlow(Contributor user) {

    }
}
