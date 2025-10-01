package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.time.format.ResolverStyle;

public class ConsoleInputValidator {
    private final Scanner scanner;

    // Global constants for validation rules
    private static final int MAX_NAME_LENGTH = 20;
    private static final int MAX_STREET_LENGTH = 30;
    private static final int MIN_HOUSE_NUMBER = 1;
    private static final int MAX_HOUSE_NUMBER = 999;
    private static final int MIN_APARTMENT_NUMBER = 1;
    private static final int MAX_APARTMENT_NUMBER = 9999;
    private static final int MIN_BIRTH_YEAR = 1900;
    private static final int MAX_AGE_YEARS = 100;
    private static final String PHONE_PREFIX = "+380";

    // Comprehensive regular expressions for validation
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[A-Z][a-zA-Z'\\-]{0," + (MAX_NAME_LENGTH - 1) + "}$"
    );

    private static final Pattern STREET_PATTERN = Pattern.compile(
            "^[A-Z][a-zA-Z\\s'\\-]{0," + (MAX_STREET_LENGTH - 1) + "}$"
    );

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\" + PHONE_PREFIX + "\\d{9}$"
    );

    private static final Pattern HOUSE_NUMBER_PATTERN = Pattern.compile(
            "^(?:(?:[1-9]\\d{0,2})(?:/[1-9]\\d{0,2})?)(?:[A-Za-z])?$"
    );

    private static final Pattern APARTMENT_PATTERN = Pattern.compile(
            "^(?:[1-9]\\d{0,3})$"
    );

    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^(0[1-9]|[12][0-9]|3[01])\\.(0[1-9]|1[0-2])\\.(19[0-9]{2}|20[0-9]{2})$"
    );

    private static final Pattern REQUIRED_FIELD_PATTERN = Pattern.compile(
            "^.*\\S.*$"  // At least one non-whitespace character
    );

    public ConsoleInputValidator() {
        this.scanner = new Scanner(System.in);
    }

    public JournalEntry inputJournalEntry() {
        System.out.println("\n=== Add New Journal Entry ===");

        String lastName = inputName("Last name");
        String firstName = inputName("First name");
        LocalDate birthDate = inputBirthDate();
        String phone = inputPhone();
        String street = inputStreet();
        String house = inputHouseNumber();
        String apartment = inputApartmentNumber();

        return new JournalEntry(lastName, firstName, birthDate, phone, street, house, apartment);
    }

    private String inputName(String fieldName) {
        return inputWithValidation(
                fieldName,
                NAME_PATTERN,
                "Name must start with uppercase letter and contain only letters, apostrophes and hyphens (max " + MAX_NAME_LENGTH + " characters)",
                "This field is required"
        );
    }

    private LocalDate inputBirthDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu").withResolverStyle(ResolverStyle.STRICT);
        LocalDate now = LocalDate.now();
        LocalDate minDate = now.minusYears(MAX_AGE_YEARS);

        while (true) {
            System.out.print("Birth date (dd.MM.yyyy): ");
            String input = scanner.nextLine().trim();

            // Check required field
            if (!REQUIRED_FIELD_PATTERN.matcher(input).matches()) {
                System.out.println("Error: This field is required. Please try again.");
                continue;
            }

            // Basic format validation with simpler regex
            if (!input.matches("^\\d{2}\\.\\d{2}\\.\\d{4}$")) {
                System.out.println("Error: Date must be in format dd.MM.yyyy (e.g., 15.01.2000).");
                continue;
            }

            try {
                // Parse with strict resolution - this will handle all date validation including leap years
                LocalDate date = LocalDate.parse(input, formatter);

                // Additional validation for logical date constraints
                if (date.isAfter(now)) {
                    System.out.println("Error: Birth date cannot be in the future. Please try again.");
                    continue;
                }

                if (date.isBefore(minDate)) {
                    System.out.println("Error: Birth date cannot be more than " + MAX_AGE_YEARS + " years ago. Please try again.");
                    continue;
                }

                return date;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date '" + input + "'. Please check if this date exists (consider leap years and month day limits).");
            }
        }
    }

    private String inputPhone() {
        return inputWithValidation(
                "Phone (" + PHONE_PREFIX + "XXXXXXXXX)",
                PHONE_PATTERN,
                "Phone must be exactly 13 characters in format " + PHONE_PREFIX + " followed by 9 digits",
                "This field is required"
        );
    }

    private String inputStreet() {
        return inputWithValidation(
                "Street",
                STREET_PATTERN,
                "Street must start with uppercase letter and contain only letters, spaces, apostrophes and hyphens (max " + MAX_STREET_LENGTH + " characters)",
                "This field is required"
        );
    }

    private String inputHouseNumber() {
        while (true) {
            System.out.print("House number: ");
            String input = scanner.nextLine().trim();

            // Check required field
            if (!REQUIRED_FIELD_PATTERN.matcher(input).matches()) {
                System.out.println("Error: This field is required. Please try again.");
                continue;
            }

            // Validate basic format with regex
            if (!HOUSE_NUMBER_PATTERN.matcher(input).matches()) {
                System.out.println("Error: Invalid house number format. Valid formats: 123, 123A, 123/45, 123/45A (numbers 1-999, optional letter at end)");
                continue;
            }

            // Extract and validate numeric parts
            if (!validateHouseNumberParts(input)) {
                continue;
            }

            return input;
        }
    }

    private boolean validateHouseNumberParts(String houseNumber) {
        try {
            // Remove any trailing letters
            String numericPart = houseNumber.replaceAll("[A-Za-z]+$", "");

            if (numericPart.contains("/")) {
                // Handle fraction format (e.g., 123/45)
                String[] parts = numericPart.split("/");
                int mainNumber = Integer.parseInt(parts[0]);
                int fractionNumber = Integer.parseInt(parts[1]);

                if (mainNumber < MIN_HOUSE_NUMBER || mainNumber > MAX_HOUSE_NUMBER) {
                    System.out.println("Error: Main house number must be between " + MIN_HOUSE_NUMBER + " and " + MAX_HOUSE_NUMBER + ".");
                    return false;
                }

                if (fractionNumber < MIN_HOUSE_NUMBER || fractionNumber > MAX_HOUSE_NUMBER) {
                    System.out.println("Error: Fraction part must be between " + MIN_HOUSE_NUMBER + " and " + MAX_HOUSE_NUMBER + ".");
                    return false;
                }
            } else {
                // Handle simple format (e.g., 123 or 123A)
                int mainNumber = Integer.parseInt(numericPart);
                if (mainNumber < MIN_HOUSE_NUMBER || mainNumber > MAX_HOUSE_NUMBER) {
                    System.out.println("Error: House number must be between " + MIN_HOUSE_NUMBER + " and " + MAX_HOUSE_NUMBER + ".");
                    return false;
                }
            }

            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid house number format. Please try again.");
            return false;
        }
    }

    private String inputApartmentNumber() {
        return inputWithValidation(
                "Apartment number",
                APARTMENT_PATTERN,
                "Apartment number must be a number between " + MIN_APARTMENT_NUMBER + " and " + MAX_APARTMENT_NUMBER + " (no leading zero)",
                "This field is required"
        );
    }

    private String inputWithValidation(String fieldName, Pattern pattern, String patternErrorMessage, String requiredErrorMessage) {
        while (true) {
            System.out.print(fieldName + ": ");
            String input = scanner.nextLine().trim();

            // Check required field using regex
            if (!REQUIRED_FIELD_PATTERN.matcher(input).matches()) {
                System.out.println("Error: " + requiredErrorMessage + ". Please try again.");
                continue;
            }

            // Validate pattern
            if (pattern.matcher(input).matches()) {
                return input;
            } else {
                System.out.println("Error: " + patternErrorMessage + ".");
            }
        }
    }

    public void close() {
        scanner.close();
    }
}