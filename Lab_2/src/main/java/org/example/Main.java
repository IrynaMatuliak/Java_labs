package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ConsoleInputValidator inputHandler = new ConsoleInputValidator();
        CuratorJournal journal = new CuratorJournal();
        Scanner scanner = new Scanner(System.in);

        try {
            boolean running = true;

            while (running) {
                displayMenu();
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        JournalEntry newEntry = inputHandler.inputJournalEntry();
                        journal.addEntry(newEntry);
                        System.out.println("\nEntry successfully added to journal!");
                        break;

                    case "2":
                        journal.displayAllEntries();
                        break;

                    case "3":
                        running = false;
                        System.out.println("Thank you for using the application. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } finally {
            inputHandler.close();
            scanner.close();
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== CURATOR JOURNAL ===");
        System.out.println("1. Add new entry");
        System.out.println("2. Show all entries");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }
}