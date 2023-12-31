//package org.example;
import org.json.simple.JSONArray;

import java.util.*;

public abstract class Staff<T extends Comparable<T>> extends User<T> implements StaffInterface {
    public List<Request> userRequests;
    public SortedSet<Comparable> contributions;
    public Staff(Information info, AccountType type, String username, Integer experience) {
        super(info, type, username, experience);
        this.userRequests = new ArrayList<>();
        this.contributions = new TreeSet<>(new NameComparator());
    }

    private class NameComparator implements Comparator<Comparable> {
        @Override
        public int compare(Comparable o1, Comparable o2) {
            String name1 = getName(o1);
            String name2 = getName(o2);
            return name1.compareTo(name2);
        }

        private String getName(Comparable obj) {
            if (obj instanceof Actor) {
                return ((Actor) obj).name;
            } else if (obj instanceof Production) {
                return ((Production) obj).title;
            }
            throw new IllegalArgumentException("Object is not an Actor or Production");
        }
    }

    @Override
    public void addProductionSystem(String name) {
        // add a production to the system
        IMDB imdb = IMDB.getInstance();
        Scanner scanner = new Scanner(System.in);

        String prodType;
        while(true) {
            imdb.userInterface.displayOutput("Enter the type of the production (movie or series): ");
            prodType = scanner.nextLine();
            if(prodType.equalsIgnoreCase("movie") || prodType.equalsIgnoreCase("series")) {
                break;
            }
            imdb.userInterface.displayOutput("Invalid type. Please enter movie or series.");
        }

        if(prodType.equalsIgnoreCase("movie")) {
            imdb.userInterface.displayOutput("Enter the duration of the movie: ");
            int duration = scanner.nextInt();
            imdb.userInterface.displayOutput("Enter the release year of the movie: ");
            int releaseYear = scanner.nextInt();
            scanner.nextLine();
            imdb.userInterface.displayOutput("Enter the description of the movie: ");
            String description = scanner.nextLine();
            ArrayList<String> directors = new ArrayList<>();
            while(true) {
                //gather information about the directors
                imdb.userInterface.displayOutput("Enter the names of the directors of the movie: ");
                String directorName = scanner.nextLine();
                directors.add(directorName);
                imdb.userInterface.displayOutput("Do you want to add another director? (y/n): ");
                String choice = scanner.nextLine();
                if(choice.equalsIgnoreCase("n")) {
                    break;
                }
            }
            ArrayList<String> actors = new ArrayList<>();
            while(true) {
                //gather information about the actors
                imdb.userInterface.displayOutput("Enter the names of the actors of the movie: ");
                String actorName = scanner.nextLine();
                actors.add(actorName);
                imdb.userInterface.displayOutput("Do you want to add another actor? (y/n): ");
                String choice = scanner.nextLine();
                if(choice.equalsIgnoreCase("n")) {
                    break;
                }
            }
            ArrayList<Genre> genres = new ArrayList<>();
            while(true) {
                //gather information about the genres
                imdb.userInterface.displayOutput("Enter the genre of the movie: ");
                String genreName = scanner.nextLine();
                Genre genre = Genre.valueOf(genreName.toUpperCase());
                genres.add(genre);
                imdb.userInterface.displayOutput("Do you want to add another genre? (y/n): ");
                String choice = scanner.nextLine();
                if(choice.equalsIgnoreCase("n")) {
                    break;
                }
            }

            Movie movie = new Movie(name, directors, actors, genres, new ArrayList<>(), description, duration, releaseYear);
            imdb.productions.add(movie);
            this.contributions.add(movie);
            Parser.addMovie(movie);
            Parser.updateContributions(this);

        } else if (prodType.equalsIgnoreCase("series")) {
//            Same thing for series but respects Series attributes
            imdb.userInterface.displayOutput("Enter the release year of the series: ");
            int releaseYear = scanner.nextInt();
            scanner.nextLine();
            imdb.userInterface.displayOutput("Enter the number of seasons: ");
            int numberOfSeasons = scanner.nextInt();
            scanner.nextLine();
            imdb.userInterface.displayOutput("Enter the description of the series: ");
            String description = scanner.nextLine();
            scanner.nextLine();

            Map<String, List<Episode>> episodes = new HashMap<>();
            for(int seasonNumber = 1; seasonNumber <= numberOfSeasons; seasonNumber++) {
                List<Episode> seasonEpisodes = new ArrayList<>();
                imdb.userInterface.displayOutput("Enter the number of episodes for Season " + seasonNumber + ": ");
                int numEpisodes = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                for (int episodeNumber = 1; episodeNumber <= numEpisodes; episodeNumber++) {
                    imdb.userInterface.displayOutput("Enter the title of Episode " + episodeNumber + " for Season " + seasonNumber + ": ");
                    String episodeTitle = scanner.nextLine();
                    imdb.userInterface.displayOutput("Enter the duration of Episode " + episodeNumber + " for Season " + seasonNumber + " (in minutes): ");
                    int episodeDuration = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    Episode episode = new Episode(episodeTitle, episodeDuration);
                    seasonEpisodes.add(episode);
                }

                episodes.put("Season " + seasonNumber, seasonEpisodes);
            }

            ArrayList<String> directors = new ArrayList<>();
            while(true) {
                //gather information about the directors
                imdb.userInterface.displayOutput("Enter the names of the directors of the series: ");
                String directorName = scanner.nextLine();
                directors.add(directorName);
                imdb.userInterface.displayOutput("Do you want to add another director? (y/n): ");
                String choice = scanner.nextLine();
                if(choice.equalsIgnoreCase("n")) {
                    break;
                }
            }

            ArrayList<String> actors = new ArrayList<>();
            while(true) {
                //gather information about the actors
                imdb.userInterface.displayOutput("Enter the names of the actors of the series: ");
                String actorName = scanner.nextLine();
                actors.add(actorName);
                imdb.userInterface.displayOutput("Do you want to add another actor? (y/n): ");
                String choice = scanner.nextLine();
                if(choice.equalsIgnoreCase("n")) {
                    break;
                }
            }

            ArrayList<Genre> genres = new ArrayList<>();
            while(true) {
                //gather information about the genres
                imdb.userInterface.displayOutput("Enter the genre of the series: ");
                String genreName = scanner.nextLine();
                Genre genre = Genre.valueOf(genreName.toUpperCase());
                genres.add(genre);
                imdb.userInterface.displayOutput("Do you want to add another genre? (y/n): ");
                String choice = scanner.nextLine();
                if(choice.equalsIgnoreCase("n")) {
                    break;
                }
            }

            Series series = new Series(name, directors, actors, genres, new ArrayList<>(), "", releaseYear, numberOfSeasons, episodes);
            imdb.productions.add(series);
            this.contributions.add(series);
            Parser.addSeries(series);
            Parser.updateContributions(this);
        }
    }

    @Override
    public void addActorSystem(String name) {
        IMDB imdb = IMDB.getInstance();
        Scanner scanner = new Scanner(System.in);
        Actor actor = new Actor(name, new ArrayList<>(), null);
        List<Actor.Pair<String, Actor.Type>> filmography = actor.filmography;
        imdb.userInterface.displayOutput("Enter pairs of name:type of production in which the actor starred: ");
        while(true) {
            imdb.userInterface.displayOutput("Enter the name of the production: ");
            String productionName = scanner.nextLine();
            imdb.userInterface.displayOutput("Enter the type of the production (movie or series): ");
            String productionType = scanner.nextLine();
            Actor.Type type = Actor.Type.valueOf(productionType.toUpperCase());
            filmography.add(new Actor.Pair<>(productionName, type));
            imdb.userInterface.displayOutput("Do you want to add another production? (y/n): ");
            String choice = scanner.nextLine();
            if(choice.equalsIgnoreCase("n")) {
                break;
            }
        }

        imdb.userInterface.displayOutput("Enter the biography of the actor: ");
        String biography = scanner.nextLine();
        actor.biography = biography;

        imdb.actors.add(actor);
        this.contributions.add(actor);
        Parser.addActor(actor);
        Parser.updateContributions(this);

    }

    @Override
    public void removeProductionSystem(Production p) {
//         remove a production from the system
        IMDB imdb = IMDB.getInstance();
        imdb.productions.remove(p);
        this.contributions.remove(p);
        Parser.removeProduction(p);
        Parser.updateContributions(this);
    }

    @Override
    public void removeActorSystem(Actor a) {
        // remove an actor from the system
        IMDB imdb = IMDB.getInstance();
        imdb.actors.remove(a);
        this.contributions.remove(a);
        Parser.removeActor(a);
        Parser.updateContributions(this);
    }

    @Override
    public void updateProduction(Staff user, String title) {
        IMDB imdb = IMDB.getInstance();
        if (title == null) {
            imdb.userInterface.displayOutput("Enter the production title: ");
            title = imdb.userInterface.getInput();
        }
        Production p = null;
        for (Production prod : imdb.productions) {
            if (prod.title.equals(title)) {
                p = prod;
            }
        }
        if (p == null) {
            imdb.userInterface.displayOutput("No production found with the given title.\n");
            return;
        }
//            if the user is a contributor then he can only update a production that he added
        if (user instanceof Contributor) {
            if (!((Contributor) user).contributions.contains(p)) {
                imdb.userInterface.displayOutput("You can only update productions that you added.\n");
                return;
            }
        }
        int option;
        if (p instanceof Movie) {
            option = Options.updateInfoMovieChoices();
            switch (option) {
                case 1:
                    Update.updateMovieTitle((Movie) p);
                    break;
                case 2:
                    Update.updateMovieDescription((Movie) p);
                    break;
                case 3:
                    Update.updateMovieDuration((Movie) p);
                    break;
                case 4:
                    Update.updateMovieReleaseDate((Movie) p);
                    break;
                case 5:
                    Update.updateMovieGenre((Movie) p);
                    break;
                case 6:
                    Update.updateMovieActors((Movie) p);
                    break;
                case 7:
                    Update.updateMovieDirectors((Movie) p);
                    break;
                case 8:
                    if (user instanceof Contributor) {
                        Flow.startContributorFlow((Contributor) user);
                    } else if (user instanceof Admin) {
                        Flow.startAdminFlow((Admin) user);
                    }
            }
        } else {
            option = Options.updateInfoSeriesChoices();
            switch (option) {
                case 1:
                    Update.updateSeriesTitle((Series) p);
                    break;
                case 2:
                    Update.updateSeriesDescription((Series) p);
                    break;
                case 3:
                    Update.updateSeriesReleaseYear((Series) p);
                    break;
                case 4:
                    Update.updateSeriesGenre((Series) p);
                    break;
                case 5:
                    Update.updateSeriesActors((Series) p);
                    break;
                case 6:
                    Update.updateSeriesDirectors((Series) p);
                    break;
                case 7:
                    Update.updateSeriesNrSeasons((Series) p);
                    break;
                case 8:
                    Update.updateSeriesSeasons((Series) p);
                    break;
                case 9:
                    if (user instanceof Contributor) {
                        Flow.startContributorFlow((Contributor) user);
                    } else if (user instanceof Admin) {
                        Flow.startAdminFlow((Admin) user);
                    }
            }
        }
    }

    @Override
    public void updateActor(Staff user, String title) {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Enter the actor name: ");
        String input = imdb.userInterface.getInput();
        Actor a = null;
        for (Actor actor : imdb.actors) {
            if (actor.name.equals(input)) {
                a = actor;
            }
        }
        if (a == null) {
            imdb.userInterface.displayOutput("No actor found with the given name.\n");
            return;
        }
//            if the user is a contributor then he can only update an actor that he added
        if (user instanceof Contributor) {
            if (!((Contributor) user).contributions.contains(a)) {
                imdb.userInterface.displayOutput("You can only update actors that you added.\n");
                return;
            }
        }
        int option = Options.updateInfoActorChoices();
        switch (option) {
            case 1:
                Update.updateActorName(a);
                break;
            case 2:
                Update.updateActorBio(a);
                break;
            case 3:
//               display all actor performances and remember the number the user chooses
                Update.updateActorPerformance(a);
                break;
            case 4:
                if (user instanceof Contributor) {
                    Flow.startContributorFlow((Contributor) user);
                } else if (user instanceof Admin) {
                    Flow.startAdminFlow((Admin) user);
                }
        }
    }

    public void solveRequest(Request r) {
        IMDB imdb = IMDB.getInstance();
        imdb.userInterface.displayOutput("Choose option: \n");
        imdb.userInterface.displayOutput("\t1) Delete request\n");
        imdb.userInterface.displayOutput("\t2) Mark request as solved\n");
//        exit
        imdb.userInterface.displayOutput("\t3) Exit\n");
        int option = ((TerminalUI) imdb.userInterface).getNumber();
        if (option == 1) {
//            TODO: notify the user that the request was deleted
            if (this instanceof Contributor) {
                ((Contributor) this).removeRequest(r);
            } else if (this instanceof Admin) {
                ((Admin) this).removeRequest(r);
            }
            imdb.requests.remove(r);
            Parser.writeRequests();
            Parser.updateLists();
        } else if (option == 2) {
            if (this instanceof Contributor) {
                ((Contributor) this).removeRequest(r);
            } else if (this instanceof Admin) {
                ((Admin) this).removeRequest(r);
            }
            imdb.requests.remove(r);
            Parser.writeRequests();
            Parser.updateLists();
//            TODO: notify user that the request was solved
            this.solveRequest(r);
        } else {
            return;
        }
    }
}