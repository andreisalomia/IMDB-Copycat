import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RegularMenu {
    private static JFrame regularMenuFrame;
    private static JPanel leftPanel;
    private static JPanel rightPanel;
    private static List<Genre> selectedGenres;
    private static int minimum_reviews = 0;
    private static DefaultTableModel model;

    public static void initializeMenu(Regular regular) {
        SwingUtilities.invokeLater(() -> {
            regularMenuFrame = new JFrame("Regular Menu");
            regularMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            regularMenuFrame.setSize(1024, 768);
            regularMenuFrame.setLocationRelativeTo(null);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerSize(5);

            leftPanel = new JPanel(new BorderLayout());
            rightPanel = new JPanel(new BorderLayout());

            JPanel buttonsPanel = new JPanel(new GridLayout(9, 1));

            JButton backButton = new JButton("Go Back");
            backButton.addActionListener(e -> {
                regularMenuFrame.setVisible(false);
                regularMenuFrame.dispose();
                StartPage.startRegularPage(regular);
            });

            JButton prod_button = new JButton("Productions");
            JButton actors_button = new JButton("Actors");
            JButton notif_button = new JButton("Notifications");
            JButton search_button = new JButton("Search");
            JButton fav_button = new JButton("Favorites");
            JButton req_button = new JButton("Requests");
            JButton review_button = new JButton("Reviews");
            JButton logout_button = new JButton("Logout");

            prod_button.addActionListener(e -> {
                displayAllProductions();
            });
            
            actors_button.addActionListener(e -> {
                displayAllActors();
            });

            search_button.addActionListener(e -> {
                initializeSearchPage();
            });
            
            logout_button.addActionListener(e -> {
                logout_page();
            });

            notif_button.addActionListener(e -> {
                displayNotifications(regular);
            });

            fav_button.addActionListener(e -> {
                displayFavorites(regular);
            });

            req_button.addActionListener(e -> {
                displayUserForm(regular);
            });

            review_button.addActionListener(e -> {
                addReviews(regular);
            });

            buttonsPanel.add(backButton);
            buttonsPanel.add(prod_button);
            buttonsPanel.add(actors_button);
            buttonsPanel.add(notif_button);
            buttonsPanel.add(search_button);
            buttonsPanel.add(fav_button);
            buttonsPanel.add(req_button);
            buttonsPanel.add(review_button);
            buttonsPanel.add(logout_button);

            leftPanel.add(buttonsPanel);

            splitPane.setLeftComponent(leftPanel);
            splitPane.setRightComponent(rightPanel);
            splitPane.setDividerLocation(180);

            regularMenuFrame.add(splitPane);
            regularMenuFrame.setVisible(true);
        });
    }

    private static void addReviews(Regular regular) {
        SwingUtilities.invokeLater(() -> {
            rightPanel.removeAll();

            List<Production> allProductions = IMDB.getInstance().productions;

            Object[][] data = new Object[allProductions.size()][2];
            for (int i = 0; i < allProductions.size(); i++) {
                data[i][0] = i + 1;
                data[i][1] = allProductions.get(i).title;
            }

            String[] columnNames = {"#", "Production Title"};

            DefaultTableModel reviewTableModel = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable reviewTable = new JTable(reviewTableModel);
            JScrollPane scrollPane = new JScrollPane(reviewTable);

            reviewTable.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = reviewTable.getSelectedRow();
                        if (selectedRow != -1) {
                            Production selectedProduction = allProductions.get(selectedRow);
                            handleAddReviewButtonClicked(regular, selectedProduction);
                        }
                    }
                }
            });

            rightPanel.add(scrollPane, BorderLayout.CENTER);

            rightPanel.revalidate();
            rightPanel.repaint();
        });
    }

    private static void handleAddReviewButtonClicked(Regular regular, Production production) {
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

                Rating newReview = new Rating(regular.username, rating, message);
                regular.addRating(newReview, production);
                JOptionPane.showMessageDialog(null, "Review added!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a valid number for the rating.");
            }
        }
    }


    private static void displayUserForm(Regular regular) {
        rightPanel.removeAll();
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JComboBox<RequestTypes> requestTypesComboBox = new JComboBox<>(RequestTypes.values());
        formPanel.add(new JLabel("Request Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(requestTypesComboBox, gbc);
        JTextArea explanationTextArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(explanationTextArea);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(new JLabel("Explanation (if you have chosen ACTOR_ISSUE/MOVIE_ISSUE make sure to mention the name of the actor/movie):"), gbc);
        gbc.gridy = 2;
        formPanel.add(scrollPane, gbc);

        JButton submitButton = new JButton("Submit Request");
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(submitButton, gbc);

        submitButton.addActionListener(e -> {
            handleRequestSubmission(regular, (RequestTypes) requestTypesComboBox.getSelectedItem(), explanationTextArea.getText());

            String confirmationMessage = String.format("Request Type: %s\nExplanation: %s",
                    requestTypesComboBox.getSelectedItem(), explanationTextArea.getText());
            JOptionPane.showMessageDialog(null, confirmationMessage, "Request Submitted", JOptionPane.INFORMATION_MESSAGE);

            requestTypesComboBox.setSelectedIndex(0);
            explanationTextArea.setText("");
        });

        JButton displayRequestsButton = new JButton("Display Requests");
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(displayRequestsButton, gbc);

        displayRequestsButton.addActionListener(e -> {
            displayUserRequests(regular);
        });

        rightPanel.add(formPanel);
        rightPanel.revalidate();
        rightPanel.repaint();

    }

    private static void handleRequestSubmission(Regular regular, RequestTypes requestType, String explanation) {
        int requestNumber = requestType.ordinal() + 1;
        Request newRequest = Request.RequestBuilder(regular, explanation, requestNumber);
        IMDB imdb = IMDB.getInstance();
        regular.createRequest(newRequest);
    }

    private static void displayUserRequests(Regular regular) {
        List<Request> userRequests = new ArrayList<>();

        for (Request request : IMDB.getInstance().requests) {
            if (request.userFrom.equals(regular.username)) {
                userRequests.add(request);
            }
        }

        Object[][] data = new Object[userRequests.size()][2];
        for (int i = 0; i < userRequests.size(); i++) {
            data[i][0] = i + 1;
            data[i][1] = userRequests.get(i).description;
        }

        String[] columnNames = {"#", "Description"};

        if (model == null) {
            model = new DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
        } else {
            model.setDataVector(data, columnNames);
        }

        JTable requestsTable = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(requestsTable);

        JButton deleteButton = new JButton("Delete Selected Request");
        deleteButton.addActionListener(e -> {
            int selectedRow = requestsTable.getSelectedRow();
            if (selectedRow != -1) {
                handleDeleteRequestClicked(userRequests, selectedRow);
                refreshTable(requestsTable, userRequests);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(deleteButton);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(null, contentPanel, "User Requests", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void handleDeleteRequestClicked(List<Request> userRequests, int selectedRow) {
        Request requestToDelete = userRequests.get(selectedRow);
        Regular regular = (Regular)IMDB.getInstance().currentUser;
        regular.removeRequest(requestToDelete);

        userRequests.remove(requestToDelete);
    }

    private static void refreshTable(JTable table, Object[][] newData, String[] columnNames) {
        DefaultTableModel model = new DefaultTableModel(newData, columnNames);
        table.setModel(model);
    }

    private static void refreshTable(JTable requestsTable, List<Request> userRequests) {
        Object[][] newData = new Object[userRequests.size()][2];
        for (int i = 0; i < userRequests.size(); i++) {
            newData[i][0] = i + 1;
            newData[i][1] = userRequests.get(i).description;
        }

        String[] columnNames = {"#", "Description"};

        refreshTable(requestsTable, newData, columnNames);
    }

    private static void displayFavorites(Regular regular) {
        SwingUtilities.invokeLater(() -> {
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

            JPanel actorButtonPanel = new JPanel();
            actorButtonPanel.add(actorAddButton);
            actorButtonPanel.add(actorDeleteButton);

            JPanel productionButtonPanel = new JPanel();
            productionButtonPanel.add(productionAddButton);
            productionButtonPanel.add(productionDeleteButton);

            JPanel actorCombinedPanel = new JPanel(new BorderLayout());
            actorCombinedPanel.add(actorButtonPanel, BorderLayout.NORTH);
            actorCombinedPanel.add(actorsScrollPane, BorderLayout.CENTER);

            JPanel productionCombinedPanel = new JPanel(new BorderLayout());
            productionCombinedPanel.add(productionButtonPanel, BorderLayout.NORTH);
            productionCombinedPanel.add(productionsScrollPane, BorderLayout.CENTER);

            rightPanel.removeAll();

            rightPanel.setLayout(new GridLayout(1, 2));
            rightPanel.add(actorCombinedPanel);
            rightPanel.add(productionCombinedPanel);

            rightPanel.revalidate();
            rightPanel.repaint();
        });
    }

    private static Object[][] getActorData() {
        List<String> favoriteActors = IMDB.getInstance().currentUser.getFavoriteActors();

        int size = favoriteActors.size();
        Object[][] data = new Object[size][1];

        for (int i = 0; i < size; i++) {
            data[i][0] = favoriteActors.get(i);
        }

        return data;
    }

    private static Object[][] getProductionData() {
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
            for (Actor actor1 : IMDB.getInstance().actors) {
                if (actor1.name.equalsIgnoreCase(actor)) {
                    a = actor1;
                    break;
                }
            }
            if(a != null)
                IMDB.getInstance().currentUser.addActorToFavorites(a);
        }
        IMDB.getInstance().userInterface.displayOutput("Actor not found");
    }

    private static void handleDeleteActorButtonClicked(JTable actorsTable) {
        int selectedRow = actorsTable.getSelectedRow();
        if (selectedRow != -1) {
            String actorName = (String) actorsTable.getValueAt(selectedRow, 0);

            Actor actorToRemove = null;
            for (Actor actor : IMDB.getInstance().actors) {
                if (actor.name.equalsIgnoreCase(actorName)) {
                    actorToRemove = actor;
                    break;
                }
            }

            if (actorToRemove != null) {
                IMDB.getInstance().currentUser.removeActorFromFavourites(actorToRemove);
            }
        }
    }

    private static void refreshActorTable(JTable actorsTable) {
        Object[][] newData = getActorData();
        DefaultTableModel model = new DefaultTableModel(newData, new String[]{"Favorite Actors"});
        actorsTable.setModel(model);
    }

    private static void handleAddProductionButtonClicked() {
        String production = JOptionPane.showInputDialog("", "Enter favorite production:");

        if (production != null && !production.isEmpty()) {
            Production p = null;
            for (Production production1 : IMDB.getInstance().productions) {
                if (production1.title.equalsIgnoreCase(production)) {
                    p = production1;
                    break;
                }
            }
            if(p != null)
                IMDB.getInstance().currentUser.addProductionToFavorites(p);
        }
        IMDB.getInstance().userInterface.displayOutput("Production not found");
    }

    private static void handleDeleteProductionButtonClicked(JTable productionsTable) {
        int selectedRow = productionsTable.getSelectedRow();
        if (selectedRow != -1) {
            String productionTitle = (String) productionsTable.getValueAt(selectedRow, 0);

            Production productionToRemove = null;
            for (Production production : IMDB.getInstance().productions) {
                if (production.title.equalsIgnoreCase(productionTitle)) {
                    productionToRemove = production;
                    break;
                }
            }

            if (productionToRemove != null) {
                IMDB.getInstance().currentUser.removeProductionFromFavourites(productionToRemove);
            }
        }
    }

    private static void refreshProductionTable(JTable productionsTable) {
        Object[][] newData = getProductionData();
        DefaultTableModel model = new DefaultTableModel(newData, new String[]{"Favorite Productions"});
        productionsTable.setModel(model);
    }

    private static void displayAllProductions() {
        JButton genreButton = new JButton("Genre");
        JButton numberOfReviewsButton = new JButton("Number of Reviews");
        JButton clearFiltersButton = new JButton("Clear Filters");

        genreButton.addActionListener(e -> handleGenreButtonClicked());
        numberOfReviewsButton.addActionListener(e -> handleNumberOfReviewsButtonClicked());
        clearFiltersButton.addActionListener(e -> handleClearFiltersButtonClicked());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonsPanel.add(genreButton);
        buttonsPanel.add(numberOfReviewsButton);
        buttonsPanel.add(clearFiltersButton);
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

        JPanel productionsPanel = createProductionsPanel(finalProd);
        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BorderLayout());
        combinedPanel.add(buttonsPanel, BorderLayout.NORTH);
        combinedPanel.add(productionsPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(combinedPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        updateRightPanel(scrollPane);
    }

    private static void handleGenreButtonClicked() {
        GenreFilterDialog genreFilterDialog = new GenreFilterDialog(regularMenuFrame);
        selectedGenres = genreFilterDialog.getSelectedGenres();
        displayAllProductions();
    }

    private static void handleNumberOfReviewsButtonClicked() {
        JSlider reviewsSlider = new JSlider(JSlider.HORIZONTAL, 0, 20, 0);
        reviewsSlider.setMajorTickSpacing(1);
        reviewsSlider.setMinorTickSpacing(1);
        reviewsSlider.setPaintTicks(true);
        reviewsSlider.setPaintLabels(true);

        reviewsSlider.setLabelTable(reviewsSlider.createStandardLabels(1));
        reviewsSlider.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(new JLabel("Select Minimum Number of Reviews:"), BorderLayout.NORTH);
        sliderPanel.add(reviewsSlider, BorderLayout.CENTER);

        Dimension preferredSize = new Dimension(400, 150);
        sliderPanel.setPreferredSize(preferredSize);

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

        if (result == JOptionPane.OK_OPTION) {

            minimum_reviews = reviewsSlider.getValue();
            displayAllProductions();
        }
    }



    private static void handleClearFiltersButtonClicked() {
        selectedGenres.clear();
        minimum_reviews = 0;
        displayAllProductions();
    }

    private static JPanel createProductionsPanel(List<Production> productions) {
        JPanel productionsPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        productionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Production production : productions) {
            ImageIcon productionIcon = getProductionImage(production);
            Image image = productionIcon.getImage();
            Image newimg = image.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            productionIcon = new ImageIcon(newimg);

            JButton productionButton = new JButton(productionIcon);
            productionButton.setPreferredSize(new Dimension(150, 200));

            productionButton.addActionListener(e -> {
                ImageIcon productionImageIcon = getProductionImage(production);
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

    private static void displayNotifications(Regular regular) {
        JPanel notificationsPanel = createNotificationsPanel(regular);

        JButton clearNotificationsButton = new JButton("Clear Notifications");
        clearNotificationsButton.addActionListener(e -> clearNotificationsButtonActionPerformed(e, regular));

        JPanel combinedPanel = new JPanel();
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.add(notificationsPanel);
        combinedPanel.add(Box.createVerticalStrut(10));
        combinedPanel.add(clearNotificationsButton);

        JScrollPane scrollPane = new JScrollPane(combinedPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        updateRightPanel(scrollPane);
    }

    private static void clearNotificationsButtonActionPerformed(ActionEvent evt, Regular regular) {
        regular.notifications.clear();
        Parser.updateNotifications(regular);
        displayNotifications(regular);
    }

    private static JPanel createNotificationsPanel(Regular regular) {
        JPanel notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.Y_AXIS));

        List<String> notifications = regular.notifications;

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
        JButton closeAppButton = new JButton("Close App");
        JButton loginButton = new JButton("Login");

        Dimension buttonSize = new Dimension(200, 40);
        closeAppButton.setPreferredSize(buttonSize);
        loginButton.setPreferredSize(buttonSize);

        JPanel logoutButtonsPanel = new JPanel(new GridBagLayout());
        logoutButtonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Add some padding

        JLabel instructionLabel = new JLabel("Close the app or login as a different user.");
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 10, 0);
        logoutButtonsPanel.add(instructionLabel, gbc);

        gbc.gridy++;
        logoutButtonsPanel.add(closeAppButton, gbc);

        gbc.gridy++;
        logoutButtonsPanel.add(loginButton, gbc);

        closeAppButton.addActionListener(e -> System.exit(0));

        loginButton.addActionListener(e -> {
            regularMenuFrame.setVisible(false);
            regularMenuFrame.dispose();
             IMDB.getInstance().login_GUI();
        });

        rightPanel.removeAll();

        rightPanel.add(logoutButtonsPanel, BorderLayout.CENTER);

        rightPanel.add(new JPanel(), BorderLayout.SOUTH);

        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private static void initializeSearchPage() {
        JTextField searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(300, 40));

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
                Image image1 = movieIcon.getImage();
                Image newimg1 = image1.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                movieIcon = new ImageIcon(newimg1);
                ItemPopup.showItemPopup((Movie) foundProduction, movieIcon, IMDB.getInstance().currentUser);
            } else if (foundProduction instanceof Series) {
                ImageIcon seriesIcon = new ImageIcon("src/series.jpg");
                Image image1 = seriesIcon.getImage();
                Image newimg1 = image1.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                seriesIcon = new ImageIcon(newimg1);
                ItemPopup.showItemPopup((Series) foundProduction, seriesIcon, IMDB.getInstance().currentUser);
            }
            return;
        }

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
        JPanel actorsPanel = createActorsPanel();
        JScrollPane scrollPane = new JScrollPane(actorsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        updateRightPanel(scrollPane);
    }

    public static JPanel createActorsPanel() {
        ArrayList<Actor> sortedActors = new ArrayList<>(IMDB.getInstance().actors);

        Collections.sort(sortedActors, Comparator.comparing(actor -> actor.name));

        JPanel actorsPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        actorsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Actor actor : sortedActors) {
            ImageIcon actorIcon = new ImageIcon("src/actor.jpg");
            Image image = actorIcon.getImage();
            Image newimg = image.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            actorIcon = new ImageIcon(newimg);

            JButton actorButton = new JButton(actorIcon);

            actorButton.addActionListener(e -> {
                ImageIcon actorImageIcon = new ImageIcon("src/actor.jpg");
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
                    rightPanel.removeAll();

                    rightPanel.add((JScrollPane) newContentPanel, BorderLayout.CENTER);

                    rightPanel.revalidate();
                    rightPanel.repaint();
                }
            });
        }
    }

}
