//package org.example;
import java.util.ArrayList;
import java.util.List;

public class Update {
    public static void updateActorName(Actor a) {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Enter new actor name: ");
        String input = imdb.userInterface.getInput();
        a.name = input;
        Parser.writeActors(imdb.actors);
        Parser.updateLists();
    }

    public static void updateActorBio(Actor a) {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Enter new actor biography: ");
        String input = imdb.userInterface.getInput();
        a.biography = input;
        Parser.writeActors(imdb.actors);
        Parser.updateLists();
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
//                    get input
        int modify = ((TerminalUI) imdb.userInterface).getNumber();
        if (modify == 1) {
            imdb.userInterface.displayOutput("Enter new title: ");
            input = imdb.userInterface.getInput();
            a.filmography.get(performanceNumber - 1).name = input;
            Parser.writeActors(imdb.actors);
            Parser.updateLists();
        } else if (modify == 2) {
            imdb.userInterface.displayOutput("Enter new type: ");
            input = imdb.userInterface.getInput().toUpperCase();
//                        convert string to Actor.Type which is an enum
            a.filmography.get(performanceNumber - 1).type = Actor.Type.valueOf(input);
            Parser.writeActors(imdb.actors);
            Parser.updateLists();
        }
    }

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

}