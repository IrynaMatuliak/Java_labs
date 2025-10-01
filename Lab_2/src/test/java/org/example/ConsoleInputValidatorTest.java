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

    // ===== TESTS FOR VALID INPUT =====

    @Test
    void testValidCompleteInput() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain Street\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();

            System.out.println("=== Test Valid Complete Input ===");
            System.out.println("Expected: All fields filled correctly");
            System.out.println("Actual entry: " + entry);

            assertEquals("Smith", entry.getLastName());
            assertEquals("John", entry.getFirstName());
            assertEquals(LocalDate.of(2000, 1, 15), entry.getBirthDate());
            assertEquals("+380991234567", entry.getPhone());
            assertEquals("Main Street", entry.getStreet());
            assertEquals("15A", entry.getHouse());
            assertEquals("42", entry.getApartment());

            System.out.println("All fields correctly populated");
        });

        assertTrue(output.contains("=== Add New Journal Entry ==="));
        System.out.println("Valid complete input test passed\n");
    }

    @Test
    void testValidInputWithSpecialCharacters() {
        String input = "O'Brian\nMary-Ann\n29.02.2000\n+380991234567\nOak-Street\n123/45B\n100\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();

            System.out.println("=== Test Valid Input with Special Characters ===");
            System.out.println("Actual entry: " + entry);

            assertEquals("O'Brian", entry.getLastName());
            assertEquals("Mary-Ann", entry.getFirstName());
            assertEquals(LocalDate.of(2000, 2, 29), entry.getBirthDate());
            assertEquals("123/45B", entry.getHouse());

            System.out.println("Special characters handled correctly");
        });

        System.out.println("Valid input with special characters test passed\n");
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

        System.out.println("=== Test Last Name - Empty Input ===");
        System.out.println("Expected error: 'Error: This field is required. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: This field is required. Please try again."));

        assertTrue(output.contains("Error: This field is required. Please try again."));
        System.out.println("Empty last name error message correct\n");
    }

    @Test
    void testLastNameLowerCaseStart() {
        String input = "smith\nSmith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Last Name - Lowercase Start ===");
        System.out.println("Expected error: 'Error: Name must start with uppercase letter. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Name must start with uppercase letter. Please try again."));

        assertTrue(output.contains("Error: Name must start with uppercase letter. Please try again."));
        System.out.println("Lowercase start error message correct\n");
    }

    @Test
    void testLastNameInvalidCharacters() {
        String input = "Smith123\nSmith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Last Name - Invalid Characters ===");
        System.out.println("Expected error: 'Error: Name can only contain letters, apostrophes and hyphens. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Name can only contain letters, apostrophes and hyphens. Please try again."));

        assertTrue(output.contains("Error: Name can only contain letters, apostrophes and hyphens. Please try again."));
        System.out.println("Invalid characters error message correct\n");
    }

    @Test
    void testLastNameTooLong() {
        String input = "VeryLongLastNameThatExceedsLimit\nSmith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Last Name - Too Long ===");
        System.out.println("Expected error: 'Error: Name cannot exceed 20 characters. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Name cannot exceed 20 characters. Please try again."));

        assertTrue(output.contains("Error: Name cannot exceed 20 characters. Please try again."));
        System.out.println("Too long name error message correct\n");
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

        System.out.println("=== Test First Name - Empty Input ===");
        System.out.println("Expected error: 'Error: This field is required. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: This field is required. Please try again."));

        assertTrue(output.contains("Error: This field is required. Please try again."));
        System.out.println("Empty first name error message correct\n");
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

        System.out.println("=== Test Birth Date - Empty Input ===");
        System.out.println("Expected error: 'Error: This field is required. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: This field is required. Please try again."));

        assertTrue(output.contains("Error: This field is required. Please try again."));
        System.out.println("Empty birth date error message correct\n");
    }

    @Test
    void testBirthDateWrongFormat() {
        String input = "Smith\nJohn\n15-01-2000\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Birth Date - Wrong Format ===");
        System.out.println("Expected error: 'Error: Date must be in format dd.MM.yyyy. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Date must be in format dd.MM.yyyy. Please try again."));

        assertTrue(output.contains("Error: Date must be in format dd.MM.yyyy. Please try again."));
        System.out.println("Wrong format error message correct\n");
    }

    @Test
    void testBirthDateFutureDate() {
        LocalDate futureDate = LocalDate.now().plusMonths(1); // Use months instead of years
        String futureDateStr = futureDate.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String input = "Smith\nJohn\n" + futureDateStr + "\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Birth Date - Future Date ===");
        System.out.println("Testing with date: " + futureDateStr);
        System.out.println("Expected error: 'Error: Birth date cannot be in the future. Please try again.'");
        System.out.println("Actual output: " + output);

        assertTrue(output.contains("Error: Birth date cannot be in the future. Please try again."),
                "Should display future date error");
        System.out.println("Future date error message correct\n");
    }

    @Test
    void testBirthDateTooOld() {
        String input = "Smith\nJohn\n15.01.1890\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Birth Date - Too Old ===");
        System.out.println("Expected error: 'Error: Year must be between 1900 and " + LocalDate.now().getYear() + ". Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Year must be between 1900 and"));

        assertTrue(output.contains("Error: Year must be between 1900 and"));
        System.out.println("Too old date error message correct\n");
    }

    @Test
    void testBirthDateInvalidFebruary29NonLeapYear() {
        String input = "Smith\nJohn\n29.02.2023\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Birth Date - Invalid February 29 (Non-Leap Year) ===");
        System.out.println("Expected error: 'has only 28 days'");
        System.out.println("Actual output contains error: " + output.contains("has only 28 days"));

        assertTrue(output.contains("has only 28 days"));
        System.out.println("February 29 non-leap year error message correct\n");
    }

    @Test
    void testBirthDateInvalidDayForMonth() {
        String input = "Smith\nJohn\n31.04.2000\n15.01.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Birth Date - Invalid Day for Month ===");
        System.out.println("Expected error: 'has only 30 days'");
        System.out.println("Actual output contains error: " + output.contains("has only 30 days"));

        assertTrue(output.contains("has only 30 days"));
        System.out.println("Invalid day for month error message correct\n");
    }

    @Test
    void testBirthDateValidLeapYear() {
        String input = "Smith\nJohn\n29.02.2000\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();

            assertEquals(LocalDate.of(2000, 2, 29), entry.getBirthDate());
        });

        System.out.println("=== Test Birth Date - Valid Leap Year ===");
        System.out.println("Valid leap year date accepted correctly\n");
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

        System.out.println("=== Test Phone - Empty Input ===");
        System.out.println("Expected error: 'Error: This field is required. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: This field is required. Please try again."));

        assertTrue(output.contains("Error: This field is required. Please try again."));
        System.out.println("Empty phone error message correct\n");
    }

    @Test
    void testPhoneWrongPrefix() {
        String input = "Smith\nJohn\n15.01.2000\n+390991234567\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Phone - Wrong Prefix ===");
        System.out.println("Expected error: 'Error: Phone must start with +380. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Phone must start with +380. Please try again."));

        assertTrue(output.contains("Error: Phone must start with +380. Please try again."));
        System.out.println("Wrong prefix error message correct\n");
    }

    @Test
    void testPhoneWrongLength() {
        String input = "Smith\nJohn\n15.01.2000\n+38099123456\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Phone - Wrong Length ===");
        System.out.println("Expected error: 'Error: Phone must be exactly 13 characters long. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Phone must be exactly 13 characters long. Please try again."));

        assertTrue(output.contains("Error: Phone must be exactly 13 characters long. Please try again."));
        System.out.println("Wrong length error message correct\n");
    }

    @Test
    void testPhoneNonDigits() {
        String input = "Smith\nJohn\n15.01.2000\n+3809912345ab\n+380991234567\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Phone - Non-Digits ===");
        System.out.println("Expected error: 'Error: Phone can only contain digits after +380. Please try again.'");
        System.out.println("Actual output: " + output);

        assertTrue(output.contains("Error: Phone can only contain digits after +380. Please try again."),
                "Should display non-digits error");
        System.out.println("Non-digits error message correct\n");
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

        System.out.println("=== Test Street - Empty Input ===");
        System.out.println("Expected error: 'Error: This field is required. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: This field is required. Please try again."));

        assertTrue(output.contains("Error: This field is required. Please try again."));
        System.out.println("Empty street error message correct\n");
    }

    @Test
    void testStreetLowerCaseStart() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nmain\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Street - Lowercase Start ===");
        System.out.println("Expected error: 'Error: Street name must start with uppercase letter. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Street name must start with uppercase letter. Please try again."));

        assertTrue(output.contains("Error: Street name must start with uppercase letter. Please try again."));
        System.out.println("Lowercase start error message correct\n");
    }

    @Test
    void testStreetTooLong() {
        String longStreet = "A".repeat(31);
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\n" + longStreet + "\nMain\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Street - Too Long ===");
        System.out.println("Expected error: 'Error: Street name cannot exceed 30 characters. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Street name cannot exceed 30 characters. Please try again."));

        assertTrue(output.contains("Error: Street name cannot exceed 30 characters. Please try again."));
        System.out.println("Too long street error message correct\n");
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

        System.out.println("=== Test House Number - Empty Input ===");
        System.out.println("Expected error: 'Error: This field is required. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: This field is required. Please try again."));

        assertTrue(output.contains("Error: This field is required. Please try again."));
        System.out.println("Empty house number error message correct\n");
    }

    @Test
    void testHouseNumberStartsWithFraction() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n/15\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test House Number - Starts with Fraction ===");
        System.out.println("Expected error: 'Error: House number must start with a digit. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: House number must start with a digit. Please try again."));

        assertTrue(output.contains("Error: House number must start with a digit. Please try again."));
        System.out.println("Starts with fraction error message correct\n");
    }

    @Test
    void testHouseNumberEndsWithFraction() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15/\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test House Number - Ends with Fraction ===");
        System.out.println("Expected error: 'Error: House number cannot end with a fraction. Please try again.'");
        System.out.println("Actual output: " + output);

        // Check for either specific error message
        boolean hasError = output.contains("Error: House number cannot end with a fraction. Please try again.") ||
                output.contains("Error: Invalid house number format. Please try again.");

        assertTrue(hasError, "Should display fraction error");
        System.out.println("Ends with fraction error message correct\n");
    }

    @Test
    void testHouseNumberInvalidFormat() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15@A\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test House Number - Invalid Format ===");
        System.out.println("Expected error: 'Error: Invalid house number format. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Invalid house number format. Please try again."));

        assertTrue(output.contains("Error: Invalid house number format. Please try again."));
        System.out.println("Invalid format error message correct\n");
    }

    @Test
    void testHouseNumberOutOfRange() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n0\n15A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test House Number - Out of Range ===");
        System.out.println("Expected error: 'Error: House number must be between 1 and 999. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: House number must be between 1 and 999. Please try again."));

        assertTrue(output.contains("Error: House number must be between 1 and 999. Please try again."));
        System.out.println("Out of range error message correct\n");
    }

    @Test
    void testHouseNumberValidWithFraction() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n123/45B\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();

            assertEquals("123/45B", entry.getHouse());
        });

        System.out.println("=== Test House Number - Valid with Fraction ===");
        System.out.println("House number with fraction accepted correctly\n");
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

        System.out.println("=== Test Apartment Number - Empty Input ===");
        System.out.println("Expected error: 'Error: This field is required. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: This field is required. Please try again."));

        assertTrue(output.contains("Error: This field is required. Please try again."));
        System.out.println("Empty apartment number error message correct\n");
    }

    @Test
    void testApartmentNumberNonDigits() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n42A\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Apartment Number - Non-Digits ===");
        System.out.println("Expected error: 'Error: Apartment number can only contain digits. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Apartment number can only contain digits. Please try again."));

        assertTrue(output.contains("Error: Apartment number can only contain digits. Please try again."));
        System.out.println("Non-digits error message correct\n");
    }

    @Test
    void testApartmentNumberOutOfRange() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n0\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Apartment Number - Out of Range ===");
        System.out.println("Expected error: 'Error: Apartment number must be between 1 and 9999. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Apartment number must be between 1 and 9999. Please try again."));

        assertTrue(output.contains("Error: Apartment number must be between 1 and 9999. Please try again."));
        System.out.println("Out of range error message correct\n");
    }

    @Test
    void testApartmentNumberTooLarge() {
        String input = "Smith\nJohn\n15.01.2000\n+380991234567\nMain\n15A\n10000\n42\n";
        String output = captureOutput(() -> {
            ConsoleInputValidator inputHandler = createInputHandlerWithInput(input);
            JournalEntry entry = inputHandler.inputJournalEntry();
            inputHandler.close();
        });

        System.out.println("=== Test Apartment Number - Too Large ===");
        System.out.println("Expected error: 'Error: Apartment number must be between 1 and 9999. Please try again.'");
        System.out.println("Actual output contains error: " + output.contains("Error: Apartment number must be between 1 and 9999. Please try again."));

        assertTrue(output.contains("Error: Apartment number must be between 1 and 9999. Please try again."));
        System.out.println("Too large error message correct\n");
    }
}
