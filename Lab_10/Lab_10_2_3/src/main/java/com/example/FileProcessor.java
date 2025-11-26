package com.example;

import java.io.*;
import java.util.*;
import java.util.logging.*;

public class FileProcessor implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fileName;
    private List<String> lines;
    private transient String fileContent;
    private static final Logger logger = Logger.getLogger(FileProcessor.class.getName());

    static {
        setupLogger();
    }

    public FileProcessor(String fileName) {
        this.fileName = fileName;
        this.lines = new ArrayList<>();
        logger.fine("FileProcessor created for file: " + fileName);
    }

    public void readFile() throws IOException {
        logger.entering("FileProcessor", "readFile", fileName);
        lines.clear();
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
                content.append(line).append("\n");
                lineCount++;
            }
            logger.info("File read successfully: " + lineCount + " lines read from " + fileName);
        } catch (IOException e) {
            logger.severe("Failed to read file: " + fileName + " - " + e.getMessage());
            throw e;
        } finally {
            logger.exiting("FileProcessor", "readFile");
        }
        fileContent = content.toString();
    }

    public String findLineWithMaxWords() {
        logger.entering("FileProcessor", "findLineWithMaxWords");
        if (lines.isEmpty()) {
            logger.fine("No lines to process - file is empty");
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
        logger.fine("Found line with max words: " + maxWords + " words");
        logger.exiting("FileProcessor", "findLineWithMaxWords", maxLine);
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
        logger.entering("FileProcessor", "saveToFile", outputFileName);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFileName))) {
            oos.writeObject(this);
            logger.info("FileProcessor object saved to: " + outputFileName);
        } catch (IOException e) {
            logger.severe("Failed to save object to: " + outputFileName + " - " + e.getMessage());
            throw e;
        } finally {
            logger.exiting("FileProcessor", "saveToFile");
        }
    }

    public static FileProcessor loadFromFile(String inputFileName) throws IOException, ClassNotFoundException {
        logger.entering("FileProcessor", "loadFromFile", inputFileName);
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(inputFileName))) {
            FileProcessor processor = (FileProcessor) ois.readObject();
            logger.info("FileProcessor object loaded from: " + inputFileName);
            logger.exiting("FileProcessor", "loadFromFile", processor);
            return processor;
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Failed to load object from: " + inputFileName + " - " + e.getMessage());
            throw e;
        }
    }

    private static void setupLogger() {
        try {
            FileHandler fileHandler = new FileHandler("file_processor.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return String.format("[%1$tF %1$tT] [%2$-7s] [FileProcessor] %3$s %n",
                            new Date(record.getMillis()),
                            record.getLevel().getName(),
                            record.getMessage());
                }
            });
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to setup FileProcessor logger: " + e.getMessage());
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