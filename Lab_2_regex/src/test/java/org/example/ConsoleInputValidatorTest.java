package org.example;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class ConsoleInputValidatorTest {

    private String captureOutput(Runnable testCode) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try {
            System.setOut(new PrintStream(outputStream));
            testCode.run();
            return outputStream.toString();
        } finally {
            System.setOut(originalOut);
        }
    }

    private ConsoleInputValidator createInputHandlerWithInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        return new ConsoleInputValidator();
    }

    // ===== TESTS FOR VALID INPUTS =====

    @Test
    void testValidCompleteInput() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain Street\n15A\n42\n";
        ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);

        JournalEntry entry = inputHandler.inputJournalEntry();

        assertNotNull(entry, "Journal entry should not be null");
        assertEquals("Smith", entry.getLastName());
        assertEquals("John", entry.getFirstName());
        assertEquals(LocalDate.of(2000, 1, 15), entry.getBirthDate());
        assertEquals("+380991234567", entry.getPhone());
        assertEquals("Main Street", entry.getStreet());
        assertEquals("15A", entry.getHouse());
        assertEquals("42", entry.getApartment());

        inputHandler.close();
    }

    // ===== TESTS FOR LAST NAME VALIDATION =====

    @Test
    void testLastNameEmptyInput() {
        String input = "\nSmith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && output.contains("required"),
                "Should display required field error for empty last name. Output: " + output);
    }

    @Test
    void testLastNameLowerCaseStart() {
        String input = "smith\nSmith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("uppercase") || output.contains("Name must")),
                "Should display uppercase letter error for lowercase start. Output: " + output);
    }

    // ===== TESTS FOR FIRST NAME VALIDATION =====

    @Test
    void testFirstNameEmptyInput() {
        String input = "Smith\n\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && output.contains("required"),
                "Should display required field error for empty first name. Output: " + output);
    }

    // ===== TESTS FOR BIRTH DATE VALIDATION =====

    @Test
    void testBirthDateEmptyInput() {
        String input = "Smith\nJohn\n\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && output.contains("required"),
                "Should display required field error for empty birth date. Output: " + output);
    }

    @Test
    void testBirthDateWrongFormat() {
        String input = "Smith\nJohn\n15-01-2000\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("format") || output.contains("Date must")),
                "Should display date format error. Output: " + output);
    }

    @Test
    void testBirthDateFutureDate() {
        String input = "Smith\nJohn\n15.01.2030\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("future") || output.contains("cannot be")),
                "Should display future date error. Output: " + output);
    }

    @Test
    void testBirthDateTooOld() {
        String input = "Smith\nJohn\n15.01.1890\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("100 years") || output.contains("cannot be")),
                "Should display too old date error. Output: " + output);
    }

    @Test
    void testBirthDateInvalidFebruary29NonLeapYear() {
        String input = "Smith\nJohn\n29.02.2023\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && output.contains("date"),
                "Should display invalid date error for February 29 in non-leap year. Output: " + output);
    }

    @Test
    void testBirthDateInvalidMonth() {
        String input = "Smith\nJohn\n15.13.2000\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        // Accept either format error or invalid date error
        boolean hasError = output.contains("Error:") &&
                (output.contains("format") || output.contains("date") || output.contains("Invalid"));
        assertTrue(hasError, "Should display error for invalid month. Output: " + output);
    }

    @Test
    void testBirthDateInvalidDayInMonth() {
        String input = "Smith\nJohn\n31.04.2000\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && output.contains("date"),
                "Should display invalid date error for day that doesn't exist in month. Output: " + output);
    }

    // ===== TESTS FOR PHONE VALIDATION =====

    @Test
    void testPhoneEmptyInput() {
        String input = "Smith\nJohn\n15.01.2000\n\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && output.contains("required"),
                "Should display required field error for empty phone. Output: " + output);
    }

    @Test
    void testPhoneWrongPrefix() {
        String input = "Smith\nJohn\n15.01.2000\n+390991234567\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("Phone") || output.contains("format")),
                "Should display phone format error. Output: " + output);
    }

    @Test
    void testPhoneWrongLength() {
        String input = "Smith\nJohn\n15.01.2000\n+38099123456\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("Phone") || output.contains("format")),
                "Should display phone format error. Output: " + output);
    }

    // ===== TESTS FOR STREET VALIDATION =====

    @Test
    void testStreetEmptyInput() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\n\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && output.contains("required"),
                "Should display required field error for empty street. Output: " + output);
    }

    @Test
    void testStreetLowerCaseStart() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nmain street\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("uppercase") || output.contains("Street must")),
                "Should display uppercase letter error for lowercase start. Output: " + output);
    }

    // ===== TESTS FOR HOUSE NUMBER VALIDATION =====

    @Test
    void testHouseNumberEmptyInput() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && output.contains("required"),
                "Should display required field error for empty house number. Output: " + output);
    }

    @Test
    void testHouseNumberStartsWithFraction() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n/15\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("House number") || output.contains("format")),
                "Should display error for house number starting with fraction. Output: " + output);
    }

    @Test
    void testHouseNumberEndsWithFraction() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15/\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("House number") || output.contains("format")),
                "Should display error for house number ending with fraction. Output: " + output);
    }

    @Test
    void testHouseNumberInvalidCharacters() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15@A\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("House number") || output.contains("format")),
                "Should display error for house number with invalid characters. Output: " + output);
    }

    @Test
    void testHouseNumberOutOfRange() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n0\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("House number") || output.contains("between") || output.contains("format")),
                "Should display error for out of range house number. Output: " + output);
    }

    @Test
    void testHouseNumberFractionOutOfRange() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n123/0\n123/45\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("House number") || output.contains("between") || output.contains("format")),
                "Should display error for out of range fraction. Output: " + output);
    }

    // ===== TESTS FOR APARTMENT NUMBER VALIDATION =====

    @Test
    void testApartmentNumberEmptyInput() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && output.contains("required"),
                "Should display required field error for empty apartment number. Output: " + output);
    }

    @Test
    void testApartmentNumberNonDigits() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n42A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("Apartment number") || output.contains("number")),
                "Should display error for non-digit apartment number. Output: " + output);
    }

    @Test
    void testApartmentNumberOutOfRange() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n0\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("Apartment number") || output.contains("between")),
                "Should display error for out of range apartment number. Output: " + output);
    }

    @Test
    void testApartmentNumberTooLarge() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n10000\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("Apartment number") || output.contains("between")),
                "Should display error for too large apartment number. Output: " + output);
    }

    @Test
    void testApartmentNumberWithLeadingZero() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n042\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        assertTrue(output.contains("Error:") && (output.contains("Apartment number") || output.contains("number")),
                "Should display error for apartment number with leading zero. Output: " + output);
    }

    // ===== COMPREHENSIVE ERROR MESSAGE TEST =====

    @Test
    void testMultipleValidationErrors() {
        String input = "\n\ninvalid-date\nwrong-phone\nlowercase street\ninvalid-house\nnot-number\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            try {
                JournalEntry entry = inputHandler.inputJournalEntry();
            } catch (Exception e) {
                // Expected due to insufficient valid inputs
            }
            inputHandler.close();
        });

        // Check that multiple error messages are displayed
        int errorCount = countOccurrences(output, "Error:");
        assertTrue(errorCount >= 3, "Should show multiple error messages. Output: " + output);
    }

    private int countOccurrences(String text, String substring) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(substring, index)) != -1) {
            count++;
            index += substring.length();
        }
        return count;
    }

    // ===== ADDITIONAL POSITIVE TESTS =====

    @Test
    void testValidNameWithApostrophe() {
        String input = "O'Brian\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);

        JournalEntry entry = inputHandler.inputJournalEntry();

        assertEquals("O'Brian", entry.getLastName(), "Name with apostrophe should be accepted");
        inputHandler.close();
    }

    @Test
    void testValidLeapYearDate() {
        String input = "Smith\nJohn\n29.02.2000\n+380991234567\nMain\n15A\n42\n";
        ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);

        JournalEntry entry = inputHandler.inputJournalEntry();

        assertEquals(LocalDate.of(2000, 2, 29), entry.getBirthDate(), "February 29 in leap year should be accepted");
        inputHandler.close();
    }

    @Test
    void testValidHouseNumberWithFraction() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n123/45B\n42\n";
        ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);

        JournalEntry entry = inputHandler.inputJournalEntry();

        assertEquals("123/45B", entry.getHouse(), "House number with fraction should be accepted");
        inputHandler.close();
    }
}