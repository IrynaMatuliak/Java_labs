package org.example;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static void printLine() {
        String line = String.valueOf('-').repeat(40);
        System.out.println(line);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String continueInput = "y";

        while (continueInput.equals("y") || continueInput.equals("yes")) {
            // Getting rows with checking for an empty array
            String[] inputArray;
            while (true) {
                System.out.print("Enter strings separated by commas: ");
                String input = scanner.nextLine().trim(); // Stores the result in the input variable

                if (input.isEmpty()) {
                    System.out.println("Input cannot be empty! Please enter at least one string.");
                    continue;
                }

                inputArray = input.split(","); // Splits a string into parts by commas, returns an array of strings between commas
                for (int i = 0; i < inputArray.length; i++) {
                    inputArray[i] = inputArray[i].trim();
                }

                // Removing blank lines after trim
                List<String> tempList = new ArrayList<>();
                for (String s : inputArray) {
                    if (!s.isEmpty()) {
                        tempList.add(s);
                    }
                }

                if (tempList.isEmpty()) {
                    System.out.println("No valid strings found! Please enter at least one non-empty string.");
                    continue;
                }

                inputArray = tempList.toArray(new String[tempList.size()]);
                break; // Exit the loop if everything is fine
            }

            // Getting option selection with validation
            int choice;
            while (true) {
                System.out.println("Choose option:");
                System.out.println("1 - Strings with length less than average");
                System.out.println("2 - Strings with length greater than average");
                System.out.println("3 - Both less and greater than average");
                System.out.print("Your choice (1/2/3): ");

                try {
                    choice = Integer.parseInt(scanner.nextLine().trim());
                    if (choice >= 1 && choice <= 3) {
                        break;
                    } else {
                        System.out.println("Invalid choice! Please enter 1, 2 or 3");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number 1, 2 or 3");
                }
            }

            double averageLength = StringLengthAnalyzer.calculateAverageLength(inputArray);
            System.out.printf("Average length: %.2f%n", averageLength);

            switch (choice) {
                case 1:
                    String[] lessThanAverage = StringLengthAnalyzer.findStringsLessThanAverage(inputArray);
                    System.out.println("Strings with length less than average:");
                    printArray(lessThanAverage);
                    break;
                case 2:
                    String[] greaterThanAverage = StringLengthAnalyzer.findStringsGreaterThanAverage(inputArray);
                    System.out.println("Strings with length greater than average:");
                    printArray(greaterThanAverage);
                    break;
                case 3:
                    String[] lessResult = StringLengthAnalyzer.findStringsLessThanAverage(inputArray);
                    String[] greaterResult = StringLengthAnalyzer.findStringsGreaterThanAverage(inputArray);

                    System.out.println("Strings with length less than average:");
                    printArray(lessResult);

                    System.out.println("Strings with length greater than average:");
                    printArray(greaterResult);
                    break;
            }

            System.out.print("Do you want to run program again? (y/n): ");
            continueInput = scanner.nextLine().trim().toLowerCase();

            printLine();
        }

        scanner.close();

        if (!continueInput.equals("n") && !continueInput.equals("no")) {
            System.out.println("Will exit program as input is invalid");
        }
    }

    private static void printArray(String[] array) {
        if (array.length == 0) {
            System.out.println("No strings found");
        } else {
            for (String str : array) {
                System.out.println("- " + str + " (length: " + str.length() + ")");
            }
        }
    }
}