package org.example.input;

import java.util.Scanner;

public class ConsoleMessageInput {

    private ConsoleMessageInput() {
        // private constructor
    }

    public static void startDialogue() {
        System.out.println("Starting dialogue...");
    }

    public static String consoleInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your message: ");
        String message = "";
        if (scanner.hasNext()){
            message = scanner.nextLine();
        }
        System.out.println();
        if (message.trim().isEmpty()) {
            System.out.println("You did not enter a message.");
        }
        scanner.close();
        return message;
    }

}
