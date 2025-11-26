package com.example;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;
import java.util.logging.*;

public class TagCounter {
    private String url;
    private Map<String, Integer> tagFrequency;
    private static final Logger logger = Logger.getLogger(TagCounter.class.getName());

    static {
        setupLogger();
    }

    public TagCounter(String url) {
        this.url = url;
        this.tagFrequency = new HashMap<>();
        logger.fine("TagCounter created for URL: " + url);
    }

    public void analyzeHTML() throws IOException {
        logger.entering("TagCounter", "analyzeHTML", url);
        tagFrequency.clear();

        try {
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            logger.fine("Connecting to URL: " + url);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                Pattern tagPattern = Pattern.compile("<\\s*([a-zA-Z][a-zA-Z0-9]*)(\\s+|>|/)");
                int lineCount = 0;
                int tagCount = 0;

                while ((line = reader.readLine()) != null) {
                    lineCount++;
                    Matcher matcher = tagPattern.matcher(line);
                    while (matcher.find()) {
                        String tag = matcher.group(1).toLowerCase();
                        tagFrequency.put(tag, tagFrequency.getOrDefault(tag, 0) + 1);
                        tagCount++;
                    }
                }
                logger.info("HTML analysis completed: " + lineCount + " lines processed, " +
                        tagCount + " tags found, " + tagFrequency.size() + " unique tags");
            }
        } catch (MalformedURLException e) {
            logger.severe("Malformed URL: " + url + " - " + e.getMessage());
            throw new IOException("Invalid URL: " + url, e);
        } catch (IOException e) {
            logger.severe("IO Error accessing URL: " + url + " - " + e.getMessage());
            throw new IOException("URL access error: " + url, e);
        } finally {
            logger.exiting("TagCounter", "analyzeHTML");
        }
    }

    public void printByTagName() {
        logger.entering("TagCounter", "printByTagName");
        System.out.println("\n=== Tags in Lexicographical Order ===");
        if (tagFrequency.isEmpty()) {
            System.out.println("No tags found.");
            logger.fine("No tags to display");
            return;
        }

        tagFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.printf("Tag <%s>: %d times%n", entry.getKey(), entry.getValue()));
        logger.fine("Tags displayed in lexicographical order");
        logger.exiting("TagCounter", "printByTagName");
    }

    public void printByFrequency() {
        logger.entering("TagCounter", "printByFrequency");
        System.out.println("\n=== Tags by Frequency (Ascending) ===");
        if (tagFrequency.isEmpty()) {
            System.out.println("No tags found.");
            logger.fine("No tags to display by frequency");
            return;
        }

        tagFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> System.out.printf("Tag <%s>: %d times%n", entry.getKey(), entry.getValue()));
        logger.fine("Tags displayed by frequency");
        logger.exiting("TagCounter", "printByFrequency");
    }

    public void saveResultsToFile(String fileName) throws IOException {
        logger.entering("TagCounter", "saveResultsToFile", fileName);
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("HTML Tag Analysis Results for URL: " + url);
            writer.println("\nTags in Lexicographical Order:");
            tagFrequency.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> writer.printf("Tag <%s>: %d times%n", entry.getKey(), entry.getValue()));

            writer.println("\nTags by Frequency (Ascending):");
            tagFrequency.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue())
                    .forEach(entry -> writer.printf("Tag <%s>: %d times%n", entry.getKey(), entry.getValue()));

            logger.info("Results saved to file: " + fileName);
        } catch (IOException e) {
            logger.severe("Failed to save results to: " + fileName + " - " + e.getMessage());
            throw e;
        } finally {
            logger.exiting("TagCounter", "saveResultsToFile");
        }
    }

    private static void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("tag_counter.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%1$tF %1$tT] [%2$-7s] [TagCounter] %3$s %n",
                            new Date(record.getMillis()),
                            record.getLevel().getName(),
                            record.getMessage());
                }
            });
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to setup TagCounter logger: " + e.getMessage());
        }
    }

    public Map<String, Integer> getTagFrequency() {
        return new HashMap<>(tagFrequency);
    }
}