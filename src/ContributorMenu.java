import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;
import java.util.List;

public class ContributorMenu {
    private static JFrame contributorMenuFrame;
    private static JPanel leftPanel;
    private static JPanel rightPanel;
    private static List<Genre> selectedGenres;
    private static int minimum_reviews = 0;
    private static DefaultTableModel model;

    public static void initializeMenu(Contributor user) {
        SwingUtilities.invokeLater(() -> {
            contributorMenuFrame = new JFrame("Contributor Menu");
            contributorMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            contributorMenuFrame.setSize(1024, 768);
            contributorMenuFrame.setLocationRelativeTo(null);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerSize(5);

            leftPanel = new JPanel(new BorderLayout());
            rightPanel = new JPanel(new BorderLayout());

            JPanel buttonsPanel = new JPanel(new GridLayout(10, 1));

            // Button 1 - Navigate back to StartPage
            JButton backButton = new JButton("Go Back");
            backButton.addActionListener(e -> {
                contributorMenuFrame.setVisible(false);
                contributorMenuFrame.dispose();
                StartPage.startContributorPage(user);
            });

            // Button 2 to Button 9 - Additional buttons
            JButton prod_button = new JButton("Productions");
            JButton actors_button = new JButton("Actors");
            JButton notif_button = new JButton("Notifications");
            JButton search_button = new JButton("Search");
            JButton fav_button = new JButton("Favorites");
            JButton req_button = new JButton("Requests");
            JButton add_delete_prod_button = new JButton("Add/Delete Productions/Actors");
            JButton solve_req_button = new JButton("Solve Requests");
            JButton logout_button = new JButton("Logout");

            // lambda expressions for buttons
            prod_button.addActionListener(e -> {
                // Display all productions on the right panel
                displayAllProductions();
            });

            actors_button.addActionListener(e -> {
                // Display all actors on the right panel
                displayAllActors();
            });

            search_button.addActionListener(e -> {
                // Display search page
                initializeSearchPage();
            });

            logout_button.addActionListener(e -> {
                // Logout
                logout_page();
            });

            notif_button.addActionListener(e -> {
                displayNotifications(user);
            });

            fav_button.addActionListener(e -> {
                // Display favorites
                displayFavorites(user);
            });

            req_button.addActionListener(e -> {
                displayUserForm(user, rightPanel);
            });

            add_delete_prod_button.addActionListener(e -> {
                // Display add/delete productions page
                initializeAddDeleteProductionsPage(user);
            });

            solve_req_button.addActionListener(e -> {
                // Display solve requests page
                initializeSolveRequestsPage();
            });

            buttonsPanel.add(backButton);
            buttonsPanel.add(prod_button);
            buttonsPanel.add(actors_button);
            buttonsPanel.add(notif_button);
            buttonsPanel.add(search_button);
            buttonsPanel.add(fav_button);
            buttonsPanel.add(req_button);
            buttonsPanel.add(add_delete_prod_button);
            buttonsPanel.add(solve_req_button);
            buttonsPanel.add(logout_button);

            // Combine left panel components
            leftPanel.add(buttonsPanel);

            // Add components to the split pane
            splitPane.setLeftComponent(leftPanel);
            splitPane.setRightComponent(rightPanel);
            // split at 180 pixels
            splitPane.setDividerLocation(180);

            contributorMenuFrame.add(splitPane);
            contributorMenuFrame.setVisible(true);
        });
    }

    private static void initializeAddDeleteProductionsPage(Contributor user) {
        SwingUtilities.invokeLater(() -> {
            // Create columns and data for the tables
            String[] actorColumnNames = {"Added Actors"};
            Object[][] actorData = getActorContributionsData(user);

            String[] productionColumnNames = {"Added Productions"};
            Object[][] productionData = getProductionContributionsData(user);

            // Create the tables
            JTable actorsTable = new JTable(actorData, actorColumnNames);
            JTable productionsTable = new JTable(productionData, productionColumnNames);

            actorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            productionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane actorsScrollPane = new JScrollPane(actorsTable);
            JScrollPane productionsScrollPane = new JScrollPane(productionsTable);

            // Create buttons
            JButton actorAddButton = new JButton("Add Actor to System");
            JButton actorDeleteButton = new JButton("Remove from System");

            JButton productionAddButton = new JButton("Add Production to System");
            JButton productionDeleteButton = new JButton("Remove from System");

            actorAddButton.addActionListener(e -> {
                handleAddActorSystemButtonClicked(actorsTable);
                refreshActorTableSystem(actorsTable, user.contributions);
            });

            actorDeleteButton.addActionListener(e -> {
                handleDeleteActorSystemButtonClicked(actorsTable);
                refreshActorTableSystem(actorsTable, user.contributions);
            });

            productionAddButton.addActionListener(e -> {
                handleAddProductionSystemButtonClicked(productionsTable, (Contributor) user);
                refreshProductionTableSystem(productionsTable, user.contributions);
            });

            productionDeleteButton.addActionListener(e -> {
                handleDeleteProductionSystemButtonClicked(productionsTable);
                refreshProductionTableSystem(productionsTable, user.contributions);
            });

            // Create panels for buttons and tables
            JPanel actorButtonPanel = new JPanel();
            actorButtonPanel.add(actorAddButton);
            actorButtonPanel.add(actorDeleteButton);

            JPanel productionButtonPanel = new JPanel();
            productionButtonPanel.add(productionAddButton);
            productionButtonPanel.add(productionDeleteButton);

            // Create combined panels with buttons and tables
            JPanel actorCombinedPanel = new JPanel(new BorderLayout());
            actorCombinedPanel.add(actorButtonPanel, BorderLayout.NORTH);
            actorCombinedPanel.add(actorsScrollPane, BorderLayout.CENTER);

            JPanel productionCombinedPanel = new JPanel(new BorderLayout());
            productionCombinedPanel.add(productionButtonPanel, BorderLayout.NORTH);
            productionCombinedPanel.add(productionsScrollPane, BorderLayout.CENTER);

            // Remove previous components from the right panel
            rightPanel.removeAll();

            // Add the combined panels to the right panel
            rightPanel.setLayout(new GridLayout(1, 2)); // 1 row, 2 columns
            rightPanel.add(actorCombinedPanel);
            rightPanel.add(productionCombinedPanel);

            // Refresh the layout
            rightPanel.revalidate();
            rightPanel.repaint();
        });
    }

    private static void refreshProductionTableSystem(JTable productionsTable, SortedSet<Comparable> contributions) {
        Object[][] newData = getProductionContributionsData((Contributor) IMDB.getInstance().currentUser);
        DefaultTableModel model = new DefaultTableModel(newData, new String[]{"Added Productions"});
        productionsTable.setModel(model);
    }

    private static void refreshActorTableSystem(JTable actorsTable, SortedSet<Comparable> contributions) {
        Object[][] newData = getActorContributionsData((Contributor) IMDB.getInstance().currentUser);
        DefaultTableModel model = new DefaultTableModel(newData, new String[]{"Added Actors"});
        actorsTable.setModel(model);
    }

    private static void handleDeleteProductionSystemButtonClicked(JTable productionsTable) {
        int selectedRow = productionsTable.getSelectedRow();
        if (selectedRow != -1) {
            String productionTitle = (String) productionsTable.getValueAt(selectedRow, 0);

            // Find the production in the database
            Production productionToRemove = null;
            for (Production production : IMDB.getInstance().productions) {
                if (production.title.equalsIgnoreCase(productionTitle)) {
                    productionToRemove = production;
                    break;
                }
            }
            Contributor user = (Contributor) IMDB.getInstance().currentUser;

            // Remove the production from favorites
            if (productionToRemove != null) {
                user.removeProductionSystem(productionToRemove);
            }
        }
    }

    private static void handleAddProductionSystemButtonClicked(JTable productionsTable, Contributor user) {
        IMDB imdb = IMDB.getInstance();

        // Prompt the user for production type
        String[] options = {"Movie", "Series"};
        String prodType = (String) JOptionPane.showInputDialog(null, "Choose production type:", "Production Type", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (prodType == null) {
            // User canceled or closed the dialog
            return;
        }

        // Common attributes for both Movie and Series
        int releaseYear = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the release year:"));

        // Additional attributes for Movie
        if (prodType.equalsIgnoreCase("Movie")) {
            // gather information about the movie
            String name = JOptionPane.showInputDialog(null, "Enter the name of the movie:");
            int duration = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the duration of the movie:"));
            String description = JOptionPane.showInputDialog(null, "Enter the description of the movie:");

            // Create a list to store directors, actors, genres
            List<String> directors = gatherInformation("directors");
            List<String> actors = gatherInformation("actors");
            List<Genre> genres = gatherGenres();

            Movie movie = new Movie(name, directors, actors, genres, new ArrayList<>(), description, duration, releaseYear);
            imdb.productions.add(movie);
            user.contributions.add(movie);
            movie.registerObserver(user);
            Parser.addMovie(movie);
            user.updateExperience(new AddToSystemStrategy().calculateExperience());
            Parser.updateContributions(user);
        } else if (prodType.equalsIgnoreCase("Series")) {
            // gather information about the series
            String name = JOptionPane.showInputDialog(null, "Enter the name of the series:");
            int numberOfSeasons = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the number of seasons:"));
            String description = JOptionPane.showInputDialog(null, "Enter the description of the series:");

            // Create a list to store directors, actors, genres
            List<String> directors = gatherInformation("directors");
            List<String> actors = gatherInformation("actors");
            List<Genre> genres = gatherGenres();

            Map<String, List<Episode>> episodes = gatherEpisodes(numberOfSeasons);

            Series series = new Series(name, directors, actors, genres, new ArrayList<>(), description, releaseYear, numberOfSeasons, episodes);
            series.registerObserver(user);
            imdb.productions.add(series);
            user.contributions.add(series);
            Parser.addSeries(series);
            user.updateExperience(new AddToSystemStrategy().calculateExperience());
            Parser.updateContributions(user);
        }

        // Refresh the table
        refreshProductionTableSystem(productionsTable, user.contributions);
    }

    private static List<String> gatherInformation(String informationType) {
        List<String> informationList = new ArrayList<>();
        while (true) {
            String input = JOptionPane.showInputDialog(null, "Enter the names of the " + informationType + ":");
            informationList.add(input);
            String choice = JOptionPane.showInputDialog(null, "Do you want to add another " + informationType + "? (y/n):");
            if (choice == null || !choice.equalsIgnoreCase("y")) {
                break;
            }
        }
        return informationList;
    }

    private static List<Genre> gatherGenres() {
        List<Genre> genres = new ArrayList<>();
        while (true) {
            String genreName = JOptionPane.showInputDialog(null, "Enter the genre:");
            Genre genre = Genre.valueOf(genreName.toUpperCase());
            genres.add(genre);
            String choice = JOptionPane.showInputDialog(null, "Do you want to add another genre? (y/n):");
            if (choice == null || !choice.equalsIgnoreCase("y")) {
                break;
            }
        }
        return genres;
    }

    private static Map<String, List<Episode>> gatherEpisodes(int numberOfSeasons) {
        Map<String, List<Episode>> episodes = new HashMap<>();
        for (int seasonNumber = 1; seasonNumber <= numberOfSeasons; seasonNumber++) {
            List<Episode> seasonEpisodes = new ArrayList<>();
            int numEpisodes = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the number of episodes for Season " + seasonNumber + ":"));
            for (int episodeNumber = 1; episodeNumber <= numEpisodes; episodeNumber++) {
                String episodeTitle = JOptionPane.showInputDialog(null, "Enter the title of Episode " + episodeNumber + " for Season " + seasonNumber + ":");
                int episodeDuration = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter the duration of Episode " + episodeNumber + " for Season " + seasonNumber + " (in minutes):"));
                Episode episode = new Episode(episodeTitle, episodeDuration);
                seasonEpisodes.add(episode);
            }
            episodes.put("Season " + seasonNumber, seasonEpisodes);
        }
        return episodes;
    }

    private static void handleDeleteActorSystemButtonClicked(JTable actorsTable) {
        int selectedRow = actorsTable.getSelectedRow();
        if (selectedRow != -1) {
            String actorName = (String) actorsTable.getValueAt(selectedRow, 0);

            // Find the actor in the database
            Actor actorToRemove = null;
            for (Actor actor : IMDB.getInstance().actors) {
                if (actor.name.equalsIgnoreCase(actorName)) {
                    actorToRemove = actor;
                    break;
                }
            }
            Contributor user = (Contributor) IMDB.getInstance().currentUser;
            // Remove the actor from favorites
            if (actorToRemove != null) {
                user.removeActorSystem(actorToRemove);
            }
        }
    }

    private static void handleAddActorSystemButtonClicked(JTable actorsTable) {
        // Prompt the user for actor information
        String name = JOptionPane.showInputDialog(null, "Enter actor's name:");
        String biography = JOptionPane.showInputDialog(null, "Enter actor's biography:");

        // Create a list to store filmography entries
        List<Actor.Pair<String, Actor.Type>> filmography = new ArrayList<>();

        // Prompt the user for filmography entries until they choose to stop
        while (true) {
            // Prompt for production name
            String productionName = JOptionPane.showInputDialog(null, "Enter production name:");
            if (productionName == null) {
                // User canceled or closed the dialog
                break;
            }

            // Prompt for production type
            String[] options = {Actor.Type.MOVIE.name(), Actor.Type.SERIES.name()};
            String productionType = (String) JOptionPane.showInputDialog(null, "Choose production type:", "Production Type", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (productionType == null) {
                // User canceled or closed the dialog
                break;
            }

            // Convert production type string to enum
            Actor.Type type = Actor.Type.valueOf(productionType);

            // Add the filmography entry to the list
            filmography.add(new Actor.Pair<>(productionName, type));
        }

        // Create a new Actor object
        Actor actor = new Actor(name, filmography, biography);

        // Add the new actor to the table's data model
        refreshActorTableSystem(actorsTable, ((Contributor) IMDB.getInstance().currentUser).contributions);

        IMDB.getInstance().actors.add(actor);
        ((Contributor) IMDB.getInstance().currentUser).contributions.add(actor);
        Parser.addActor(actor);
        ((Contributor) IMDB.getInstance().currentUser).updateExperience(new AddToSystemStrategy().calculateExperience());
        Parser.updateContributions((Contributor) IMDB.getInstance().currentUser);
    }




    private static void initializeSolveRequestsPage() {
        Contributor user = (Contributor) IMDB.getInstance().currentUser;
        List<Request> requests = new ArrayList<>();
        requests.addAll(user.userRequests);

        // Create a table model for the requests
        model = new DefaultTableModel();
        model.addColumn("Type");
        model.addColumn("Description");

        for (Request request : requests) {
            model.addRow(new Object[]{request.type, request.description});
        }

        // Create the table
        JTable requestsTable = new JTable(model);

        // Set single selection mode
        requestsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Create "Solve" button
        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(e -> {
            int selectedRow = requestsTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the selected request from the table
                Request selectedRequest = requests.get(selectedRow);
                // Implement the logic to solve the request
                solveRequest(selectedRequest);
                // Remove the row from the table
                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a request to solve.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Create "Delete" button
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            int selectedRow = requestsTable.getSelectedRow();
            if (selectedRow != -1) {
                // Get the selected request from the table
                Request selectedRequest = requests.get(selectedRow);
                // Implement the logic to delete the request
                deleteRequest(selectedRequest);
                // Remove the row from the table
                model.removeRow(selectedRow);
            } else {
                JOptionPane.showMessageDialog(null, "Please select a request to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Create a panel for buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(solveButton);
        buttonsPanel.add(deleteButton);

        // Create a panel for the table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(new JScrollPane(requestsTable), BorderLayout.CENTER);

        // Create a panel to hold the table and buttons
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Remove previous components from the right panel
        rightPanel.removeAll();

        // Add the main panel to the right panel
        rightPanel.add(mainPanel);

        // Refresh the layout
        rightPanel.revalidate();
        rightPanel.repaint();
    }
    private static void solveRequest(Request r) {
        IMDB imdb = IMDB.getInstance();
        Contributor user = (Contributor) imdb.currentUser;
        r.notifyObservers("request_solved");
        user.removeRequest(r);
        r.removeObserver(user);
        imdb.requests.remove(r);
        Parser.writeRequests();
        User user1 = null;
        for (User u : imdb.users) {
            if (u.username.equals(r.userFrom)) {
                user1 = u;
            }
        }
        user1.updateExperience(new IssueStrategy().calculateExperience());
        Parser.updateLists();
        Parser.updateLists();
    }

    // Dummy method, replace with your actual implementation
    private static void deleteRequest(Request r) {
        IMDB imdb = IMDB.getInstance();
        Contributor user = (Contributor) imdb.currentUser;
        r.notifyObservers("request_denied");
        user.removeRequest(r);
        r.removeObserver(user);
        imdb.requests.remove(r);
        Parser.writeRequests();
        Parser.updateLists();
    }


    private static void displayUserForm(Contributor user, JPanel rightPanel) {
        // Create a panel for the form
        rightPanel.removeAll();
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add a combo box for RequestTypes
        JComboBox<RequestTypes> requestTypesComboBox = new JComboBox<>(RequestTypes.values());
        formPanel.add(new JLabel("Request Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(requestTypesComboBox, gbc);

        // Add a text area for the user to explain the problem
        JTextArea explanationTextArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(explanationTextArea);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Explanation (if you have chosen ACTOR_ISSUE/MOVIE_ISSUE make sure to mention the name of the actor/movie):"), gbc);
        gbc.gridy = 2;
        formPanel.add(scrollPane, gbc);

        // Create a button to submit the form
        JButton submitButton = new JButton("Submit Request");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(submitButton, gbc);

        // Add an action listener to the submit button
        submitButton.addActionListener(e -> {
            handleRequestSubmission(user, (RequestTypes) requestTypesComboBox.getSelectedItem(), explanationTextArea.getText());

            // Show confirmation dialog
            String confirmationMessage = String.format("Request Type: %s\nExplanation: %s",
                    requestTypesComboBox.getSelectedItem(), explanationTextArea.getText());
            JOptionPane.showMessageDialog(null, confirmationMessage, "Request Submitted", JOptionPane.INFORMATION_MESSAGE);

            // Optionally, you can clear the form fields or close the form after submission
            requestTypesComboBox.setSelectedIndex(0);
            explanationTextArea.setText("");
        });

        // Create a button to display user requests
        JButton displayRequestsButton = new JButton("Display Requests");
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(displayRequestsButton, gbc);

        // Add an action listener to the display requests button
        displayRequestsButton.addActionListener(e -> {
            displayUserRequests(user);
        });

        // Add the form components to the right panel (assuming your rightPanel is a class variable)
        rightPanel.add(formPanel);
        rightPanel.revalidate();
        rightPanel.repaint();

    }

    private static void handleRequestSubmission(Contributor user, RequestTypes requestType, String explanation) {
        // Create a new request and add it to the list
//        convert requestType to its enum value
        int requestNumber = requestType.ordinal() + 1;
        Request newRequest = Request.RequestBuilder(user, explanation, requestNumber);
        IMDB imdb = IMDB.getInstance();
//        check if the user wants to add a request regarding a production/actor that was added by himself
        String problemName = newRequest.problemName;
        if (user instanceof Contributor) {
            for (Actor actor : imdb.actors) {
                if (actor.name.equals(problemName)) {
                    if (user.contributions.contains(actor)) {
                        imdb.userInterface.displayOutput("You cannot add a request regarding an actor that you added");
                        return;
                    }
                }
            }
            for (Production production : imdb.productions) {
                if (production.title.equals(problemName)) {
                    if (user.contributions.contains(production)) {
                        imdb.userInterface.displayOutput("You cannot add a request regarding a production that you added");
                        return;
                    }
                }
            }
        }
        user.createRequest(newRequest);
    }

    private static void displayUserRequests(Contributor user) {
        List<Request> userRequests = new ArrayList<>();

        // Get user's requests
        for (Request request : IMDB.getInstance().requests) {
            if (request.userFrom.equals(user.username)) {
                userRequests.add(request);
            }
        }

        // Create table data
        Object[][] data = new Object[userRequests.size()][2];
        for (int i = 0; i < userRequests.size(); i++) {
            data[i][0] = i + 1;
            data[i][1] = userRequests.get(i).description;
        }

        // Create table column names
        String[] columnNames = {"#", "Description"};

        // Initialize or update the table model
        if (model == null) {
            model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Make the table cells non-editable
                }
            };
        } else {
            model.setDataVector(data, columnNames);
        }

        JTable requestsTable = new JTable(model);

        // Create the scroll pane
        JScrollPane scrollPane = new JScrollPane(requestsTable);

        // Create a button to delete the selected request
        JButton deleteButton = new JButton("Delete Selected Request");
        deleteButton.addActionListener(e -> {
            int selectedRow = requestsTable.getSelectedRow();
            if (selectedRow != -1) {
                handleDeleteRequestClicked(userRequests, selectedRow);
                refreshTable(requestsTable, userRequests);
            }
        });

        // Create a panel for the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);

        // Create a panel for the whole content
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Display requests in a popup
        JOptionPane.showMessageDialog(null, contentPanel, "User Requests", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void handleDeleteRequestClicked(List<Request> userRequests, int selectedRow) {
        // Delete the request from the list
        Request requestToDelete = userRequests.get(selectedRow);
        Contributor user = (Contributor) IMDB.getInstance().currentUser;
        user.removeRequest(requestToDelete);

        // Update the userRequests list
        userRequests.remove(requestToDelete);
    }

    private static void refreshTable(JTable table, Object[][] newData, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(newData, columnNames);
        table.setModel(model);
    }

    private static void refreshTable(JTable requestsTable, List<Request> userRequests) {
        // Create updated data for the table
        Object[][] newData = new Object[userRequests.size()][2];
        for (int i = 0; i < userRequests.size(); i++) {
            newData[i][0] = i + 1;
            newData[i][1] = userRequests.get(i).description;
        }

        // Create table column names
        String[] columnNames = {"#", "Description"};

        // Call the refreshTable method
        refreshTable(requestsTable, newData, columnNames);
    }

    private static void displayFavorites(Contributor user) {
        SwingUtilities.invokeLater(() -> {
            // Create columns and data for the tables
            String[] actorColumnNames = {"Favorite Actors"};
            Object[][] actorData = getActorData();

            String[] productionColumnNames = {"Favorite Productions"};
            Object[][] productionData = getProductionData();

            // Create the tables
            JTable actorsTable = new JTable(actorData, actorColumnNames);
            JTable productionsTable = new JTable(productionData, productionColumnNames);

            actorsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            productionsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            JScrollPane actorsScrollPane = new JScrollPane(actorsTable);
            JScrollPane productionsScrollPane = new JScrollPane(productionsTable);

            // Create buttons
            JButton actorAddButton = new JButton("Add Actor to Favorites");
            JButton actorDeleteButton = new JButton("Remove from Favorites");

            JButton productionAddButton = new JButton("Add Production to Favorites");
            JButton productionDeleteButton = new JButton("Remove from Favorites");

            actorAddButton.addActionListener(e -> {
                handleAddActorButtonClicked();
                refreshActorTable(actorsTable);
            });

            actorDeleteButton.addActionListener(e -> {
                handleDeleteActorButtonClicked(actorsTable);
                refreshActorTable(actorsTable);
            });

            productionAddButton.addActionListener(e -> {
                handleAddProductionButtonClicked();
                refreshProductionTable(productionsTable);
            });

            productionDeleteButton.addActionListener(e -> {
                handleDeleteProductionButtonClicked(productionsTable);
                refreshProductionTable(productionsTable);
            });

            // Create panels for buttons and tables
            JPanel actorButtonPanel = new JPanel();
            actorButtonPanel.add(actorAddButton);
            actorButtonPanel.add(actorDeleteButton);

            JPanel productionButtonPanel = new JPanel();
            productionButtonPanel.add(productionAddButton);
            productionButtonPanel.add(productionDeleteButton);

            // Create combined panels with buttons and tables
            JPanel actorCombinedPanel = new JPanel(new BorderLayout());
            actorCombinedPanel.add(actorButtonPanel, BorderLayout.NORTH);
            actorCombinedPanel.add(actorsScrollPane, BorderLayout.CENTER);

            JPanel productionCombinedPanel = new JPanel(new BorderLayout());
            productionCombinedPanel.add(productionButtonPanel, BorderLayout.NORTH);
            productionCombinedPanel.add(productionsScrollPane, BorderLayout.CENTER);

            // Remove previous components from the right panel
            rightPanel.removeAll();

            // Add the combined panels to the right panel
            rightPanel.setLayout(new GridLayout(1, 2)); // 1 row, 2 columns
            rightPanel.add(actorCombinedPanel);
            rightPanel.add(productionCombinedPanel);

            // Refresh the layout
            rightPanel.revalidate();
            rightPanel.repaint();
        });
    }

    private static Object[][] getActorData() {
        // Populate data from the user's favorite actors
        List<String> favoriteActors = IMDB.getInstance().currentUser.getFavoriteActors();

        int size = favoriteActors.size();
        Object[][] data = new Object[size][1];

        for (int i = 0; i < size; i++) {
            data[i][0] = favoriteActors.get(i);
        }

        return data;
    }

    private static Object[][] getActorContributionsData(Contributor user) {
        List<Actor> contributions = new ArrayList<>();
        for (Object comparable : user.contributions) {
            if (comparable instanceof Actor) {
                contributions.add((Actor) comparable);
            }
        }

        int size = contributions.size();
        Object[][] data = new Object[size][1];

        for (int i = 0; i < size; i++) {
            data[i][0] = contributions.get(i).name;
        }

        return data;
    }

    private static Object[][] getProductionContributionsData(Contributor user) {
        List<Production> contributions = new ArrayList<>();
        for (Object comparable : user.contributions) {
            if (comparable instanceof Production) {
                contributions.add((Production) comparable);
            }
        }

        int size = contributions.size();
        Object[][] data = new Object[size][1];

        for (int i = 0; i < size; i++) {
            data[i][0] = contributions.get(i).title;
        }

        return data;
    }

    private static Object[][] getProductionData() {
        // Populate data from the user's favorite productions
        List<String> favoriteProductions = IMDB.getInstance().currentUser.getFavoriteProductions();

        int size = favoriteProductions.size();
        Object[][] data = new Object[size][1];

        for (int i = 0; i < size; i++) {
            data[i][0] = favoriteProductions.get(i);
        }

        return data;
    }

    private static void handleAddActorButtonClicked() {
        String actor = JOptionPane.showInputDialog("", "Enter favorite actor:");

        Actor a = null;
        if (actor != null && !actor.isEmpty()) {
            // Get the actor from the database
            for (Actor actor1 : IMDB.getInstance().actors) {
                if (actor1.name.equalsIgnoreCase(actor)) {
                    a = actor1;
                    break;
                }
            }
            if (a != null)
                IMDB.getInstance().currentUser.addActorToFavorites(a);
        }
//        display actor not found
        IMDB.getInstance().userInterface.displayOutput("Actor not found");
    }

    private static void handleDeleteActorButtonClicked(JTable actorsTable) {
        int selectedRow = actorsTable.getSelectedRow();
        if (selectedRow != -1) {
            String actorName = (String) actorsTable.getValueAt(selectedRow, 0);

            // Find the actor in the database
            Actor actorToRemove = null;
            for (Actor actor : IMDB.getInstance().actors) {
                if (actor.name.equalsIgnoreCase(actorName)) {
                    actorToRemove = actor;
                    break;
                }
            }

            // Remove the actor from favorites
            if (actorToRemove != null) {
                IMDB.getInstance().currentUser.removeActorFromFavourites(actorToRemove);
            }
        }
    }

    private static void refreshActorTable(JTable actorsTable) {
        // Refresh the actor table after adding or removing items
        Object[][] newData = getActorData();
        DefaultTableModel model = new DefaultTableModel(newData, new String[]{"Favorite Actors"});
        actorsTable.setModel(model);
    }

    private static void handleAddProductionButtonClicked() {
        String production = JOptionPane.showInputDialog("", "Enter favorite production:");

        if (production != null && !production.isEmpty()) {
            // Get the production from the database
            Production p = null;
            for (Production production1 : IMDB.getInstance().productions) {
                if (production1.title.equalsIgnoreCase(production)) {
                    p = production1;
                    break;
                }
            }
            if (p != null)
                IMDB.getInstance().currentUser.addProductionToFavorites(p);
        }
//        display production not found
        IMDB.getInstance().userInterface.displayOutput("Production not found");
    }

    private static void handleDeleteProductionButtonClicked(JTable productionsTable) {
        int selectedRow = productionsTable.getSelectedRow();
        if (selectedRow != -1) {
            String productionTitle = (String) productionsTable.getValueAt(selectedRow, 0);

            // Find the production in the database
            Production productionToRemove = null;
            for (Production production : IMDB.getInstance().productions) {
                if (production.title.equalsIgnoreCase(productionTitle)) {
                    productionToRemove = production;
                    break;
                }
            }

            // Remove the production from favorites
            if (productionToRemove != null) {
                IMDB.getInstance().currentUser.removeProductionFromFavourites(productionToRemove);
            }
        }
    }

    private static void refreshProductionTable(JTable productionsTable) {
        // Refresh the production table after adding or removing items
        Object[][] newData = getProductionData();
        DefaultTableModel model = new DefaultTableModel(newData, new String[]{"Favorite Productions"});
        productionsTable.setModel(model);
    }

    private static void displayAllProductions() {
        // Create three buttons
        JButton genreButton = new JButton("Genre");
        JButton numberOfReviewsButton = new JButton("Number of Reviews");
        JButton clearFiltersButton = new JButton("Clear Filters");

        // Add action listeners to the buttons
        genreButton.addActionListener(e -> handleGenreButtonClicked());
        numberOfReviewsButton.addActionListener(e -> handleNumberOfReviewsButtonClicked());
        clearFiltersButton.addActionListener(e -> handleClearFiltersButtonClicked());

        // Create a panel for the buttons
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Adjust layout as needed
        buttonsPanel.add(genreButton);
        buttonsPanel.add(numberOfReviewsButton);
        buttonsPanel.add(clearFiltersButton);
//        make a new list of all the productions that have the selected genres
        List<Production> finalProd = new ArrayList<>();
        for (Production production : IMDB.getInstance().productions) {
            if (selectedGenres == null || selectedGenres.isEmpty()) {
                if (production.ratings.size() >= minimum_reviews) {
                    finalProd.add(production);
                }
            } else {
                boolean meetsAllGenres = true;
                for (Genre genre : selectedGenres) {
                    if (!production.genres.contains(genre)) {
                        meetsAllGenres = false;
                        break;
                    }
                }
                if (meetsAllGenres && production.ratings.size() >= minimum_reviews) {
                    finalProd.add(production);
                }
            }
        }

        // Create a panel with productions
        JPanel productionsPanel = createProductionsPanel(finalProd);
        // Create a combined panel with buttons and productions
        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BorderLayout());
        combinedPanel.add(buttonsPanel, BorderLayout.NORTH);
        combinedPanel.add(productionsPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(combinedPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Update the right panel with the scrollPane
        updateRightPanel(scrollPane);
    }

    private static void handleGenreButtonClicked() {
        GenreFilterDialog genreFilterDialog = new GenreFilterDialog(contributorMenuFrame);
        selectedGenres = genreFilterDialog.getSelectedGenres();
        displayAllProductions();
    }

    private static void handleNumberOfReviewsButtonClicked() {
        // Create a slider with a range from 0 to a maximum value (e.g., 20)
        JSlider reviewsSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
        reviewsSlider.setMajorTickSpacing(1);
        reviewsSlider.setMinorTickSpacing(1);
        reviewsSlider.setPaintTicks(true);
        reviewsSlider.setPaintLabels(true);

        // Customize appearance of the slider
        reviewsSlider.setLabelTable(reviewsSlider.createStandardLabels(1)); // Display labels at major ticks
        reviewsSlider.setFont(new Font("Arial", Font.PLAIN, 12)); // Adjust font size

        // Create the panel to hold the slider
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(new JLabel("Select Minimum Number of Reviews:"), BorderLayout.NORTH);
        sliderPanel.add(reviewsSlider, BorderLayout.CENTER);

        // Set preferred size of the JOptionPane
        Dimension preferredSize = new Dimension(400, 150); // Adjust the values as needed
        sliderPanel.setPreferredSize(preferredSize);

        // Show the option pane with the slider
        int result = JOptionPane.showOptionDialog(
                null,
                sliderPanel,
                "Number of Reviews Filter",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                null,
                null
        );

        // If the user clicks OK, update the minimum_reviews variable
        if (result == JOptionPane.OK_OPTION) {

            minimum_reviews = reviewsSlider.getValue();
            displayAllProductions();
        }
    }

    private static void handleClearFiltersButtonClicked() {
        if (selectedGenres != null)
            selectedGenres.clear();
        minimum_reviews = 0;
        displayAllProductions();
    }

    private static JPanel createProductionsPanel(List<Production> productions) {
        JPanel productionsPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns, with gaps
        productionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Production production : productions) {
            ImageIcon productionIcon = getProductionImage(production);
            Image image = productionIcon.getImage();
            Image newimg = image.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            productionIcon = new ImageIcon(newimg);

            JButton productionButton = new JButton(productionIcon);
            productionButton.setPreferredSize(new Dimension(150, 200));

            productionButton.addActionListener(e -> {
                // Handle the button click, e.g., display information about the production
                ImageIcon productionImageIcon = getProductionImage(production);
                // Resize image
                Image productionImage = productionImageIcon.getImage();
                Image newProductionImage = productionImage.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
                productionImageIcon = new ImageIcon(newProductionImage);
                if (production instanceof Movie)
                    ItemPopup.showItemPopup((Movie) production, productionImageIcon, IMDB.getInstance().currentUser);
                else if (production instanceof Series)
                    ItemPopup.showItemPopup((Series) production, productionImageIcon, IMDB.getInstance().currentUser);
            });

            JLabel productionTitleLabel = new JLabel("<html><body style='width:150px;'>" + production.title + "</body></html>");
            productionTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel productionPanel = new JPanel(new BorderLayout());
            productionPanel.add(productionButton, BorderLayout.CENTER);
            productionPanel.add(productionTitleLabel, BorderLayout.SOUTH);

            productionsPanel.add(productionPanel);
        }

        return productionsPanel;
    }


    private static ImageIcon getProductionImage(Production production) {
        if (production instanceof Movie) {
            return new ImageIcon("src/movie.jpg");
        } else if (production instanceof Series) {
            return new ImageIcon("src/series.jpg");
        }
        return null;
    }

    private static void displayNotifications(Contributor user) {
        // Create a panel with notifications
        JPanel notificationsPanel = createNotificationsPanel(user);

        // Create a clear button
        JButton clearNotificationsButton = new JButton("Clear Notifications");
        clearNotificationsButton.addActionListener(e -> clearNotificationsButtonActionPerformed(e, user));

        // Create a panel for notifications and the clear button
        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.add(notificationsPanel);
        combinedPanel.add(Box.createVerticalStrut(10)); // Add some vertical spacing
        combinedPanel.add(clearNotificationsButton);

        // Create a scroll pane for the combined panel
        JScrollPane scrollPane = new JScrollPane(combinedPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // Update the right panel with the scrollPane
        updateRightPanel(scrollPane);
    }

    private static void clearNotificationsButtonActionPerformed(ActionEvent evt, Contributor user) {
        user.notifications.clear();
        Parser.updateNotifications(user);
        displayNotifications(user);
    }

    private static JPanel createNotificationsPanel(Contributor user) {
        JPanel notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.Y_AXIS));

        List<String> notifications = user.notifications;

        if (notifications.isEmpty()) {
            JLabel noNotificationsLabel = new JLabel("No notifications!");
            noNotificationsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            notificationsPanel.add(noNotificationsLabel);
        } else {
            for (String notification : notifications) {
                JPanel notificationPanel = createNotificationPanel(notification);
                notificationsPanel.add(notificationPanel);
            }
        }

        return notificationsPanel;
    }

    private static JPanel createNotificationPanel(String notification) {
        JPanel notificationPanel = new JPanel(new BorderLayout());
        notificationPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel messageLabel = new JLabel("<html><body style='width: 400px; text-align: center; font-size: 14pt;'>" + notification + "</body></html>");
        messageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        notificationPanel.add(messageLabel, BorderLayout.CENTER);

        return notificationPanel;
    }

    private static void logout_page() {
        // Create buttons for logout options
        JButton closeAppButton = new JButton("Close App");
        JButton loginButton = new JButton("Login");

        // Set preferred size for buttons
        Dimension buttonSize = new Dimension(200, 40);
        closeAppButton.setPreferredSize(buttonSize);
        loginButton.setPreferredSize(buttonSize);

        // Set layout for the panel
        JPanel logoutButtonsPanel = new JPanel(new GridBagLayout());
        logoutButtonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add some padding

        // Create label for the instruction
        JLabel instructionLabel = new JLabel("Close the app or login as a different user.");
        // Center the text
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add labels and buttons to the panel using GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0); // Add vertical spacing
        logoutButtonsPanel.add(instructionLabel, gbc);

        gbc.gridy++;
        logoutButtonsPanel.add(closeAppButton, gbc);

        gbc.gridy++;
        logoutButtonsPanel.add(loginButton, gbc);

        // Add action listeners to handle button clicks
        closeAppButton.addActionListener(e -> System.exit(0));

        loginButton.addActionListener(e -> {
            // Close the current app and open a new instance
            contributorMenuFrame.setVisible(false);
            contributorMenuFrame.dispose();
            IMDB.getInstance().login_GUI();
        });

        // Remove existing components from the right panel
        rightPanel.removeAll();

        // Add the buttons panel to the center of the right panel
        rightPanel.add(logoutButtonsPanel, BorderLayout.CENTER);

        // Add an empty panel to occupy the remaining space
        rightPanel.add(new JPanel(), BorderLayout.SOUTH);

        // Repaint the panel to reflect changes
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private static void initializeSearchPage() {
        JTextField searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(300, 40));

        // Label for search bar
        JLabel searchBarLabel = new JLabel("Search:");
        searchBarLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchBarPanel.add(searchBarLabel);
        searchBarPanel.add(searchBar);

        JPanel searchButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton searchButton = new JButton("Search");

        searchButtonPanel.add(searchButton);
        rightPanel.add(searchButtonPanel, BorderLayout.CENTER);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(200, 50));
        rightPanel.add(emptyPanel, BorderLayout.NORTH);

//        add action listener to search button
        searchButton.addActionListener(e -> searchButtonActionPerformed(e, searchBar));
        searchBar.addActionListener(e -> searchButtonActionPerformed(e, searchBar));

        rightPanel.removeAll();
        rightPanel.add(searchBarPanel, BorderLayout.NORTH);
        rightPanel.add(searchButtonPanel, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private static void searchButtonActionPerformed(java.awt.event.ActionEvent evt, JTextField searchBar) {
        String searchQuery = searchBar.getText().trim().toLowerCase();

        Actor foundActor = findActor(searchQuery);
        if (foundActor != null) {
            ImageIcon actorIcon = new ImageIcon("src/actor.jpg");
//                    resize image
            Image image1 = actorIcon.getImage();
            Image newimg1 = image1.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
            actorIcon = new ImageIcon(newimg1);
            ItemPopup.showItemPopup(foundActor, actorIcon, IMDB.getInstance().currentUser);
            return;
        }

        Production foundProduction = findProduction(searchQuery);
        if (foundProduction != null) {
            if (foundProduction instanceof Movie) {
                ImageIcon movieIcon = new ImageIcon("src/movie.jpg");
//                    resize
                Image image1 = movieIcon.getImage();
                Image newimg1 = image1.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                movieIcon = new ImageIcon(newimg1);
                ItemPopup.showItemPopup((Movie) foundProduction, movieIcon, IMDB.getInstance().currentUser);
            } else if (foundProduction instanceof Series) {
                ImageIcon seriesIcon = new ImageIcon("src/series.jpg");
//                    resize
                Image image1 = seriesIcon.getImage();
                Image newimg1 = image1.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                seriesIcon = new ImageIcon(newimg1);
                ItemPopup.showItemPopup((Series) foundProduction, seriesIcon, IMDB.getInstance().currentUser);
            }
            return;
        }

        // If nothing is found, display a popup
        JOptionPane.showMessageDialog(null, "No actor/production found with the given name.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
    }


    private static Actor findActor(String searchQuery) {
        for (Actor actor : IMDB.getInstance().actors) {
            if (actor.name.toLowerCase().equals(searchQuery)) {
                return actor;
            }
        }
        return null;
    }

    private static Production findProduction(String searchQuery) {
        for (Production production : IMDB.getInstance().productions) {
            if (production.title.toLowerCase().equals(searchQuery)) {
                return production;
            }
        }
        return null;
    }

    private static void displayAllActors() {
        // Create a panel with actors
        JPanel actorsPanel = createActorsPanel();
        JScrollPane scrollPane = new JScrollPane(actorsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


        // Update the right panel with the actorsPanel
        updateRightPanel(scrollPane);
    }

    public static JPanel createActorsPanel() {
        // Create a copy of the actors list to avoid modifying the original list
        ArrayList<Actor> sortedActors = new ArrayList<>(IMDB.getInstance().actors);

        // Sort the actors alphabetically by name
        Collections.sort(sortedActors, Comparator.comparing(actor -> actor.name));

        JPanel actorsPanel = new JPanel(new GridLayout(0, 3, 10, 10)); // 3 columns, with gaps
        actorsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Actor actor : sortedActors) {
            ImageIcon actorIcon = new ImageIcon("src/actor.jpg");
            Image image = actorIcon.getImage();
            Image newimg = image.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            actorIcon = new ImageIcon(newimg);

            JButton actorButton = new JButton(actorIcon);

            actorButton.addActionListener(e -> {
                // Handle the button click, e.g., display information about the actor
                ImageIcon actorImageIcon = new ImageIcon("src/actor.jpg");
                // Resize image
                Image actorImage = actorImageIcon.getImage();
                Image newActorImage = actorImage.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                actorImageIcon = new ImageIcon(newActorImage);
                ItemPopup.showItemPopup(actor, actorImageIcon, IMDB.getInstance().currentUser);
            });

            JLabel actorNameLabel = new JLabel(actor.name);
            actorNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel actorPanel = new JPanel(new BorderLayout());
            actorPanel.add(actorButton, BorderLayout.CENTER);
            actorPanel.add(actorNameLabel, BorderLayout.SOUTH);

            actorsPanel.add(actorPanel);
        }

        return actorsPanel;
    }

    private static void updateRightPanel(Object newContentPanel) {
        if (newContentPanel instanceof JScrollPane) {
            SwingUtilities.invokeLater(() -> {
                if (rightPanel != null) {
                    // Clear existing components in the right panel
                    rightPanel.removeAll();

                    // Add the new content to the right panel
                    rightPanel.add((JScrollPane) newContentPanel, BorderLayout.CENTER);

                    // Repaint the panel to reflect changes
                    rightPanel.revalidate();
                    rightPanel.repaint();
                }
            });
        }
    }

}
