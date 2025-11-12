package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringLengthAnalyzerTest {
    // Test average length calculation for a normal case with multiple strings
    @Test
    void testAverageLengthNormalCase() {
        String[] strings = {"apple", "banana", "pear"};
        double result = StringLengthAnalyzer.calculateAverageLength(strings);
        printAverageResult("Normal case", 5.0, result);
        assertEquals(5.0, result);
    }

    // Test average length calculation for a single-element array
    @Test
    void testAverageLengthSingleElement() {
        String[] strings = {"hello"};
        double result = StringLengthAnalyzer.calculateAverageLength(strings);
        printAverageResult("Single element", 5.0, result);
        assertEquals(5.0, result);
    }

    // Test average length calculation with strings of different lengths
    @Test
    void testAverageLengthDifferentLengths() {
        String[] strings = {"a", "ab", "abc", "abcd"};
        double result = StringLengthAnalyzer.calculateAverageLength(strings);
        printAverageResult("Different lengths", 2.5, result);
        assertEquals(2.5, result);
    }

    // Test average length calculation when all strings are empty
    @Test
    void testAverageLengthEmptyStrings() {
        String[] strings = {"", "", ""};
        double result = StringLengthAnalyzer.calculateAverageLength(strings);
        printAverageResult("Empty strings", 0.0, result);
        assertEquals(0.0, result);
    }

    // Test finding strings shorter than the average length
    @Test
    void testFindStringsLessThanAverage() {
        System.out.println("--- testFindStringsLessThanAverage ---");

        String[] input1 = {"abc", "abcd", "abcde", "ab"};
        String[] expected1 = {"abc", "ab"};
        String[] actual1 = StringLengthAnalyzer.findStringsLessThanAverage(input1);
        printTestResult("Test 1", expected1, actual1);
        assertArrayEquals(expected1, actual1);

        String[] input2 = {"hello", "world", "test"};
        String[] expected2 = {"test"};
        String[] actual2 = StringLengthAnalyzer.findStringsLessThanAverage(input2);
        printTestResult("Test 2", expected2, actual2);
        assertArrayEquals(expected2, actual2);

        String[] input3 = {"a", "bb", "ccc"};
        String[] expected3 = {"a"};
        String[] actual3 = StringLengthAnalyzer.findStringsLessThanAverage(input3);
        printTestResult("Test 3", expected3, actual3);
        assertArrayEquals(expected3, actual3);

        System.out.println();
    }

    // Test finding strings longer than the average length
    @Test
    void testFindStringsGreaterThanAverage() {
        System.out.println("--- testFindStringsGreaterThanAverage ---");

        String[] input1 = {"abc", "abcd", "abcde", "ab"};
        String[] expected1 = {"abcd", "abcde"};
        String[] actual1 = StringLengthAnalyzer.findStringsGreaterThanAverage(input1);
        printTestResult("Test 1", expected1, actual1);
        assertArrayEquals(expected1, actual1);

        String[] input2 = {"hello", "world", "test"};
        String[] expected2 = {"hello", "world"};
        String[] actual2 = StringLengthAnalyzer.findStringsGreaterThanAverage(input2);
        printTestResult("Test 2", expected2, actual2);
        assertArrayEquals(expected2, actual2);

        String[] input3 = {"a", "bb", "ccc"};
        String[] expected3 = {"ccc"};
        String[] actual3 = StringLengthAnalyzer.findStringsGreaterThanAverage(input3);
        printTestResult("Test 3", expected3, actual3);
        assertArrayEquals(expected3, actual3);

        System.out.println();
    }

    // Test both less-than and greater-than average string methods together
    @Test
    void testBothOptionsCombined() {
        System.out.println("--- testBothOptionsCombined ---");

        String[] input1 = {"abc", "abcd", "abcde", "ab"};
        String[] lessExpected1 = {"abc", "ab"};
        String[] greaterExpected1 = {"abcd", "abcde"};

        String[] lessActual1 = StringLengthAnalyzer.findStringsLessThanAverage(input1);
        String[] greaterActual1 = StringLengthAnalyzer.findStringsGreaterThanAverage(input1);

        printCombinedResult("Test 1", lessExpected1, lessActual1, greaterExpected1, greaterActual1);
        assertArrayEquals(lessExpected1, lessActual1);
        assertArrayEquals(greaterExpected1, greaterActual1);

        String[] input2 = {"hello", "world", "test"};
        String[] lessExpected2 = {"test"};
        String[] greaterExpected2 = {"hello", "world"};

        String[] lessActual2 = StringLengthAnalyzer.findStringsLessThanAverage(input2);
        String[] greaterActual2 = StringLengthAnalyzer.findStringsGreaterThanAverage(input2);

        printCombinedResult("Test 2", lessExpected2, lessActual2, greaterExpected2, greaterActual2);
        assertArrayEquals(lessExpected2, lessActual2);
        assertArrayEquals(greaterExpected2, greaterActual2);

        System.out.println();
    }

    // Test edge cases: same length strings, single string, empty array, and null input
    @Test
    void testEdgeCases() {
        System.out.println("--- testEdgeCases ---");

        String[] input1 = {"abc", "def", "ghi"};
        String[] expected1 = {};
        String[] actual1 = StringLengthAnalyzer.findStringsLessThanAverage(input1);
        printTestResult("Same length", expected1, actual1);
        assertArrayEquals(expected1, actual1);

        String[] input2 = {"single"};
        String[] expected2 = {};
        String[] actual2 = StringLengthAnalyzer.findStringsLessThanAverage(input2);
        printTestResult("Single string", expected2, actual2);
        assertArrayEquals(expected2, actual2);

        String[] input3 = {};
        String[] expected3 = {};
        String[] actual3 = StringLengthAnalyzer.findStringsLessThanAverage(input3);
        printTestResult("Empty array", expected3, actual3);
        assertArrayEquals(expected3, actual3);

        String[] expected4 = {};
        String[] actual4 = StringLengthAnalyzer.findStringsLessThanAverage(null);
        printTestResult("Null array", expected4, actual4);
        assertArrayEquals(expected4, actual4);

        System.out.println();
    }

    private void printTestResult(String testName, String[] expected, String[] actual) {
        System.out.printf("%s - Expected: %s | Actual: %s%n", testName, java.util.Arrays.toString(expected), java.util.Arrays.toString(actual));
    }

    private void printCombinedResult(String testName, String[] lessExpected, String[] lessActual, String[] greaterExpected, String[] greaterActual) {
        System.out.printf("%s%n", testName);
        System.out.printf("  Less    - Expected: %s | Actual: %s%n", java.util.Arrays.toString(lessExpected), java.util.Arrays.toString(lessActual));
        System.out.printf("  Greater - Expected: %s | Actual: %s%n", java.util.Arrays.toString(greaterExpected), java.util.Arrays.toString(greaterActual));
    }

    private void printAverageResult(String testName, double expected, double actual) {
        System.out.printf("%s - Expected: %.1f | Actual: %.1f%n", testName, expected, actual);
    }
}
