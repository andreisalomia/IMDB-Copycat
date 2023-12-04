//package org.example;
import java.util.Scanner;


public class TerminalUI implements UserInterface {

    private Scanner scanner;

    public TerminalUI() {
        scanner = new Scanner(System.in);
    }

    @Override
    public String getInput() {
        return scanner.nextLine();
    }

    public int getNumber() {
        int number = scanner.nextInt();
        scanner.nextLine();
        return number;
    }

    public String getEmail() {
        System.out.print("\tEmail: ");
        return scanner.nextLine();
    }

    public String getPassword() {
        System.out.print("\tPassword: ");
        return scanner.nextLine();
    }

    @Override
    public void displayOutput(String output) {
        System.out.print(output);
    }
}
