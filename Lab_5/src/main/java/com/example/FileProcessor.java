package com.example;

import java.io.*;
import java.util.*;

public class FileProcessor implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fileName;
    private List<String> lines;
    private transient String fileContent;

    public FileProcessor(String fileName) {
        this.fileName = fileName;
        this.lines = new ArrayList<>();
    }

    public void readFile() throws IOException {
        lines.clear();
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                content.append(line).append("\n");
            }
        }
        fileContent = content.toString();
    }

    public String findLineWithMaxWords() {
        if (lines.isEmpty()) {
            return null;
        }
        String maxLine = lines.get(0);
        int maxWords = countWords(maxLine);
        for (String line : lines) {
            int wordCount = countWords(line);
            if (wordCount > maxWords) {
                maxWords = wordCount;
                maxLine = line;
            }
        }
        return maxLine;
    }

    private int countWords(String line) {
        if (line == null || line.trim().isEmpty()) {
            return 0;
        }
        String[] words = line.trim().split("\\s+");
        return words.length;
    }

    public void saveToFile(String outputFileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFileName))) {
            oos.writeObject(this);
        }
    }

    public static FileProcessor loadFromFile(String inputFileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFileName))) {
            return (FileProcessor) ois.readObject();
        }
    }

    public List<String> getLines() {
        return new ArrayList<>(lines);
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileContent() {
        return fileContent;
    }
}