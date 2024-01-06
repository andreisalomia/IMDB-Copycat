import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginGUI extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel feedbackLabel;
    private JLabel welcomeLabel;
    private List<User> users;

    public LoginGUI(List<User> users) {
        this.users = users;

        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        initializeComponents();
    }

    private void initializeComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding

        welcomeLabel = new JLabel("Welcome back to IMDB! Please enter your email and password to login.");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(emailLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(passwordField, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });

        feedbackLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(feedbackLabel, gbc);

        add(panel);
        pack(); // Resize the frame to fit its contents
    }

    private void performLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        User user = authenticateUser(email, password);

        if (user != null) {
            feedbackLabel.setText("Login successful! Welcome back, " + user.username + "!");
            welcomeLabel.setVisible(false);
            setVisible(false);
            IMDB.getInstance().currentUser = user;
            // Proceed with further program logic here
            IMDB.getInstance().startPage(user);
        } else {
            feedbackLabel.setText("Invalid credentials. Please try again.");
            this.initializeComponents();
        }
    }

    private User authenticateUser(String email, String password) {
        for (User user : users) {
            Credentials credentials = user.userInformation.getCredentials();
            if (credentials.getEmail().equals(email) && credentials.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }
}
