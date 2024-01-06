import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GenreFilterDialog extends JDialog {
    private static final int DIALOG_WIDTH = 300; // Set your preferred width
    private static final int DIALOG_HEIGHT = 600; // Set your preferred height

    private List<Genre> selectedGenres;

    public GenreFilterDialog(Frame parent) {
        super(parent, "Genre Filter", true);
        selectedGenres = new ArrayList<>();

        // Create checkboxes for each genre
        JPanel checkboxesPanel = new JPanel();
        checkboxesPanel.setLayout(new GridLayout(0, 1)); // One column

        for (Genre genre : Genre.values()) {
            JCheckBox checkBox = new JCheckBox(genre.toString());
            checkBox.addActionListener(e -> handleCheckBoxClicked(checkBox, genre));
            checkboxesPanel.add(checkBox);
        }

        // Create OK and Cancel buttons
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");

        okButton.addActionListener(e -> handleOkButtonClicked());
        cancelButton.addActionListener(e -> handleCancelButtonClicked());

        // Create a panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Set layout for the dialog
        setLayout(new BorderLayout());
        add(checkboxesPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void handleCheckBoxClicked(JCheckBox checkBox, Genre genre) {
        if (checkBox.isSelected()) {
            selectedGenres.add(genre);
        } else {
            selectedGenres.remove(genre);
        }
    }

    private void handleOkButtonClicked() {
        dispose(); // Close the dialog
    }

    private void handleCancelButtonClicked() {
        selectedGenres.clear(); // Clear the selected genres
        dispose(); // Close the dialog
    }

    public List<Genre> getSelectedGenres() {
        return selectedGenres;
    }
}
