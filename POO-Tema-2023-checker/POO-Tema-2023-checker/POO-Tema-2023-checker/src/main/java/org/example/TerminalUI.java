import java.util.Scanner;
import java.io.Console;

public class TerminalUI implements UserInterface {
    private Console console;
    private Scanner scanner;

    public TerminalUI() {
        this.console = System.console();
        if (console == null) {
            // If console is null, it might be running in an IDE or non-interactive environment
            throw new UnsupportedOperationException("Console not available");
        }
    }

    @Override
    public String getInput() {
        return scanner.nextLine();
    }

    public String getEmail() {
        System.out.print("Email: ");
        return scanner.nextLine();
    }

    public String getPassword() {
        System.out.print("Password: ");
        return scanner.nextLine();
    }

    @Override
    public void displayOutput(String output) {
        System.out.println(output);
    }
}
