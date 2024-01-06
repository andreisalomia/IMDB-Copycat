import javax.swing.*;

public class GUI implements UserInterface {
    @Override
    public String getInput() {
        return JOptionPane.showInputDialog(null, "Enter input:");
    }

    @Override
    public void displayOutput(String output) {
        JOptionPane.showMessageDialog(null, output);
    }
}