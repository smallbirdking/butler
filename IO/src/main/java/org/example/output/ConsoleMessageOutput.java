package org.example.output;

public class ConsoleMessageOutput {

    private ConsoleMessageOutput() {
        // private constructor
    }

    public static void chat(String message) {
        System.out.println("Res : ");
        display(message);
    }

    public static void display(String message) {
        System.out.println(message);
    }
}