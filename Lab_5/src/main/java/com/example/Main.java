package com.example;

import java.io.*;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static FileCipherService cipherService = new FileCipherService();

    public static void main(String[] args) {
        System.out.println("=== Text Data Processing Program ===");
        boolean running = true;

        while (running) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. Find line with maximum word count");
            System.out.println("2. File encryption/decryption");
            System.out.println("3. Count HTML tags on web page");
            System.out.println("4. Exit");
            System.out.print("Choose option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        processFileWords();
                        break;
                    case 2:
                        processCipher();
                        break;
                    case 3:
                        processTagCounter();
                        break;
                    case 4:
                        running = false;
                        System.out.println("Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: please enter a valid number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void processFileWords() {
        try {
            System.out.print("Enter path to text file: ");
            String filePath = scanner.nextLine();

            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("File not found: " + filePath);
                return;
            }

            FileProcessor processor = new FileProcessor(filePath);
            processor.readFile();

            String maxLine = processor.findLineWithMaxWords();
            if (maxLine != null) {
                System.out.println("\n=== Search Results ===");
                System.out.println("Line with maximum word count:");
                System.out.println("\"" + maxLine + "\"");
                System.out.println("Word count: " + maxLine.trim().split("\\s+").length);
            } else {
                System.out.println("File is empty or contains no text.");
            }

            System.out.print("\nSave object to file? (y/n): ");
            String saveChoice = scanner.nextLine();
            if (saveChoice.equalsIgnoreCase("y")) {
                System.out.print("Enter filename for saving: ");
                String saveFile = scanner.nextLine();
                processor.saveToFile(saveFile);
                System.out.println("Object saved to file: " + saveFile);
            }
        } catch (IOException e) {
            System.out.println("File operation error: " + e.getMessage());
        }
    }

    private static void processCipher() {
        try {
            System.out.println("\n=== Encryption/Decryption ===");
            System.out.println("1. File encryption");
            System.out.println("2. File decryption");
            System.out.println("3. Text file encryption (character streams)");
            System.out.println("4. Text file decryption (character streams)");
            System.out.print("Choose option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter input file path: ");
            String inputFile = scanner.nextLine();
            File file = new File(inputFile);
            if (!file.exists()) {
                System.out.println("File not found: " + inputFile);
                return;
            }

            System.out.print("Enter output file path: ");
            String outputFile = scanner.nextLine();

            System.out.print("Enter key character: ");
            String keyInput = scanner.nextLine();
            if (keyInput.isEmpty()) {
                System.out.println("Error: key cannot be empty.");
                return;
            }
            char key = keyInput.charAt(0);

            // Використовуємо FileCipherService замість FileProcessor для шифрування
            switch (choice) {
                case 1:
                    cipherService.encryptFile(inputFile, outputFile, key);
                    System.out.println("File encrypted successfully (byte streams).");
                    break;
                case 2:
                    cipherService.decryptFile(inputFile, outputFile, key);
                    System.out.println("File decrypted successfully (byte streams).");
                    break;
                case 3:
                    cipherService.encryptTextFile(inputFile, outputFile, key);
                    System.out.println("Text file encrypted successfully (character streams).");
                    break;
                case 4:
                    cipherService.decryptTextFile(inputFile, outputFile, key);
                    System.out.println("Text file decrypted successfully (character streams).");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: please enter a valid number.");
        } catch (IOException e) {
            System.out.println("File operation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void processTagCounter() {
        try {
            System.out.println("\n=== HTML Tag Counter ===");
            System.out.print("Enter web page URL: ");
            String url = scanner.nextLine();

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
            }

            TagCounter counter = new TagCounter(url);
            System.out.println("Analyzing page...");
            counter.analyzeHTML();

            counter.printByTagName();
            counter.printByFrequency();
            System.out.print("\nSave results to file? (y/n): ");
            String saveChoice = scanner.nextLine();
            if (saveChoice.equalsIgnoreCase("y")) {
                System.out.print("Enter filename for saving: ");
                String saveFile = scanner.nextLine();
                counter.saveResultsToFile(saveFile);
                System.out.println("Results saved to file: " + saveFile);
            }
        } catch (IOException e) {
            System.out.println("URL access error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}