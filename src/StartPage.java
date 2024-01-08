import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class StartPage {
    public static JPanel rightPanel;
    public static JPanel leftPanel;

    public static void startPage(User user) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("IMDB App - " + user.accountType + " Start Page");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1024, 768);
            frame.setLocationRelativeTo(null);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerSize(5);
            splitPane.setDividerLocation(280);

            JPanel userDetailsPanel = new JPanel(new GridLayout(3, 1));
            JLabel welcomeLabel = new JLabel("Welcome, " + user.userInformation.name + "!");
            JLabel usernameLabel = new JLabel("Username: " + user.username);
            JLabel userDetailsLabel = new JLabel("User experience: " + user.experience);
            welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            usernameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            userDetailsLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            usernameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            userDetailsLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            userDetailsPanel.add(welcomeLabel);
            userDetailsPanel.add(usernameLabel);
            userDetailsPanel.add(userDetailsLabel);

            JPanel userPicturePanel = new JPanel(new FlowLayout());
            ImageIcon userIcon = new ImageIcon("src/no_image_user.jpg");
            Image image = userIcon.getImage();
            Image newimg = image.getScaledInstance(200, 280, Image.SCALE_SMOOTH);
            userIcon = new ImageIcon(newimg);
            JLabel userImageLabel = new JLabel(userIcon);
            userPicturePanel.add(userImageLabel);

            JPanel buttonsPanel = new JPanel(new GridLayout(3, 1));
            JButton recommendedButton = new JButton("Recommended");
            JButton actorsButton = new JButton("Actors");
            JButton menuButton = new JButton("Menu");
            recommendedButton.setPreferredSize(new Dimension(150, 100));
            actorsButton.setPreferredSize(new Dimension(150, 100));
            menuButton.setPreferredSize(new Dimension(150, 100));
            recommendedButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            actorsButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            menuButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            recommendedButton.setFont(new Font("Arial", Font.PLAIN, 20));
            actorsButton.setFont(new Font("Arial", Font.PLAIN, 20));
            menuButton.setFont(new Font("Arial", Font.PLAIN, 20));
            recommendedButton.setBackground(Color.WHITE);
            actorsButton.setBackground(Color.WHITE);
            menuButton.setBackground(Color.WHITE);

            recommendedButton.addActionListener(e -> displayRecommendedContent());

            actorsButton.addActionListener(e -> displayAllActors());

            menuButton.addActionListener((ActionEvent e) -> {
                if (user instanceof Regular) {
                    frame.setVisible(false);
                    frame.dispose();
                    RegularMenu.initializeMenu((Regular) user);
                } else if (user instanceof Contributor) {
                    frame.setVisible(false);
                    frame.dispose();
                    ContributorMenu.initializeMenu((Contributor) user);
                } else if (user instanceof Admin) {
                    frame.setVisible(false);
                    frame.dispose();
                    AdminMenu.initializeMenu((Admin) user);
                }
            });

            buttonsPanel.add(recommendedButton);
            buttonsPanel.add(actorsButton);
            buttonsPanel.add(menuButton);

            leftPanel = new JPanel(new BorderLayout());
            leftPanel.add(userDetailsPanel, BorderLayout.NORTH);
            leftPanel.add(userPicturePanel, BorderLayout.CENTER);
            leftPanel.add(buttonsPanel, BorderLayout.SOUTH);

            rightPanel = new JPanel(new BorderLayout());

            splitPane.setLeftComponent(leftPanel);
            splitPane.setRightComponent(rightPanel);

            frame.add(splitPane);
            frame.setVisible(true);
        });
    }

    private static void displayAllActors() {
        List<Actor> actors = IMDB.getInstance().actors;

        JPanel actorsPanel = createActorsPanel();

        JScrollPane scrollPane = new JScrollPane(actorsPanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        rightPanel.removeAll();
        rightPanel.add(scrollPane, BorderLayout.CENTER);
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    private static JPanel createActorsPanel() {
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


    private static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(newimg);
    }

    private static void displayRecommendedContent() {
        JTextField searchBar = new JTextField();
        searchBar.setPreferredSize(new Dimension(200, 30));

        JLabel searchBarLabel = new JLabel("Search:");
        searchBarLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchBarPanel.add(searchBarLabel);
        searchBarPanel.add(searchBar);

        JLabel recommendedTitleLabel = new JLabel("Recommended Actors/Productions");
        recommendedTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        recommendedTitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel recommendedTitlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        recommendedTitlePanel.add(recommendedTitleLabel);

        JPanel recommendedActorsPanel = createRecommendedPanel(IMDB.getInstance().actors);

        JPanel recommendedProductionsPanel = createRecommendedPanel(IMDB.getInstance().productions);

        JPanel newContentPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        newContentPanel.add(searchBarPanel, gbc);

        gbc.gridy++;
        newContentPanel.add(recommendedTitlePanel, gbc);

        gbc.gridy++;
        newContentPanel.add(recommendedActorsPanel, gbc);

        gbc.gridy++;
        newContentPanel.add(recommendedProductionsPanel, gbc);

        searchBar.addActionListener(e -> {
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
                if(foundProduction instanceof Movie){
                    ImageIcon movieIcon = new ImageIcon("src/movie.jpg");
                    Image image1 = movieIcon.getImage();
                    Image newimg1 = image1.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                    movieIcon = new ImageIcon(newimg1);
                    ItemPopup.showItemPopup((Movie) foundProduction, movieIcon, IMDB.getInstance().currentUser);
                }
                else if(foundProduction instanceof Series){
                    ImageIcon seriesIcon = new ImageIcon("src/series.jpg");
                    Image image1 = seriesIcon.getImage();
                    Image newimg1 = image1.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                    seriesIcon = new ImageIcon(newimg1);
                    ItemPopup.showItemPopup((Series) foundProduction, seriesIcon, IMDB.getInstance().currentUser);
                }
                return;
            }

            JOptionPane.showMessageDialog(null, "No actor/production found with the given name.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
        });

        updateRightPanel(newContentPanel);
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


    private static JPanel createRecommendedPanel(java.util.List<?> items) {
        JPanel recommendedPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        int[] randomNumbers = new Random().ints(0, items.size()).distinct().limit(3).toArray();

        for (int i = 0; i < 3; i++) {
            Object item = items.get(randomNumbers[i]);
            ImageIcon itemIcon = getItemIcon(item);

            Image image = itemIcon.getImage();
            Image newimg = image.getScaledInstance(150, 200, Image.SCALE_SMOOTH);
            itemIcon = new ImageIcon(newimg);

            JButton itemButton = new JButton(itemIcon);

            itemButton.addActionListener(e -> {
                if (item instanceof Actor) {
                    Actor actor = (Actor) item;
                    ImageIcon actorIcon = new ImageIcon("src/actor.jpg");
                    Image image1 = actorIcon.getImage();
                    Image newimg1 = image1.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                    actorIcon = new ImageIcon(newimg1);
                    ItemPopup.showItemPopup(actor, actorIcon, IMDB.getInstance().currentUser);
                } else if (item instanceof Movie) {
                    Movie movie = (Movie) item;
                    ImageIcon movieIcon = new ImageIcon("src/movie.jpg");
                    Image image1 = movieIcon.getImage();
                    Image newimg1 = image1.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                    movieIcon = new ImageIcon(newimg1);
                    ItemPopup.showItemPopup(movie, movieIcon,   IMDB.getInstance().currentUser);
                } else if (item instanceof Series) {
                    Series series = (Series) item;
                    ImageIcon seriesIcon = new ImageIcon("src/series.jpg");
                    Image image1 = seriesIcon.getImage();
                    Image newimg1 = image1.getScaledInstance(200, 320, Image.SCALE_SMOOTH);
                    seriesIcon = new ImageIcon(newimg1);
                    ItemPopup.showItemPopup(series, seriesIcon, IMDB.getInstance().currentUser);
                }
            });

            JLabel itemNameLabel = new JLabel(getItemName(item));
            itemNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.add(itemButton, BorderLayout.CENTER);
            itemPanel.add(itemNameLabel, BorderLayout.SOUTH);

            gbc.gridx = i;
            recommendedPanel.add(itemPanel, gbc);
        }

        return recommendedPanel;
    }

    private static void searchForActorMovieSeries(String input) {
        IMDB imdb = IMDB.getInstance();
        try {
            for (Production prod : imdb.productions) {
                if (prod.title.equals(input)) {
                    if(prod instanceof Movie)
                        ItemPopup.showItemPopup((Movie) prod, new ImageIcon("src/movie_icon.jpg"), IMDB.getInstance().currentUser);
                    else if(prod instanceof Series)
                        ItemPopup.showItemPopup((Series) prod, new ImageIcon("src/series_icon.jpg"), IMDB.getInstance().currentUser);
                    return;
                }
            }
            for (Actor actor : imdb.actors) {
                if (actor.name.equals(input)) {
                    ItemPopup.showItemPopup(actor, new ImageIcon("src/actor_icon.jpg"), IMDB.getInstance().currentUser);
                    return;
                }
            }
            throw new Exception();
        } catch (Exception e) {
            IMDB.getInstance().userInterface.displayOutput("No actor/movie/series found with the given name.\n");
        }
    }

    private static ImageIcon getItemIcon(Object item) {
        if (item instanceof Actor) {
            return new ImageIcon("src/actor.jpg");
        } else if (item instanceof Production) {
            if (item instanceof Series) {
                return new ImageIcon("src/series.jpg");
            } else {
                return new ImageIcon("src/movie.jpg");
            }
        } else {
            return new ImageIcon("src/no_image_user.jpg");
        }
    }

    private static String getItemName(Object item) {
        if (item instanceof Actor) {
            return ((Actor) item).name;
        } else if (item instanceof Production) {
            return ((Production) item).title;
        }
        return "";
    }



    private static void updateRightPanel(JPanel newContentPanel) {
        SwingUtilities.invokeLater(() -> {
            if (rightPanel != null) {
                rightPanel.removeAll();

                rightPanel.add(newContentPanel);

                rightPanel.revalidate();
                rightPanel.repaint();
            }
        });
    }

    public static void startAdminPage(Admin admin) {
        startPage(admin);
    }

    public static void startContributorPage(Contributor contributor) {
        startPage(contributor);
    }

    public static void startRegularPage(Regular regular) {
        startPage(regular);
    }
}
