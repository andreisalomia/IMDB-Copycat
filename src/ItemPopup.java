import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ItemPopup {

    public static void showItemPopup(Actor actor, ImageIcon actorIcon, User user) {
        String actorInfo = getActorInformation(actor);
        showInfoPopup(actorInfo, actorIcon, user, actor);
    }

    public static void showItemPopup(Movie movie, ImageIcon movieIcon, User user) {
        String movieInfo = getMovieInformation(movie);
        showInfoPopup(movieInfo, movieIcon, user, movie);
    }

    public static void showItemPopup(Series series, ImageIcon seriesIcon, User user) {
        String seriesInfo = getSeriesInformation(series);
        showInfoPopup(seriesInfo, seriesIcon, user, series);
    }

    private static void showInfoPopup(String itemInfo, ImageIcon itemIcon, User user, Object item) {
        JTextArea textArea = new JTextArea(itemInfo);
        textArea.setEditable(false);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JPanel buttonPanel = createButtonPanel(user, item);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(itemIcon), BorderLayout.WEST);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(null, panel, "Item Information", JOptionPane.PLAIN_MESSAGE);
    }

    private static JPanel createButtonPanel(User user, Object item) {
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton addToFavoritesButton = new JButton("Add to Favorites");
        addToFavoritesButton.addActionListener(e -> {
            if (item instanceof Actor) {
                Actor actor = (Actor) item;
                addActorToFavorites(user, actor);
            } else if (item instanceof Production) {
                Production production = (Production) item;
                addProductionToFavorites(user, production);
            }
            JOptionPane.showMessageDialog(null, "Added to Favorites!");
        });
        buttonPanel.add(addToFavoritesButton);

        if (user instanceof Regular) {
            JButton reviewButton = new JButton("Review");
            reviewButton.addActionListener(e -> addReview(user, item));
            buttonPanel.add(reviewButton);
        }

        Admin admin = null;
        IMDB imdb = IMDB.getInstance();
        for(User user1 : imdb.users) {
            if(user1.username.equals("admin")) {
                admin = (Admin) user1;
                break;
            }
        }

        if (user instanceof Admin  || (user instanceof Contributor && ((Contributor<?>) user).contributions.contains(item))) {
            JButton updateButton = new JButton("Update");
            updateButton.addActionListener(e -> {
                if (item instanceof Actor) {
                    chooseUpdateActor((Actor) item);
                } else if (item instanceof Production) {
                    if(item instanceof Movie) {
                        chooseUpdateMovie((Movie) item);
                    } else if(item instanceof Series) {
                        chooseUpdateSeries((Series) item);
                    }
                }
            });
            buttonPanel.add(updateButton);
        }

        return buttonPanel;
    }

    private static void chooseUpdateSeries(Series item) {
        Object[] options = {"Update Title", "Update Description", "Update Release Year", "Update Genres",
                "Update Actors", "Update Directors", "Update Number of Seasons", "Update Episodes"};

        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose an update option:",
                "Update Series",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice != -1) {
            switch (choice) {
                case 0:
                    Update.updateSeriesTitleGUI(item);
                    break;
                case 1:
                    Update.updateSeriesDescriptionGUI(item);
                    break;
                case 2:
                    Update.updateSeriesReleaseYearGUI(item);
                    break;
                case 3:
                    Update.updateSeriesGenreGUI(item);
                    break;
                case 4:
                    Update.updateSeriesActorsGUI(item);
                    break;
                case 5:
                    Update.updateSeriesDirectorsGUI(item);
                    break;
                case 6:
                    Update.updateSeriesNrSeasonsGUI(item);
                    break;
                case 7:
                    Update.updateSeriesSeasonsGUI(item);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option selected");
            }
        }
    }


    private static void chooseUpdateActor(Actor item) {
        String[] options = {"Name", "Biography", "Filmography"};
        int choice = JOptionPane.showOptionDialog(null, "What do you want to update?", "Update Actor", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0:
                Update.updateActorNameGUI(item);
                break;
            case 1:
                Update.updateActorBioGUI(item);
                break;
            case 2:
                Update.updateActorPerformanceGUI(item);
                break;
        }
    }

    private static void chooseUpdateMovie(Movie item) {
        String[] options = {
                "Update Title",
                "Update Description",
                "Update Duration",
                "Update Release Date",
                "Update Genre",
                "Update Actors",
                "Update Directors"
        };

        int choice = JOptionPane.showOptionDialog(
                null,
                "Choose an update option:",
                "Update Movie",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choice != -1) {
            switch (choice) {
                case 0:
                    Update.updateMovieTitleGUI(item);
                    break;
                case 1:
                    Update.updateMovieDescriptionGUI(item);
                    break;
                case 2:
                    Update.updateMovieDurationGUI(item);
                    break;
                case 3:
                    Update.updateMovieReleaseDateGUI(item);
                    break;
                case 4:
                    Update.updateMovieGenreGUI(item);
                    break;
                case 5:
                    Update.updateMovieActorsGUI(item);
                    break;
                case 6:
                    Update.updateMovieDirectorsGUI(item);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid option selected");
            }
        }
    }


    private static void addReview(User user, Object item) {
        String productionTitle = null;

        if(user instanceof Contributor && item instanceof Production) {
            Production production = (Production) item;
            if(((Contributor<?>) user).contributions.contains(production)) {
                JOptionPane.showMessageDialog(null, "You can't review a production that you contributed to!");
                return;
            }
        }

        if (item instanceof Production) {
            Production production = (Production) item;
            productionTitle = production.title;
        }

        if (productionTitle != null) {
            JTextField ratingField = new JTextField();
            JTextField messageField = new JTextField();

            Object[] fields = {"Rating:", ratingField, "Message:", messageField};

            int option = JOptionPane.showConfirmDialog(null, fields, "Enter Review Information", JOptionPane.OK_CANCEL_OPTION);

            if (option == JOptionPane.OK_OPTION) {
                try {
                    int rating = Integer.parseInt(ratingField.getText());
                    String message = messageField.getText();

                    if (rating < 1 || rating > 10) {
                        JOptionPane.showMessageDialog(null, "Invalid rating. Please enter a rating between 1 and 10.");
                        return;
                    }

                    Rating newRating = new Rating(user.username, rating, message);

                    if (item instanceof Production) {
                        Production production = (Production) item;
                        Regular regular = (Regular) user;
                        regular.addRating(newRating, production);
                        JOptionPane.showMessageDialog(null, "Review added!");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the rating.");
                }
            }
        }
    }

    private static void addActorToFavorites(User user, Actor actor) {
        IMDB imdb = IMDB.getInstance();
        if (Filter.actorCheckExists(user, actor.name)) {
            imdb.userInterface.displayOutput("Actor already exists in the list.\n");
            return;
        }
        user.addActorToFavorites(actor);
    }

    private static void addProductionToFavorites(User user, Production production) {
        IMDB imdb = IMDB.getInstance();
        if (Filter.productionCheckExists(user, production.title)) {
            imdb.userInterface.displayOutput("Production already exists in the list.\n");
            return;
        }
        user.addProductionToFavorites(production);
    }

    private static String getActorInformation(Actor actor) {
        StringBuilder builder = new StringBuilder();
        builder.append("Actor: ").append(actor.name).append("\n");

        if (actor.biography != null)
            builder.append("Biography: ").append(actor.biography).append("\n");

        if (actor.filmography != null && !actor.filmography.isEmpty()) {
            builder.append("Filmography:\n");
            for (Actor.Pair<String, Actor.Type> entry : actor.filmography) {
                builder.append("\t").append(entry.name).append(" (").append(entry.type).append(")\n");
            }
        }

        return builder.toString();
    }

    private static String getMovieInformation(Movie movie) {
        StringBuilder builder = new StringBuilder();
        builder.append("Title: ").append(movie.title).append("\n");

        builder.append("Type: Movie\n");
        if (movie.directors != null) builder.append("Directors: ").append(movie.directors).append("\n");
        if (movie.actors != null) builder.append("Actors: ").append(movie.actors).append("\n");
        if (movie.genres != null) builder.append("Genres: ").append(movie.genres).append("\n");

        if (movie.ratings != null && !movie.ratings.isEmpty()) {
            builder.append("Ratings:\n");
            for (Rating rating : movie.ratings) {
                builder.append(String.format("%s - %s (%d)\n", rating.username, rating.review, rating.rating));
            }
        }

        if (movie.description != null) builder.append("Description: ").append(movie.description).append("\n");
        if (movie.duration != 0) builder.append("Duration: ").append(movie.duration).append("\n");
        if (movie.releaseYear != 0) builder.append("Release Year: ").append(movie.releaseYear).append("\n");
        if (movie.averageRating != 0) builder.append("Average Rating: ").append((movie.averageRating)).append("\n");

        return builder.toString();
    }

    private static String getSeriesInformation(Series series) {
        StringBuilder builder = new StringBuilder();
        builder.append("Title: ").append(series.title).append("\n");
        builder.append("Type: Series\n");

        if (series.directors != null)
            builder.append("Directors: ").append(series.directors).append("\n");

        if (series.actors != null)
            builder.append("Actors: ").append(series.actors).append("\n");

        if (series.genres != null)
            builder.append("Genres: ").append(series.genres).append("\n");

        if (series.ratings != null)
            builder.append(formatRatings("Ratings", series.ratings)).append("\n");

        if (series.description != null)
            builder.append("Description: ").append(series.description).append("\n");

        if (series.releaseYear != 0)
            builder.append("Release Year: ").append(series.releaseYear).append("\n");

        if (series.numberOfSeasons != 0)
            builder.append("Number of Seasons: ").append(series.numberOfSeasons).append("\n");

        if (series.averageRating != 0)
            builder.append("Average Rating: ").append((series.averageRating)).append("\n");

        return builder.toString();
    }

    private static String formatRating(Rating rating) {
        return String.format("%s - %s", rating.username, rating.review);
    }

    private static String formatRatings(String label, List<Rating> ratings) {
        StringBuilder builder = new StringBuilder(label).append(": ");
        for (Rating rating : ratings) {
            builder.append(formatRating(rating)).append(" ");
        }
        return builder.toString().trim();
    }

}
