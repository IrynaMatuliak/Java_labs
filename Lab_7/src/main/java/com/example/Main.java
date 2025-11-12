package com.example;

import java.util.Scanner;
import java.util.Arrays;

public class Main {
    private static void printLine() {
        System.out.println("-".repeat(40));
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String continueInput = "y";
        while (continueInput.equals("y") || continueInput.equals("yes")) {
            String[] inputArray = getValidInputArray(scanner);
            if (inputArray == null) {
                continue;
            }
            int choice = getValidChoice(scanner);
            if (choice == -1) {
                continue;
            }
            processChoice(inputArray, choice);
            continueInput = getContinueInput(scanner);
            printLine();
        }
        scanner.close();
        System.out.println("Program finished.");
    }

    private static String[] getValidInputArray(Scanner scanner) {
        while (true) {
            System.out.print("Enter strings separated by commas: ");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty! Please enter at least one string.");
                continue;
            }
            String[] inputArray = Arrays.stream(input.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
            if (inputArray.length == 0) {
                System.out.println("No valid strings found! Please enter at least one non-empty string.");
                continue;
            }
            return inputArray;
        }
    }

    private static int getValidChoice(Scanner scanner) {
        while (true) {
            System.out.println("Choose option:");
            System.out.println("1 - Strings with length less than average");
            System.out.println("2 - Strings with length greater than average");
            System.out.println("3 - Both less and greater than average");
            System.out.print("Your choice (1/2/3): ");
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= 3) {
                    return choice;
                }
                System.out.println("Invalid choice! Please enter 1, 2 or 3");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number 1, 2 or 3");
            }
        }
    }

    private static void processChoice(String[] inputArray, int choice) {
        double averageLength = StringLengthAnalyzer.calculateAverageLength(inputArray);
        System.out.printf("Average length: %.2f%n", averageLength);
        switch (choice) {
            case 1:
                String[] lessThanAverage = StringLengthAnalyzer.findStringsLessThanAverage(inputArray);
                System.out.println("Strings with length less than average:");
                printArray(lessThanAverage);
            case 2:
                String[] greaterThanAverage = StringLengthAnalyzer.findStringsGreaterThanAverage(inputArray);
                System.out.println("Strings with length greater than average:");
                printArray(greaterThanAverage);
            case 3:
                String[] lessResult = StringLengthAnalyzer.findStringsLessThanAverage(inputArray);
                String[] greaterResult = StringLengthAnalyzer.findStringsGreaterThanAverage(inputArray);
                System.out.println("Strings with length less than average:");
                printArray(lessResult);
                System.out.println("Strings with length greater than average:");
                printArray(greaterResult);
        }
    }

    private static String getContinueInput(Scanner scanner) {
        System.out.print("Do you want to run program again? (y/n): ");
        return scanner.nextLine().trim().toLowerCase();
    }

    private static void printArray(String[] array) {
        if (array.length == 0) {
            System.out.println("No strings found");
        } else {
            Arrays.stream(array).forEach(str -> System.out.println("- " + str + " (length: " + str.length() + ")"));
        }
    }
}