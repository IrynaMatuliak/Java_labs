package com.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TagCounterTest {
    private static final String TEST_HTML_FILE = "test_page.html";
    private static final String RESULTS_FILE = "test_tag_results.txt";

    @BeforeEach
    void setUp() throws IOException {
        String testHtml = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Test Page</title>
                <style>
                    body { font-family: Arial; }
                </style>
            </head>
            <body>
                <h1>Main Header</h1>
                <div class="content">
                    <p>First paragraph</p>
                    <p>Second paragraph with <strong>bold</strong> text</p>
                    <img src="test.jpg" alt="Test image">
                    <br>
                    <a href="https://example.com">Link</a>
                </div>
                <ul>
                    <li>First item</li>
                    <li>Second item</li>
                    <li>Third item</li>
                </ul>
                <script>
                    console.log("Test script");
                </script>
            </body>
            </html>
            """;

        try (PrintWriter writer = new PrintWriter(new FileWriter(TEST_HTML_FILE))) {
            writer.println(testHtml);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_HTML_FILE));
        Files.deleteIfExists(Paths.get(RESULTS_FILE));
    }

    @Test
    void testTagCounterCreation() {
        System.out.println("=== TagCounter Creation Test ===");

        TagCounter counter = new TagCounter("https://example.com");
        assertNotNull(counter);

        Map<String, Integer> frequency = counter.getTagFrequency();
        assertNotNull(frequency);
        assertTrue(frequency.isEmpty());

        System.out.println("TagCounter created correctly");
        System.out.println("Creation test passed\n");
    }

    @Test
    void testAnalyzeHTMLFromFile() throws IOException {
        System.out.println("=== HTML Analysis from File Test ===");

        String fileUrl = "file:///" + new File(TEST_HTML_FILE).getAbsolutePath().replace("\\", "/");
        TagCounter counter = new TagCounter(fileUrl);
        counter.analyzeHTML();

        Map<String, Integer> frequency = counter.getTagFrequency();
        assertNotNull(frequency);
        assertFalse(frequency.isEmpty());

        assertTrue(frequency.containsKey("html"));
        assertTrue(frequency.containsKey("head"));
        assertTrue(frequency.containsKey("body"));
        assertTrue(frequency.containsKey("p"));
        assertTrue(frequency.containsKey("li"));

        System.out.println("Tags found: " + frequency.size());
        frequency.forEach((tag, count) ->
                System.out.println("  <" + tag + ">: " + count + " times")
        );

        System.out.println("HTML analysis test passed\n");
    }

    @Test
    void testPrintByTagName() throws IOException {
        System.out.println("=== Lexicographical Order Output Test ===");

        String fileUrl = "file:///" + new File(TEST_HTML_FILE).getAbsolutePath().replace("\\", "/");
        TagCounter counter = new TagCounter(fileUrl);
        counter.analyzeHTML();

        assertDoesNotThrow(() -> counter.printByTagName());

        System.out.println("Lexicographical order output works correctly");
        System.out.println("Tag output test passed\n");
    }

    @Test
    void testPrintByFrequency() throws IOException {
        System.out.println("=== Frequency Output Test ===");

        String fileUrl = "file:///" + new File(TEST_HTML_FILE).getAbsolutePath().replace("\\", "/");
        TagCounter counter = new TagCounter(fileUrl);
        counter.analyzeHTML();

        assertDoesNotThrow(() -> counter.printByFrequency());

        System.out.println("Frequency output works correctly");
        System.out.println("Frequency output test passed\n");
    }

    @Test
    void testSaveResultsToFile() throws IOException {
        System.out.println("=== Save Results to File Test ===");

        String fileUrl = "file:///" + new File(TEST_HTML_FILE).getAbsolutePath().replace("\\", "/");
        TagCounter counter = new TagCounter(fileUrl);
        counter.analyzeHTML();

        counter.saveResultsToFile(RESULTS_FILE);

        File resultsFile = new File(RESULTS_FILE);
        assertTrue(resultsFile.exists());
        assertTrue(resultsFile.length() > 0);

        String content = new String(Files.readAllBytes(Paths.get(RESULTS_FILE)));
        assertTrue(content.contains("HTML Tag Analysis Results"));
        assertTrue(content.contains("Tags in Lexicographical Order"));
        assertTrue(content.contains("Tags by Frequency"));

        System.out.println("Results saved to file: " + resultsFile.length() + " bytes");
        System.out.println("Save results test passed\n");
    }

    @Test
    void testTagFrequencyCounting() throws IOException {
        System.out.println("=== Tag Frequency Counting Test ===");

        String fileUrl = "file:///" + new File(TEST_HTML_FILE).getAbsolutePath().replace("\\", "/");
        TagCounter counter = new TagCounter(fileUrl);
        counter.analyzeHTML();

        Map<String, Integer> frequency = counter.getTagFrequency();

        assertTrue(frequency.get("li") >= 3);
        assertTrue(frequency.get("p") >= 2);

        System.out.println("Tag frequency counted correctly:");
        System.out.println("  <li>: " + frequency.get("li") + " times");
        System.out.println("  <p>: " + frequency.get("p") + " times");
        System.out.println("  <div>: " + frequency.get("div") + " times");

        System.out.println("Tag frequency counting test passed\n");
    }

    @Test
    void testEmptyHTML() throws IOException {
        System.out.println("=== Empty HTML Test ===");

        String emptyHtmlFile = "empty_test.html";
        try (PrintWriter writer = new PrintWriter(new FileWriter(emptyHtmlFile))) {
            writer.println("<!DOCTYPE html>");
            writer.println("<html>");
            writer.println("</html>");
        }

        String fileUrl = "file:///" + new File(emptyHtmlFile).getAbsolutePath().replace("\\", "/");
        TagCounter counter = new TagCounter(fileUrl);
        counter.analyzeHTML();

        Map<String, Integer> frequency = counter.getTagFrequency();
        assertTrue(frequency.containsKey("html"));
        assertEquals(1, frequency.get("html"));

        System.out.println("Empty HTML processed correctly");
        System.out.println("Tags found: " + frequency.size());

        Files.deleteIfExists(Paths.get(emptyHtmlFile));
        System.out.println("Empty HTML test passed\n");
    }

    @Test
    void testInvalidURL() {
        System.out.println("=== Invalid URL Test ===");

        TagCounter counter = new TagCounter("invalid_url_12345");

        assertThrows(IOException.class, () -> {
            counter.analyzeHTML();
        });
        System.out.println("Exception for invalid URL handled correctly");

        System.out.println("Invalid URL test passed\n");
    }

    @Test
    void testCaseSensitivity() throws IOException {
        System.out.println("=== Tag Case Insensitivity Test ===");

        String mixedCaseHtml = """
            <HTML>
            <Head>
                <TITLE>Test</TITLE>
            </Head>
            <Body>
                <P>Text</P>
                <p>More text</p>
            </Body>
            </HTML>
            """;

        String mixedCaseFile = "mixed_case_test.html";
        try (PrintWriter writer = new PrintWriter(new FileWriter(mixedCaseFile))) {
            writer.println(mixedCaseHtml);
        }

        String fileUrl = "file:///" + new File(mixedCaseFile).getAbsolutePath().replace("\\", "/");
        TagCounter counter = new TagCounter(fileUrl);
        counter.analyzeHTML();

        Map<String, Integer> frequency = counter.getTagFrequency();

        assertTrue(frequency.containsKey("html"));
        assertTrue(frequency.containsKey("head"));
        assertTrue(frequency.containsKey("body"));
        assertTrue(frequency.containsKey("p"));

        assertEquals(2, frequency.get("p"));

        System.out.println("Tag case handled correctly");
        frequency.forEach((tag, count) ->
                System.out.println("  " + tag + ": " + count + " times")
        );
        Files.deleteIfExists(Paths.get(mixedCaseFile));
        System.out.println("Case insensitivity test passed\n");
    }
}