//package org.example;
import java.util.Scanner;

public class GUI implements UserInterface {
    private Scanner scanner;

    public GUI() {
        scanner = new Scanner(System.in);
    }

    @Override
    public String getInput() {
        return scanner.nextLine();
    }

    @Override
    public void displayOutput(String output) {
        System.out.println(output);
    }
}
