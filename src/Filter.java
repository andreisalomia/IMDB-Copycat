import java.util.*;

public class Filter {
    public static void filterProductions(IMDB imdb) {
        Set<Production> displayedProductions = new HashSet<>(IMDB.getInstance().productions);
        Set<Production> helper = new HashSet<>();
        filterProductionsFunction(imdb, helper, displayedProductions);
    }

    public static void filterProductionsFunction(IMDB imdb, Set<Production> helper, Set<Production> displayedProductions) {
        int filterChoice = Options.filterProductionsOptions(imdb);
        Scanner scanner = new Scanner(System.in);
        helper.clear();

        if (filterChoice == 1) {
            imdb.userInterface.displayOutput("Enter the Genre: ");
            String genreInput = scanner.nextLine();
            Genre genre = getGenre(genreInput);
            if (genre == null) {
                imdb.userInterface.displayOutput("Genre not found. Please try again.\n");
                filterProductions(imdb);
            } else {
                for (Production prod : imdb.productions) {
                    for(Production prod2 : displayedProductions) {
                        if (prod.genres.contains(genre) && prod2.genres.contains(genre)) {
                            helper.add(prod2);
                        }
                    }
                }
                displayedProductions.clear();
                displayedProductions.addAll(helper);
            }
        }
        if(filterChoice == 2) {
            imdb.userInterface.displayOutput("Enter the number of ratings: ");
            int numberOfRatings = ((TerminalUI) imdb.userInterface).getNumber();
            boolean validInput = false;
            for(Production prod2 : displayedProductions) {
                    if (prod2.ratings.size() >= numberOfRatings) {
                        helper.add(prod2);
                        validInput = true;
                    }
            }
            if (!validInput) {
                imdb.userInterface.displayOutput("No productions found with the given number of ratings.\n");
            }
            displayedProductions.clear();
            displayedProductions.addAll(helper);
        }
        if (filterChoice == 3) {
            displayedProductions.clear();
            helper.clear();
            return;
        }
        if (filterChoice == 4) {
            filterProductions(imdb);
        }
        if (filterChoice == 5) {
            imdb.userInterface.displayOutput("Enter the title of the production: ");
            String title = scanner.nextLine();
            boolean found = false;

            for (Production prod : displayedProductions) {
                if (prod.title.equalsIgnoreCase(title)) {
                    // Display details of the production
                    prod.displayInfo();
                    found = true;
                    break; // Stop searching once a match is found
                }
            }

            if (!found) {
                imdb.userInterface.displayOutput("No productions found with the given title.\n");
            }
            return;
        }
        for(Production prod : displayedProductions) {
            imdb.userInterface.displayOutput(prod.title + "\n");
        }
        filterProductionsFunction(imdb, helper, displayedProductions);
    }

    public static void listAllProductions(IMDB imdb) {
        for(Production production : imdb.productions) {
            imdb.userInterface.displayOutput(production.title + "\n");
        }
        imdb.userInterface.displayOutput("Enter the title of the production: ");
        Scanner scanner = new Scanner(System.in);
        String title = scanner.nextLine();
        boolean found = false;

        for (Production prod : imdb.productions) {
            if (prod.title.equalsIgnoreCase(title)) {
                // Display details of the production
                prod.displayInfo();
                found = true;
                break; // Stop searching once a match is found
            }
        }
        if (!found) {
            imdb.userInterface.displayOutput("No productions found with the given title.\n");
        }
    }

    public static void listAllActors(IMDB imdb) {
        listActors(imdb.actors, imdb);
    }

    public static void listActorsAlphabetically(IMDB imdb) {
        // Sort the actors alphabetically
        List<Actor> actors = new ArrayList<>(imdb.actors);
        Collections.sort(actors);
        listActors(actors, imdb);
    }

    public static void listActors(List<Actor> actors, IMDB imdb) {
        for(Actor actor : actors) {
            imdb.userInterface.displayOutput(actor.name + "\n");
        }
        imdb.userInterface.displayOutput("Enter the name of the actor: ");
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        boolean found = false;

        for (Actor actor : actors) {
            if (actor.name.equalsIgnoreCase(name)) {
                // Display details of the actor
                actor.displayInfo();
                found = true;
                break; // Stop searching once a match is found
            }
        }

        if (!found) {
            imdb.userInterface.displayOutput("No actors found with the given name.\n");
        }
    }
    private static Genre getGenre(String genreInput) {
        try {
            return Genre.valueOf(genreInput.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // Genre not found in the enum
        }
    }
}

