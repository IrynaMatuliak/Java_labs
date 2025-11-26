package com.example;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static FileCipherService cipherService = new FileCipherService();
    private static ResourceBundle messages;
    private static Logger logger;

    static {
        setupLogger();
        setLanguage("en");
    }

    public static void main(String[] args) {
        logger.info("Program started");
        System.out.println("=== " + messages.getString("menu.title") + " ===");
        boolean running = true;

        while (running) {
            System.out.println("\n=== " + messages.getString("menu.main") + " ===");
            System.out.println("1. " + messages.getString("menu.option1"));
            System.out.println("2. " + messages.getString("menu.option2"));
            System.out.println("3. " + messages.getString("menu.option3"));
            System.out.println("4. " + messages.getString("menu.option4"));
            System.out.println("5. " + messages.getString("menu.option5"));
            System.out.print(messages.getString("menu.choose_option") + " ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());
                logger.fine("User selected option: " + choice);

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
                        System.out.println(messages.getString("menu.goodbye"));
                        logger.info("Program terminated by user");
                        break;
                    case 5:
                        changeLanguage();
                        break;
                    default:
                        System.out.println(messages.getString("menu.invalid_choice"));
                        logger.warning("Invalid user choice: " + choice);
                }
            } catch (NumberFormatException e) {
                System.out.println(messages.getString("error.number_format"));
                logger.warning("Number format exception: " + e.getMessage());
            } catch (Exception e) {
                System.out.println(messages.getString("error.general") + " " + e.getMessage());
                logger.severe("Unexpected error: " + e.getMessage());
            }
        }
    }

    private static void setupLogger() {
        logger = Logger.getLogger(Main.class.getName());
        logger.setLevel(Level.ALL);

        try {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.WARNING);
            consoleHandler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%1$tT] [%2$-7s] %3$s%n",
                            new Date(record.getMillis()),
                            record.getLevel().getName(),
                            record.getMessage());
                }
            });
            logger.addHandler(consoleHandler);

            FileHandler mainFileHandler = new FileHandler("application.log", true);
            mainFileHandler.setLevel(Level.ALL);
            mainFileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%1$tF %1$tT] [MAIN_FILE] [%2$-7s] %3$s%n",
                            new Date(record.getMillis()),
                            record.getLevel().getName(),
                            record.getMessage());
                }
            });
            logger.addHandler(mainFileHandler);

            FileHandler textFileHandler = new FileHandler("messages.txt", true);
            textFileHandler.setLevel(Level.INFO);
            textFileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%1$tF %1$tT] [TEXT_FILE] [%2$-7s] %3$s%n",
                            new Date(record.getMillis()),
                            record.getLevel().getName(),
                            record.getMessage());
                }
            });
            logger.addHandler(textFileHandler);
        } catch (IOException e) {
            System.err.println("Failed to setup logger: " + e.getMessage());
        }
    }

    private static void setLanguage(String language) {
        try {
            messages = ResourceBundle.getBundle("location.messages", new Locale(language));
            logger.config("Language set to: " + language);
        } catch (MissingResourceException e) {
            System.err.println("Resource bundle not found for language: " + language);
            logger.severe("Failed to load resource bundle for language: " + language);
        }
    }

    private static void changeLanguage() {
        System.out.println("\n=== " + messages.getString("menu.language_selection") + " ===");
        System.out.println("1. " + messages.getString("menu.language_english"));
        System.out.println("2. " + messages.getString("menu.language_ukrainian"));
        System.out.print(messages.getString("menu.choose_option") + " ");

        try {
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    setLanguage("en");
                    System.out.println("Language changed to English");
                    logger.fine("Language changed to English");
                    break;
                case 2:
                    setLanguage("uk");
                    System.out.println("Мову змінено на українську");
                    logger.fine("Language changed to Ukrainian");
                    break;
                default:
                    System.out.println(messages.getString("menu.invalid_choice"));
                    logger.warning("Invalid language choice: " + choice);
            }
        } catch (NumberFormatException e) {
            System.out.println(messages.getString("error.number_format"));
            logger.warning("Number format error in language selection: " + e.getMessage());
        }
    }

    private static void processFileWords() {
        logger.entering("Main", "processFileWords");
        try {
            System.out.print(messages.getString("file.enter_path") + " ");
            String filePath = scanner.nextLine();

            logger.finest("User entered file path: " + filePath);
            logger.finer("Starting file processing");
            logger.fine("Processing file: " + filePath);
            logger.config("File processor initialized");

            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println(messages.getString("file.not_found") + " " + filePath);
                logger.warning("File not found: " + filePath);
                return;
            }

            FileProcessor processor = new FileProcessor(filePath);
            processor.readFile();
            logger.info("File read successfully: " + filePath);

            String maxLine = processor.findLineWithMaxWords();
            if (maxLine != null) {
                int wordCount = maxLine.trim().split("\\s+").length;
                System.out.println("\n=== " + messages.getString("file.search_results") + " ===");
                System.out.println(messages.getString("file.max_line"));
                System.out.println("\"" + maxLine + "\"");
                System.out.println(messages.getString("file.word_count") + " " + wordCount);
                logger.info("Found line with max words: " + wordCount + " words");
            } else {
                System.out.println(messages.getString("file.empty"));
                logger.info("File is empty: " + filePath);
            }

            System.out.print("\n" + messages.getString("file.save_object") + " ");
            String saveChoice = scanner.nextLine();
            if (saveChoice.equalsIgnoreCase("y")) {
                System.out.print(messages.getString("file.object_saved") + " ");
                String saveFile = scanner.nextLine();
                processor.saveToFile(saveFile);
                System.out.println(messages.getString("file.object_saved") + " " + saveFile);
                logger.info("Object saved to file: " + saveFile);
            } else {
                logger.fine("User chose not to save the object");
            }
        } catch (IOException e) {
            System.out.println(messages.getString("file.operation_error") + " " + e.getMessage());
            logger.severe("File operation error: " + e.getMessage());
        } finally {
            logger.exiting("Main", "processFileWords");
        }
    }

    private static void processCipher() {
        logger.entering("Main", "processCipher");
        try {
            System.out.println("\n=== " + messages.getString("cipher.title") + " ===");
            System.out.println("1. " + messages.getString("cipher.option1"));
            System.out.println("2. " + messages.getString("cipher.option2"));
            System.out.println("3. " + messages.getString("cipher.option3"));
            System.out.println("4. " + messages.getString("cipher.option4"));
            System.out.print(messages.getString("menu.choose_option") + " ");
            int choice = Integer.parseInt(scanner.nextLine());
            logger.fine("Cipher operation selected: " + choice);

            System.out.print(messages.getString("cipher.input_file") + " ");
            String inputFile = scanner.nextLine();
            File file = new File(inputFile);
            if (!file.exists()) {
                System.out.println(messages.getString("file.not_found") + " " + inputFile);
                logger.warning("Input file not found: " + inputFile);
                return;
            }

            System.out.print(messages.getString("cipher.output_file") + " ");
            String outputFile = scanner.nextLine();

            System.out.print(messages.getString("cipher.key") + " ");
            String keyInput = scanner.nextLine();
            if (keyInput.isEmpty()) {
                System.out.println(messages.getString("cipher.key_empty"));
                logger.warning("Empty key provided");
                return;
            }
            char key = keyInput.charAt(0);
            logger.fine("Cipher key: " + key);

            switch (choice) {
                case 1:
                    cipherService.encryptFile(inputFile, outputFile, key);
                    System.out.println(messages.getString("cipher.encrypted_success"));
                    logger.info("File encrypted: " + inputFile + " -> " + outputFile);
                    break;
                case 2:
                    cipherService.decryptFile(inputFile, outputFile, key);
                    System.out.println(messages.getString("cipher.decrypted_success"));
                    logger.info("File decrypted: " + inputFile + " -> " + outputFile);
                    break;
                case 3:
                    cipherService.encryptTextFile(inputFile, outputFile, key);
                    System.out.println(messages.getString("cipher.text_encrypted_success"));
                    logger.info("Text file encrypted: " + inputFile + " -> " + outputFile);
                    break;
                case 4:
                    cipherService.decryptTextFile(inputFile, outputFile, key);
                    System.out.println(messages.getString("cipher.text_decrypted_success"));
                    logger.info("Text file decrypted: " + inputFile + " -> " + outputFile);
                    break;
                default:
                    System.out.println(messages.getString("menu.invalid_choice"));
                    logger.warning("Invalid cipher choice: " + choice);
            }
        } catch (NumberFormatException e) {
            System.out.println(messages.getString("error.number_format"));
            logger.warning("Number format error in cipher: " + e.getMessage());
        } catch (IOException e) {
            System.out.println(messages.getString("file.operation_error") + " " + e.getMessage());
            logger.severe("Cipher operation error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(messages.getString("error.general") + " " + e.getMessage());
            logger.severe("Cipher error: " + e.getMessage());
        } finally {
            logger.exiting("Main", "processCipher");
        }
    }

    private static void processTagCounter() {
        logger.entering("Main", "processTagCounter");
        try {
            System.out.println("\n=== " + messages.getString("html.title") + " ===");
            System.out.print(messages.getString("html.enter_url") + " ");
            String url = scanner.nextLine();
            logger.fine("Processing URL: " + url);

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "https://" + url;
                logger.fine("URL normalized to: " + url);
            }

            TagCounter counter = new TagCounter(url);
            System.out.println(messages.getString("html.analyzing"));
            counter.analyzeHTML();
            logger.info("HTML analysis completed for URL: " + url);

            counter.printByTagName();
            counter.printByFrequency();

            System.out.print("\n" + messages.getString("html.save_results") + " ");
            String saveChoice = scanner.nextLine();
            if (saveChoice.equalsIgnoreCase("y")) {
                System.out.print(messages.getString("html.results_saved") + " ");
                String saveFile = scanner.nextLine();
                counter.saveResultsToFile(saveFile);
                System.out.println(messages.getString("html.results_saved") + " " + saveFile);
                logger.info("HTML analysis results saved to: " + saveFile);
            }
        } catch (IOException e) {
            System.out.println(messages.getString("html.url_error") + " " + e.getMessage());
            logger.severe("URL access error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(messages.getString("error.general") + " " + e.getMessage());
            logger.severe("HTML analysis error: " + e.getMessage());
        } finally {
            logger.exiting("Main", "processTagCounter");
        }
    }
}