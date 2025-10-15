package com.example;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public class TagCounter {
    private String url;
    private Map<String, Integer> tagFrequency;

    public TagCounter(String url) {
        this.url = url;
        this.tagFrequency = new HashMap<>();
    }

    public void analyzeHTML() throws IOException {
        tagFrequency.clear();

        try {
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String line;
                Pattern tagPattern = Pattern.compile("<\\s*([a-zA-Z][a-zA-Z0-9]*)(\\s+|>|/)");

                while ((line = reader.readLine()) != null) {
                    Matcher matcher = tagPattern.matcher(line);
                    while (matcher.find()) {
                        String tag = matcher.group(1).toLowerCase();
                        tagFrequency.put(tag, tagFrequency.getOrDefault(tag, 0) + 1);
                    }
                }
            }
        } catch (MalformedURLException e) {
            throw new IOException("Invalid URL: " + url, e);
        } catch (IOException e) {
            throw new IOException("URL access error: " + url, e);
        }
    }

    public void printByTagName() {
        System.out.println("\n=== Tags in Lexicographical Order ===");
        if (tagFrequency.isEmpty()) {
            System.out.println("No tags found.");
            return;
        }

        tagFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.printf("Tag <%s>: %d times%n", entry.getKey(), entry.getValue()));
    }

    public void printByFrequency() {
        System.out.println("\n=== Tags by Frequency (Ascending) ===");
        if (tagFrequency.isEmpty()) {
            System.out.println("No tags found.");
            return;
        }

        tagFrequency.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> System.out.printf("Tag <%s>: %d times%n", entry.getKey(), entry.getValue()));
    }

    public void saveResultsToFile(String fileName) throws IOException {
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
        }
    }

    public Map<String, Integer> getTagFrequency() {
        return new HashMap<>(tagFrequency);
    }
}