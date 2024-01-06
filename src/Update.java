//package org.example;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Update {

    public static void updateMovieTitle(Movie p) {
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production title: ");
        input = imdb.userInterface.getInput();
        p.title = input;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateMovieDescription(Movie p) {
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production description: ");
        input = imdb.userInterface.getInput();
        p.description = input;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    //    duration
    public static void updateMovieDuration(Movie p) {
        IMDB imdb = IMDB.getInstance();
        int input;
        imdb.userInterface.displayOutput("Enter new production duration: ");
        int duration = ((TerminalUI) imdb.userInterface).getNumber();
        if (duration < 1) {
            imdb.userInterface.displayOutput("Invalid duration.\n");
            return;
        }
        p.duration = duration;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateMovieReleaseDate(Movie p) {
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production release date: ");
        input = imdb.userInterface.getInput();
//                        convert input to number
        p.releaseYear = Integer.parseInt(input);
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateMovieGenre(Movie p) {
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production genres: ");
        List<Genre> genres = new ArrayList<>();
//                        Ask the user to insert a genre after another after each genre inserted the user must type y/n
//                        Also tell the user what to do
        boolean check = true;
        while (check) {
            imdb.userInterface.displayOutput("Enter genre: ");
            input = imdb.userInterface.getInput().toUpperCase();
            genres.add(Genre.valueOf(input));
            imdb.userInterface.displayOutput("Do you want to add another genre? (y/n) ");
            input = imdb.userInterface.getInput().toLowerCase();
            if (input.equals("n")) {
                check = false;
            }
        }
        p.genres = genres;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateMovieActors(Movie p) {
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production actors: ");
        List<String> actors = new ArrayList<>();
//                        Ask the user to insert an actor after another after each actor inserted the user must type y/n
//                        Also tell the user what to do
        boolean check = true;
        while (check) {
            imdb.userInterface.displayOutput("Enter actor: ");
            input = imdb.userInterface.getInput();
            actors.add(input);
            imdb.userInterface.displayOutput("Do you want to add another actor? (y/n) ");
            input = imdb.userInterface.getInput().toLowerCase();
            if (input.equals("n")) {
                check = false;
            }
        }
        p.actors = actors;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }


    public static void updateMovieDirectors(Movie p) {
        //                        update movie directors
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production directors: ");
        List<String> directors = new ArrayList<>();
//                        Ask the user to insert a director after another after each director inserted the user must type y/n
//                        Also tell the user what to do
        boolean check = true;
        while (check) {
            imdb.userInterface.displayOutput("Enter director: ");
            input = imdb.userInterface.getInput();
            directors.add(input);
            imdb.userInterface.displayOutput("Do you want to add another director? (y/n) ");
            input = imdb.userInterface.getInput().toLowerCase();
            if (input.equals("n")) {
                check = false;
            }
        }
        p.directors = directors;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateSeriesTitle(Series p) {
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production title: ");
        input = imdb.userInterface.getInput();
        p.title = input;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateSeriesDescription(Series p) {
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production description: ");
        input = imdb.userInterface.getInput();
        p.description = input;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateSeriesReleaseYear(Series p) {
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production release year: ");
        input = imdb.userInterface.getInput();
        p.releaseYear = Integer.parseInt(input);
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateSeriesGenre(Series p) {
        //                        genres
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production genres: ");
        List<Genre> genres = new ArrayList<>();
//                        Ask the user to insert a genre after another after each genre inserted the user must type y/n
//                        Also tell the user what to do
        boolean check = true;
        while (check) {
            imdb.userInterface.displayOutput("Enter genre: ");
            input = imdb.userInterface.getInput().toUpperCase();
            genres.add(Genre.valueOf(input));
            imdb.userInterface.displayOutput("Do you want to add another genre? (y/n) ");
            input = imdb.userInterface.getInput().toLowerCase();
            if (input.equals("n")) {
                check = false;
            }
        }
        ((Series) p).genres = genres;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateSeriesActors(Series p) {
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production actors: ");
        List<String> actors = new ArrayList<>();
//                        Ask the user to insert an actor after another after each actor inserted the user must type y/n
//                        Also tell the user what to do
        boolean check = true;
        while (check) {
            imdb.userInterface.displayOutput("Enter actor: ");
            input = imdb.userInterface.getInput();
            actors.add(input);
            imdb.userInterface.displayOutput("Do you want to add another actor? (y/n) ");
            input = imdb.userInterface.getInput().toLowerCase();
            if (input.equals("n")) {
                check = false;
            }
        }
        ((Series) p).actors = actors;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateSeriesDirectors(Series p) {
        //                        directors
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Enter new production directors: ");
        List<String> directors = new ArrayList<>();
//                        Ask the user to insert a director after another after each director inserted the user must type y/n
//                        Also tell the user what to do
        boolean check = true;
        while (check) {
            imdb.userInterface.displayOutput("Enter director: ");
            input = imdb.userInterface.getInput();
            directors.add(input);
            imdb.userInterface.displayOutput("Do you want to add another director? (y/n) ");
            input = imdb.userInterface.getInput().toLowerCase();
            if (input.equals("n")) {
                check = false;
            }
        }
        ((Series) p).directors = directors;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateSeriesNrSeasons(Series p) {
        //                        update the number of seasons
        IMDB imdb = IMDB.getInstance();
        int input;
        imdb.userInterface.displayOutput("Enter new number of seasons: ");
        int seasons = ((TerminalUI) imdb.userInterface).getNumber();
        if (seasons < 1) {
            imdb.userInterface.displayOutput("Invalid number of seasons.\n");
            return;
        }
        ((Series) p).numberOfSeasons = seasons;
        Parser.writeProductions(imdb.productions);
        Parser.updateLists();
    }

    public static void updateSeriesSeasons(Series p) {
        // Choose season
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Choose season: \n");
        for (int i = 0; i < ((Series) p).numberOfSeasons; i++) {
            imdb.userInterface.displayOutput("\t" + (i + 1) + ") Season " + (i + 1) + "\n");
        }
        int seasonNumber = ((TerminalUI) imdb.userInterface).getNumber();
        if (seasonNumber < 1 || seasonNumber > ((Series) p).numberOfSeasons) {
            imdb.userInterface.displayOutput("Invalid season number.\n");
            return;
        }

        // Choose episode
        imdb.userInterface.displayOutput("Choose episode: \n");
        List<Episode> episodesInSeason = ((Series) p).episodes.get("Season " + seasonNumber);
        for (int i = 0; i < episodesInSeason.size(); i++) {
            imdb.userInterface.displayOutput("\t" + (i + 1) + ") Episode " + episodesInSeason.get(i).title + "\n");
        }
        int episodeNumber = ((TerminalUI) imdb.userInterface).getNumber();
        if (episodeNumber < 1 || episodeNumber > episodesInSeason.size()) {
            imdb.userInterface.displayOutput("Invalid episode number.\n");
            return;
        }

        // Choose what to update: episode title or episode duration
        imdb.userInterface.displayOutput("Choose what you want to update: \n");
        imdb.userInterface.displayOutput("\t1) Episode title\n");
        imdb.userInterface.displayOutput("\t2) Episode duration\n");
        int update = ((TerminalUI) imdb.userInterface).getNumber();
        if (update == 1) {
            imdb.userInterface.displayOutput("Enter new episode title: ");
            input = imdb.userInterface.getInput();
            episodesInSeason.get(episodeNumber - 1).title = input;
            Parser.writeProductions(imdb.productions);
            Parser.updateLists();
        } else if (update == 2) {
            imdb.userInterface.displayOutput("Enter new episode duration: ");
            int duration = ((TerminalUI) imdb.userInterface).getNumber();
            if (duration < 1) {
                imdb.userInterface.displayOutput("Invalid duration.\n");
                return;
            }
            episodesInSeason.get(episodeNumber - 1).duration = duration;
            Parser.writeProductions(imdb.productions);
            Parser.updateLists();
        }
    }

    public static void updateSeriesTitleGUI(Series p) {
        String newTitle = JOptionPane.showInputDialog("Enter new production title:");
        if (newTitle != null) {
            p.title = newTitle;
            updateSeriesData(p);
        }
    }

    public static void updateSeriesDescriptionGUI(Series p) {
        String newDescription = JOptionPane.showInputDialog("Enter new production description:");
        if (newDescription != null) {
            p.description = newDescription;
            updateSeriesData(p);
        }
    }

    public static void updateSeriesReleaseYearGUI(Series p) {
        String newReleaseYear = JOptionPane.showInputDialog("Enter new production release year:");
        if (newReleaseYear != null) {
            try {
                p.releaseYear = Integer.parseInt(newReleaseYear);
                updateSeriesData(p);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the release year.");
            }
        }
    }

    public static void updateSeriesGenreGUI(Series p) {
        List<Genre> genres = new ArrayList<>();
        boolean check = true;
        while (check) {
            Object selectedGenre = JOptionPane.showInputDialog(null, "Choose genre:", "Genre Selection",
                    JOptionPane.QUESTION_MESSAGE, null, Genre.values(), Genre.values()[0]);

            if (selectedGenre != null && selectedGenre instanceof Genre) {
                genres.add((Genre) selectedGenre);
                int option = JOptionPane.showConfirmDialog(null, "Do you want to add another genre?", "Add Another Genre", JOptionPane.YES_NO_OPTION);
                check = (option == JOptionPane.YES_OPTION);
            } else {
                check = false;
            }
        }
        p.genres = genres;
        p.genres = genres;
        updateSeriesData(p);
    }

    public static void updateSeriesActorsGUI(Series p) {
        List<String> actors = new ArrayList<>();
        boolean check = true;
        while (check) {
            String input = JOptionPane.showInputDialog("Enter actor:");
            if (input != null) {
                actors.add(input);
                int option = JOptionPane.showConfirmDialog(null, "Do you want to add another actor?", "Add Another Actor", JOptionPane.YES_NO_OPTION);
                check = (option == JOptionPane.YES_OPTION);
            } else {
                check = false;
            }
        }
        p.actors = actors;
        updateSeriesData(p);
    }

    public static void updateSeriesDirectorsGUI(Series p) {
        List<String> directors = new ArrayList<>();
        boolean check = true;
        while (check) {
            String input = JOptionPane.showInputDialog("Enter director:");
            if (input != null) {
                directors.add(input);
                int option = JOptionPane.showConfirmDialog(null, "Do you want to add another director?", "Add Another Director", JOptionPane.YES_NO_OPTION);
                check = (option == JOptionPane.YES_OPTION);
            } else {
                check = false;
            }
        }
        p.directors = directors;
        updateSeriesData(p);
    }

    public static void updateSeriesNrSeasonsGUI(Series p) {
        int newSeasons = showSeasonsInputDialog();
        if (newSeasons >= 0) {
            p.numberOfSeasons = newSeasons;
            updateSeriesData(p);
        }
    }

    public static void updateSeriesSeasonsGUI(Series p) {
        int seasonNumber = showSeasonInputDialog("Choose season:", p.numberOfSeasons);
        if (seasonNumber >= 1 && seasonNumber <= p.numberOfSeasons) {
            int episodeNumber = showEpisodeInputDialog("Choose episode:", p.episodes.get("Season " + seasonNumber).size());
            if (episodeNumber >= 1 && episodeNumber <= p.episodes.get("Season " + seasonNumber).size()) {
                int updateChoice = showUpdateOptionDialog("Choose what you want to update:\n1) Episode title\n2) Episode duration");
                if (updateChoice == 1) {
                    String newEpisodeTitle = JOptionPane.showInputDialog("Enter new episode title:");
                    if (newEpisodeTitle != null) {
                        p.episodes.get("Season " + seasonNumber).get(episodeNumber - 1).title = newEpisodeTitle;
                        updateSeriesData(p);
                    }
                } else if (updateChoice == 2) {
                    int newEpisodeDuration = showDurationInputDialogEpisode();
                    if (newEpisodeDuration >= 0) {
                        p.episodes.get("Season " + seasonNumber).get(episodeNumber - 1).duration = newEpisodeDuration;
                        updateSeriesData(p);
                    }
                }
            }
        }
    }

    private static int showSeasonsInputDialog() {
        try {
            return Integer.parseInt(JOptionPane.showInputDialog("Enter new number of seasons:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the number of seasons.");
            return -1;
        }
    }

    private static int showSeasonInputDialog(String message, int maxSeasons) {
        try {
            return Integer.parseInt(JOptionPane.showInputDialog(message));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the season.");
            return -1;
        }
    }

    private static int showEpisodeInputDialog(String message, int maxEpisodes) {
        try {
            return Integer.parseInt(JOptionPane.showInputDialog(message));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the episode.");
            return -1;
        }
    }

    private static int showUpdateOptionDialog(String message) {
        return JOptionPane.showOptionDialog(
                null,
                message,
                "Update Option",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"Episode title", "Episode duration"},
                "Episode title"
        );
    }

    private static int showDurationInputDialogEpisode() {
        try {
            return Integer.parseInt(JOptionPane.showInputDialog("Enter new episode duration:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the episode duration.");
            return -1;
        }
    }

    private static void updateSeriesData(Series p) {
        Parser.writeProductions(IMDB.getInstance().productions);
        Parser.updateLists();
    }

    public static void updateActorName(Actor a) {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Enter new actor name: ");
        String input = imdb.userInterface.getInput();
        a.name = input;
        updateActorData(a);
    }

    public static void updateActorBio(Actor a) {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Enter new actor biography: ");
        String input = imdb.userInterface.getInput();
        a.biography = input;
        updateActorData(a);
    }

    public static void updateActorPerformance(Actor a) {
        IMDB imdb = IMDB.getInstance();
        String input;
        imdb.userInterface.displayOutput("Choose performance: \n");
        for (int i = 0; i < a.filmography.size(); i++) {
            imdb.userInterface.displayOutput("\t" + (i + 1) + ") " + a.filmography.get(i).name + " - " + a.filmography.get(i).type + "\n");
        }
        int performanceNumber = ((TerminalUI) imdb.userInterface).getNumber();
        if (performanceNumber < 1 || performanceNumber > a.filmography.size()) {
            imdb.userInterface.displayOutput("Invalid performance number.\n");
            return;
        }
        imdb.userInterface.displayOutput("Choose what you want to modify: \n");
        imdb.userInterface.displayOutput("\t1) Title\n");
        imdb.userInterface.displayOutput("\t2) Type\n");

        int modify = ((TerminalUI) imdb.userInterface).getNumber();
        if (modify == 1) {
            imdb.userInterface.displayOutput("Enter new title: ");
            input = imdb.userInterface.getInput();
            a.filmography.get(performanceNumber - 1).name = input;
            updateActorData(a);
        } else if (modify == 2) {
            imdb.userInterface.displayOutput("Enter new type: ");
            input = imdb.userInterface.getInput().toUpperCase();
            a.filmography.get(performanceNumber - 1).type = Actor.Type.valueOf(input);
            updateActorData(a);
        }
    }

    // Adapted functions for GUI input/output
    public static void updateActorNameGUI(Actor item) {
        String newName = JOptionPane.showInputDialog("Enter new actor name:");
        if (newName != null) {
            item.name = newName;
            updateActorData(item);
        }
    }

    public static void updateActorBioGUI(Actor item) {
        String newBio = JOptionPane.showInputDialog("Enter new actor biography:");
        if (newBio != null) {
            item.biography = newBio;
            updateActorData(item);
        }
    }

    public static void updateActorPerformanceGUI(Actor item) {
        String[] options = {"Title", "Type"};
        int performanceNumber = showPerformanceSelectionDialog(item);
        if (performanceNumber != -1) {
            int modify = JOptionPane.showOptionDialog(null, "Choose what you want to modify:", "Modify Option",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

            if (modify == 0) { // Title
                String newTitle = JOptionPane.showInputDialog("Enter new title:");
                if (newTitle != null) {
                    item.filmography.get(performanceNumber).name = newTitle;
                    updateActorData(item);
                }
            } else if (modify == 1) { // Type
                String newType = (String) JOptionPane.showInputDialog(null, "Choose new type:", "Type Selection",
                        JOptionPane.QUESTION_MESSAGE, null, Actor.Type.values(), Actor.Type.values()[0]);
                if (newType != null) {
                    item.filmography.get(performanceNumber).type = Actor.Type.valueOf(newType);
                    updateActorData(item);
                }
            }
        }
    }

    private static void updateActorData(Actor item) {
        Parser.writeActors(IMDB.getInstance().actors);
        Parser.updateLists();
    }

    private static int showPerformanceSelectionDialog(Actor item) {
        Object[] options = new Object[item.filmography.size()];
        for (int i = 0; i < item.filmography.size(); i++) {
            options[i] = item.filmography.get(i).name + " - " + item.filmography.get(i).type;
        }

        return JOptionPane.showOptionDialog(null, "Choose performance:", "Performance Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
    }

    public static void updateMovieTitleGUI(Movie p) {
        String newTitle = JOptionPane.showInputDialog("Enter new production title:");
        if (newTitle != null) {
            p.title = newTitle;
            updateMovieData(p);
        }
    }

    public static void updateMovieDescriptionGUI(Movie p) {
        String newDescription = JOptionPane.showInputDialog("Enter new production description:");
        if (newDescription != null) {
            p.description = newDescription;
            updateMovieData(p);
        }
    }

    public static void updateMovieDurationGUI(Movie p) {
        int newDuration = showDurationInputDialog();
        if (newDuration >= 0) {
            p.duration = newDuration;
            updateMovieData(p);
        }
    }

    public static void updateMovieReleaseDateGUI(Movie p) {
        String newReleaseDate = JOptionPane.showInputDialog("Enter new production release date:");
        if (newReleaseDate != null) {
            try {
                p.releaseYear = Integer.parseInt(newReleaseDate);
                updateMovieData(p);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the release date.");
            }
        }
    }

    public static void updateMovieGenreGUI(Movie p) {
        List<Genre> genres = new ArrayList<>();
        boolean check = true;
        while (check) {
            Object selectedGenre = JOptionPane.showInputDialog(null, "Choose genre:", "Genre Selection",
                    JOptionPane.QUESTION_MESSAGE, null, Genre.values(), Genre.values()[0]);

            if (selectedGenre != null && selectedGenre instanceof Genre) {
                genres.add((Genre) selectedGenre);
                int option = JOptionPane.showConfirmDialog(null, "Do you want to add another genre?", "Add Another Genre", JOptionPane.YES_NO_OPTION);
                check = (option == JOptionPane.YES_OPTION);
            } else {
                check = false;
            }
        }
        p.genres = genres;
        updateMovieData(p);
    }

    public static void updateMovieActorsGUI(Movie p) {
        List<String> actors = new ArrayList<>();
        boolean check = true;
        while (check) {
            String input = JOptionPane.showInputDialog("Enter actor:");
            if (input != null) {
                actors.add(input);
                int option = JOptionPane.showConfirmDialog(null, "Do you want to add another actor?", "Add Another Actor", JOptionPane.YES_NO_OPTION);
                check = (option == JOptionPane.YES_OPTION);
            } else {
                check = false;
            }
        }
        p.actors = actors;
        updateMovieData(p);
    }

    public static void updateMovieDirectorsGUI(Movie p) {
        List<String> directors = new ArrayList<>();
        boolean check = true;
        while (check) {
            String input = JOptionPane.showInputDialog("Enter director:");
            if (input != null) {
                directors.add(input);
                int option = JOptionPane.showConfirmDialog(null, "Do you want to add another director?", "Add Another Director", JOptionPane.YES_NO_OPTION);
                check = (option == JOptionPane.YES_OPTION);
            } else {
                check = false;
            }
        }
        p.directors = directors;
        updateMovieData(p);
    }

    private static int showDurationInputDialog() {
        try {
            return Integer.parseInt(JOptionPane.showInputDialog("Enter new production duration:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the duration.");
            return -1;
        }
    }

    private static void updateMovieData(Movie p) {
        Parser.writeProductions(IMDB.getInstance().productions);
        Parser.updateLists();
    }
}