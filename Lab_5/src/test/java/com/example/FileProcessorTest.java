package com.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class FileProcessorTest {

    @TempDir
    Path tempDir;

    private FileProcessor fileProcessor;
    private FileCipherService cipherService;
    private File testFile;
    private File encryptedFile;
    private File serializedFile;

    @BeforeEach
    void setUp() throws IOException {
        testFile = tempDir.resolve("test.txt").toFile();
        encryptedFile = tempDir.resolve("encrypted.dat").toFile();
        serializedFile = tempDir.resolve("serialized.obj").toFile();
        String content = "Hello world\nThis is a test file\nJava programming is fun\nLast line";
        Files.write(testFile.toPath(), content.getBytes());
        fileProcessor = new FileProcessor(testFile.getAbsolutePath());
        cipherService = new FileCipherService();
    }

    @Test
    void testReadFile() throws IOException {
        fileProcessor.readFile();
        List<String> lines = fileProcessor.getLines();
        assertEquals(4, lines.size());
        assertEquals("Hello world", lines.get(0));
        assertEquals("This is a test file", lines.get(1));
        assertEquals("Java programming is fun", lines.get(2));
        assertEquals("Last line", lines.get(3));
        assertNotNull(fileProcessor.getFileContent());
        assertTrue(fileProcessor.getFileContent().contains("Hello world"));
    }

    @Test
    void testReadFileNonExistent() {
        FileProcessor nonExistentProcessor = new FileProcessor("nonexistent.txt");
        assertThrows(FileNotFoundException.class, nonExistentProcessor::readFile);
    }

    @Test
    void testFindLineWithMaxWords() throws IOException {
        fileProcessor.readFile();
        String maxLine = fileProcessor.findLineWithMaxWords();
        assertEquals("This is a test file", maxLine);
    }

    @Test
    void testFindLineWithMaxWordsEmptyFile() throws IOException {
        File emptyFile = tempDir.resolve("empty.txt").toFile();
        Files.write(emptyFile.toPath(), "".getBytes());
        FileProcessor emptyProcessor = new FileProcessor(emptyFile.getAbsolutePath());
        emptyProcessor.readFile();
        assertNull(emptyProcessor.findLineWithMaxWords());
    }

    @Test
    void testCountWordsThroughPublicMethods() throws IOException {
        File testCountFile = tempDir.resolve("wordcount.txt").toFile();
        String content = "one\none two three\nsingle";
        Files.write(testCountFile.toPath(), content.getBytes());
        FileProcessor countProcessor = new FileProcessor(testCountFile.getAbsolutePath());
        countProcessor.readFile();
        String maxLine = countProcessor.findLineWithMaxWords();
        assertEquals("one two three", maxLine);
    }

    @Test
    void testSaveAndLoadObject() throws IOException, ClassNotFoundException {
        fileProcessor.readFile();
        fileProcessor.saveToFile(serializedFile.getAbsolutePath());
        assertTrue(serializedFile.exists());
        assertTrue(serializedFile.length() > 0);
        FileProcessor loadedProcessor = FileProcessor.loadFromFile(serializedFile.getAbsolutePath());
        assertEquals(fileProcessor.getFileName(), loadedProcessor.getFileName());
        assertEquals(fileProcessor.getLines(), loadedProcessor.getLines());
        assertNull(loadedProcessor.getFileContent());
    }

    @Test
    void testGetters() {
        String fileName = "test.txt";
        FileProcessor processor = new FileProcessor(fileName);
        assertEquals(fileName, processor.getFileName());
        assertTrue(processor.getLines().isEmpty());
        assertNull(processor.getFileContent());
    }

    @Test
    void testSerializationWithTransientField() throws IOException, ClassNotFoundException {
        fileProcessor.readFile();
        assertNotNull(fileProcessor.getFileContent());
        fileProcessor.saveToFile(serializedFile.getAbsolutePath());
        FileProcessor loaded = FileProcessor.loadFromFile(serializedFile.getAbsolutePath());
        assertNull(loaded.getFileContent());
        assertEquals(fileProcessor.getFileName(), loaded.getFileName());
        assertEquals(fileProcessor.getLines(), loaded.getLines());
    }

    @Test
    void testEmptyFileOperations() throws IOException {
        File emptyFile = tempDir.resolve("empty.txt").toFile();
        Files.write(emptyFile.toPath(), "".getBytes());
        FileProcessor emptyProcessor = new FileProcessor(emptyFile.getAbsolutePath());
        emptyProcessor.readFile();
        assertTrue(emptyProcessor.getLines().isEmpty());
        assertEquals("", emptyProcessor.getFileContent().trim());
    }

    @Test
    void testFileWithMultipleSpaces() throws IOException {
        File spacesFile = tempDir.resolve("spaces.txt").toFile();
        String content = "  Multiple    spaces   between    words  ";
        Files.write(spacesFile.toPath(), content.getBytes());
        FileProcessor spacesProcessor = new FileProcessor(spacesFile.getAbsolutePath());
        spacesProcessor.readFile();
        String maxLine = spacesProcessor.findLineWithMaxWords();
        assertEquals(4, maxLine.trim().split("\\s+").length);
    }

    @AfterEach
    void tearDown() {
        // clear @TempDir
    }
}