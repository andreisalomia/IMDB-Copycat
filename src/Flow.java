//package org.example;
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
                Choices.searchForActorMovieSeries(user);
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
        }
        startRegularFlow(user);
    }

    public static void startAdminFlow(Admin user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.adminOptions();
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
                Choices.searchForActorMovieSeries(user);
                break;
            case 5:
                Choices.addDeleteFavourites(user);
                break;
            case 6:
                Choices.addDeleteActorProduction(user);
                break;
            case 7:
                Choices.viewSolveRequests(user);
                break;
            case 8:
                Choices.updateInfoActorProduction(user);
                break;
            case 9:
                Choices.addDeleteUser(user);
                break;
            case 10:
                user.logout();
        }
        startAdminFlow(user);
    }

    public static void startContributorFlow(Contributor user) {
        IMDB imdb = IMDB.getInstance();
        int choice = Options.contributorOptions();
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
                Choices.searchForActorMovieSeries(user);
                break;
            case 5:
                Choices.addDeleteFavourites(user);
                break;
            case 6:
                Choices.addDeleteRequest(user);
                break;
            case 7:
                Choices.addDeleteActorProduction(user);
                break;
            case 8:
                Choices.viewSolveRequestsContributor(user);
                break;
            case 9:
                Choices.updateInfoActorProduction(user);
                break;
            case 10:
                user.logout();
        }
        startContributorFlow(user);
    }
}
