package org.example.view;

import org.example.controller.TurnstileController;
import java.util.Scanner;

public class TurnstileView {
    private final Scanner scanner;

    public TurnstileView() {
        this.scanner = new Scanner(System.in);
    }

    public void showMessage(String message) {
        System.out.println(message);
    }

    public void showError(String error) {
        System.out.println("Error: " + error);
    }

    public void showCardDetails(String details) {
        System.out.println(details);
    }

    public void showValidationResult(TurnstileController.ValidationResult result) {
        if (result.isSuccess()) {
            showMessage(result.getMessage());
            if (result.getDetails() != null) {
                showCardDetails(result.getDetails());
            }
        } else {
            showError(result.getMessage());
        }
    }

    public void showStatistics(TurnstileController.Statistics stats) {
        System.out.println("\n=== General Statistics ===");
        System.out.println("Total passes granted: " + stats.getTotalPasses());
        System.out.println("Total passes denied: " + stats.getTotalDenials());
        System.out.println("Total attempts: " + stats.getTotalAttempts());
        System.out.printf("Success rate: %.2f%%\n", stats.getSuccessRate());
    }

    public void showStatisticsByType(java.util.Map<String, TurnstileController.TypeStatistics> statsByType) {
        System.out.println("\n=== Statistics by Card Type ===");

        for (TurnstileController.TypeStatistics stats : statsByType.values()) {
            System.out.printf("\n%s Cards:\n", stats.getType());
            System.out.println("  Passes granted: " + stats.getPasses());
            System.out.println("  Passes denied: " + stats.getDenials());
            System.out.println("  Total attempts: " + stats.getTotalAttempts());
            System.out.printf("  Success rate: %.2f%%\n", stats.getSuccessRate());
        }
    }

    public void showMenu() {
        System.out.println("\n=== Tram Turnstile System ===");
        System.out.println("1. Issue card");
        System.out.println("2. Check card");
        System.out.println("3. Show statistics");
        System.out.println("4. Show statistics by type");
        System.out.println("5. Exit");
    }

    // Input methods with validation
    public String getInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }
            showError("Input cannot be empty. Please try again.");
        }
    }

    public int getIntInput(String prompt) {
        while (true) {
            String input = getInput(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                showError("Please enter a valid integer.");
            }
        }
    }

    public double getDoubleInput(String prompt) {
        while (true) {
            String input = getInput(prompt);
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                showError("Please enter a valid number.");
            }
        }
    }

    public String getCardTypeInput() {
        while (true) {
            String type = getInput("Enter card type (Student, Pupil, Regular): ");
            if (isValidCardType(type)) {
                return type;
            }
            showError("Invalid card type. Please enter Student, Pupil, or Regular.");
        }
    }

    public int getTripCountInput() {
        while (true) {
            int trips = getIntInput("Enter number of trips (5 or 10): ");
            if (trips == 5 || trips == 10) {
                return trips;
            }
            showError("Invalid number of trips. Only 5 or 10 are allowed.");
        }
    }

    public int getValidityPeriodInput() {
        while (true) {
            System.out.println("Choose validity period:");
            System.out.println("1. 10 days");
            System.out.println("2. 1 month (30 days)");
            int choice = getIntInput("Enter your choice (1 or 2): ");

            if (choice == 1) return 10;
            if (choice == 2) return 30;

            showError("Invalid choice. Please enter 1 or 2.");
        }
    }

    public double getBalanceInput() {
        while (true) {
            double balance = getDoubleInput("Enter initial balance: ");
            if (balance > 0) {
                return balance;
            }
            showError("Balance must be greater than 0.");
        }
    }

    public String getYesNoInput(String prompt) {
        while (true) {
            String input = getInput(prompt + " (y/n): ");
            if (input.equalsIgnoreCase("y") || input.equalsIgnoreCase("n")) {
                return input;
            }
            showError("Please enter 'y' for yes or 'n' for no.");
        }
    }

    private boolean isValidCardType(String type) {
        return type.equalsIgnoreCase("Student") ||
                type.equalsIgnoreCase("Pupil") ||
                type.equalsIgnoreCase("Regular");
    }

    public void close() {
        scanner.close();
    }
}