import java.awt.*;

public class Flow {
    public static void startRegularFlow(Regular user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.regularOptions();
        switch (choice) {
            case 1:
                Choices.viewProductionDetails();
                break;
            case 2:
                Choices.viewActorDetails();
                break;
            case 3:
                Choices.viewNotifications(user);
                break;
            case 4:
                Choices.searchForActorMovieSeries();
                break;
            case 5:
                Choices.addDeleteFavourites(user);
                break;
            case 6:
                Choices.addDeleteRequest(user);
                break;
            case 7:
                Choices.addDeleteRating(user);
                break;
            case 8:
                user.logout();
                int option = Options.startOptions();
                if(option == 1) {
                    imdb.restartApp();
                }
                else {
                    System.exit(0);
                }

        }
        startRegularFlow(user);
    }

    public static void startAdminFlow(Admin user) {

    }

    public static void startContributorFlow(Contributor user) {

    }
}
