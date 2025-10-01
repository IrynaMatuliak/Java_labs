package org.example;

import java.time.LocalDate;
import java.util.Scanner;

class Main {
    private static final Turnstile turnstile = new Turnstile();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Tram Turnstile System!");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Issue card");
            System.out.println("2. Check card");
            System.out.println("3. Show statistics");
            System.out.println("4. Show statistics by type");
            System.out.println("5. Exit");
            System.out.print("Choose option: ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: Input cannot be empty. Please try again.");
                continue;
            }

            try {
                int choice = Integer.parseInt(input);

                switch (choice) {
                    case 1:
                        issueCard();
                        break;
                    case 2:
                        checkCard();
                        break;
                    case 3:
                        turnstile.showStatistics();
                        break;
                    case 4:
                        turnstile.showStatisticsByType();
                        break;
                    case 5:
                        System.out.println("Exiting program.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }

    private static void issueCard() {
        String type;
        while (true) {
            System.out.print("Enter card type (Student, Pupil, Regular): ");
            type = scanner.nextLine().trim();

            if (type.isEmpty()) {
                System.out.println("Error: Card type cannot be empty. Please try again.");
                continue;
            }

            if (isValidCardType(type)) {
                break;
            } else {
                System.out.println("Invalid card type. Please enter Student, Pupil, or Regular.");
            }
        }

        String id = getUniqueCardId();

        if (type.equalsIgnoreCase("Regular")) {
            issueRegularCard(id);
        } else {
            issueTripBasedCard(id, type);
        }
    }

    private static String getUniqueCardId() {
        while (true) {
            System.out.print("Enter unique card ID: ");
            String id = scanner.nextLine().trim();

            if (id.isEmpty()) {
                System.out.println("Error: Card ID cannot be empty. Please try again.");
                continue;
            }

            if (!turnstile.isCardIdExists(id)) {
                return id;
            } else {
                System.out.println("This ID is already in use. Please try another one.");
            }
        }
    }

    private static void issueRegularCard(String id) {
        String isAccumulating;
        while (true) {
            System.out.print("Is this an accumulating card? (y/n): ");
            isAccumulating = scanner.nextLine().trim();

            if (isAccumulating.isEmpty()) {
                System.out.println("Error: Input cannot be empty. Please enter 'y' or 'n'.");
                continue;
            }

            if (isAccumulating.equalsIgnoreCase("y") || isAccumulating.equalsIgnoreCase("n")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'y' for yes or 'n' for no.");
            }
        }

        if (isAccumulating.equalsIgnoreCase("y")) {
            double balance = getValidBalance();
            Card card = new AccumulatingCard(id, balance);
            turnstile.issueCard(card);
            System.out.println("Accumulating card issued: " + card);
        } else {
            issueTripBasedCard(id, "Regular");
        }
    }

    private static void issueTripBasedCard(String id, String type) {
        int trips = getValidTripCount();
        int validityDays = getValidityPeriod();
        LocalDate expiryDate = LocalDate.now().plusDays(validityDays);

        Card card = new TripBasedCard(id, type, expiryDate, trips);
        turnstile.issueCard(card);
        System.out.println("Card issued: " + card);
        System.out.println("Expiry date: " + expiryDate);
    }

    private static boolean isValidCardType(String type) {
        return type.equalsIgnoreCase("Student") || type.equalsIgnoreCase("Pupil") || type.equalsIgnoreCase("Regular");
    }

    private static int getValidTripCount() {
        while (true) {
            System.out.print("Enter number of trips (5 or 10): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: Number of trips cannot be empty. Please try again.");
                continue;
            }

            try {
                int trips = Integer.parseInt(input);
                if (trips == 5 || trips == 10) {
                    return trips;
                } else {
                    System.out.println("Invalid number of trips. Only 5 or 10 are allowed.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid integer.");
            }
        }
    }

    private static int getValidityPeriod() {
        while (true) {
            System.out.println("Choose validity period:");
            System.out.println("1. 10 days");
            System.out.println("2. 1 month (30 days)");
            System.out.print("Enter your choice (1 or 2): ");

            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: Choice cannot be empty. Please try again.");
                continue;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice == 1) {
                    return 10;
                } else if (choice == 2) {
                    return 30;
                } else {
                    System.out.println("Invalid choice. Please enter 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number (1 or 2).");
            }
        }
    }

    private static double getValidBalance() {
        while (true) {
            System.out.print("Enter initial balance: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: Balance cannot be empty. Please try again.");
                continue;
            }

            try {
                double balance = Double.parseDouble(input);
                if (balance > 0) {
                    return balance;
                } else {
                    System.out.println("Balance must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid amount.");
            }
        }
    }

    private static void checkCard() {
        while (true) {
            System.out.print("Enter card ID: ");
            String id = scanner.nextLine().trim();

            if (id.isEmpty()) {
                System.out.println("Error: Card ID cannot be empty. Please try again.");
                continue;
            }

            turnstile.validateCard(id);
            break;
        }
    }
}