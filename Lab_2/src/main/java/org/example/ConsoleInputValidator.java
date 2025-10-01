package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

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
    private static final int PHONE_LENGTH = 13;
    private static final int DATE_LENGTH = 10;
    private static final char DATE_SEPARATOR = '.';
    private static final String PHONE_PREFIX = "+380";

    // Date format positions
    private static final int DAY_START = 0;
    private static final int DAY_END = 2;
    private static final int MONTH_START = 3;
    private static final int MONTH_END = 5;
    private static final int YEAR_START = 6;
    private static final int YEAR_END = 10;

    // Days in each month (non-leap year)
    private static final int[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public ConsoleInputValidator() {
        this.scanner = new Scanner(System.in);
    }

    public JournalEntry inputJournalEntry() {
        System.out.println("\n=== Add New Journal Entry ===");

        String lastName = inputName("Last name", true);
        String firstName = inputName("First name", true);
        LocalDate birthDate = inputBirthDate();
        String phone = inputPhone();
        String street = inputStreet();
        String house = inputHouseNumber();
        String apartment = inputApartmentNumber();

        return new JournalEntry(lastName, firstName, birthDate, phone, street, house, apartment);
    }

    private String inputName(String fieldName, boolean isRequired) {
        while (true) {
            System.out.print(fieldName + ": ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                if (isRequired) {
                    System.out.println("Error: This field is required. Please try again.");
                    continue;
                } else {
                    return input;
                }
            }

            if (!Character.isUpperCase(input.charAt(0))) {
                System.out.println("Error: Name must start with uppercase letter. Please try again.");
                continue;
            }

            // Character validation: letters, apostrophes, and hyphens are allowed
            boolean valid = true;
            for (int i = 1; i < input.length(); i++) {
                char c = input.charAt(i);
                if (!Character.isLetter(c) && c != '\'' && c != '-') {
                    valid = false;
                    break;
                }
            }
            if (!valid) {
                System.out.println("Error: Name can only contain letters, apostrophes and hyphens. Please try again.");
                continue;
            }
            if (input.length() > MAX_NAME_LENGTH) {
                System.out.println("Error: Name cannot exceed " + MAX_NAME_LENGTH + " characters. Please try again.");
                continue;
            }
            return input;
        }
    }

    private LocalDate inputBirthDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate now = LocalDate.now();
        int currentYear = now.getYear();

        while (true) {
            System.out.print("Birth date (dd.MM.yyyy): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: This field is required. Please try again.");
                continue;
            }

            // Basic format validation
            if (input.length() != DATE_LENGTH || input.charAt(2) != DATE_SEPARATOR || input.charAt(5) != DATE_SEPARATOR) {
                System.out.println("Error: Date must be in format dd.MM.yyyy. Please try again.");
                continue;
            }

            try {
                // Extract day, month, year parts
                String dayStr = input.substring(DAY_START, DAY_END);
                String monthStr = input.substring(MONTH_START, MONTH_END);
                String yearStr = input.substring(YEAR_START, YEAR_END);

                int day = Integer.parseInt(dayStr);
                int month = Integer.parseInt(monthStr);
                int year = Integer.parseInt(yearStr);

                // Validate year range
                if (year < MIN_BIRTH_YEAR || year > currentYear) {
                    System.out.println("Error: Year must be between " + MIN_BIRTH_YEAR + " and " + currentYear + ". Please try again.");
                    continue;
                }

                // Validate month
                if (month < 1 || month > 12) {
                    System.out.println("Error: Month must be between 01 and 12. Please try again.");
                    continue;
                }

                // Validate day based on month and leap year
                if (!isValidDayForMonth(day, month, year)) {
                    int maxDays = getDaysInMonth(month, year);
                    System.out.println("Error: Invalid date '" + input + "'. " +
                            getMonthName(month) + " " + year + " has only " + maxDays + " days" +
                            (isLeapYear(year) && month == 2 ? " (leap year)" : "") + ". Please try again.");
                    continue;
                }

                LocalDate date = LocalDate.parse(input, formatter);

                // Check if date is in future
                if (date.isAfter(now)) {
                    System.out.println("Error: Birth date cannot be in the future. Please try again.");
                    continue;
                }

                // Check if date is too old
                if (date.isBefore(now.minusYears(MAX_AGE_YEARS))) {
                    System.out.println("Error: Birth date cannot be more than " + MAX_AGE_YEARS + " years ago. Please try again.");
                    continue;
                }

                return date;

            } catch (DateTimeParseException e) {
                System.out.println("Error: Invalid date format. Please check the date values and try again.");
            } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                System.out.println("Error: Date must contain only numbers in format dd.MM.yyyy. Please try again.");
            }
        }
    }

    private boolean isValidDayForMonth(int day, int month, int year) {
        if (day < 1) return false;

        int maxDays = getDaysInMonth(month, year);
        return day <= maxDays;
    }

    private int getDaysInMonth(int month, int year) {
        if (month == 2 && isLeapYear(year)) {
            return 29;
        }
        return DAYS_IN_MONTH[month - 1];
    }

    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private String getMonthName(int month) {
        String[] monthNames = {
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        };
        return monthNames[month - 1];
    }

    private String inputPhone() {
        while (true) {
            System.out.print("Phone (" + PHONE_PREFIX + "XXXXXXXXX): ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: This field is required. Please try again.");
                continue;
            }

            if (!input.startsWith(PHONE_PREFIX)) {
                System.out.println("Error: Phone must start with " + PHONE_PREFIX + ". Please try again.");
                continue;
            }

            if (input.length() != PHONE_LENGTH) {
                System.out.println("Error: Phone must be exactly " + PHONE_LENGTH + " characters long. Please try again.");
                continue;
            }

            boolean valid = true;
            for (int i = 1; i < input.length(); i++) {
                if (!Character.isDigit(input.charAt(i))) {
                    valid = false;
                    break;
                }
            }

            if (!valid) {
                System.out.println("Error: Phone can only contain digits after " + PHONE_PREFIX + ". Please try again.");
                continue;
            }

            return input;
        }
    }

    private String inputStreet() {
        while (true) {
            System.out.print("Street: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: This field is required. Please try again.");
                continue;
            }

            if (!Character.isUpperCase(input.charAt(0))) {
                System.out.println("Error: Street name must start with uppercase letter. Please try again.");
                continue;
            }

            if (input.length() > MAX_STREET_LENGTH) {
                System.out.println("Error: Street name cannot exceed " + MAX_STREET_LENGTH + " characters. Please try again.");
                continue;
            }

            return input;
        }
    }

    private String inputHouseNumber() {
        while (true) {
            System.out.print("House number: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: This field is required. Please try again.");
                continue;
            }

            if (!Character.isDigit(input.charAt(0))) {
                System.out.println("Error: House number must start with a digit. Please try again.");
                continue;
            }

            int i = 0;
            boolean hasFraction = false;
            boolean validFormat = true;

            while (i < input.length()) {
                char currentChar = input.charAt(i);
                if (Character.isDigit(currentChar)) {
                    i++;
                } else if (currentChar == '/') {
                    // Cannot be: "/15", "15/", two "/"
                    if (hasFraction || i == 0 || i == input.length() - 1) {
                        validFormat = false;
                        break;
                    }
                    hasFraction = true;
                    i++;
                } else if (Character.isLetter(currentChar)) {
                    // Letters can only be at the end
                    if (i < input.length() - 1) {
                        boolean onlyLettersRemaining = true;
                        // Check that all following characters are letters
                        for (int j = i + 1; j < input.length(); j++) {
                            if (!Character.isLetter(input.charAt(j))) {
                                onlyLettersRemaining = false;
                                break;
                            }
                        }
                        if (!onlyLettersRemaining) {
                            validFormat = false;
                            break;
                        }
                    }
                    while (i < input.length() && Character.isLetter(input.charAt(i))) {
                        i++;
                    }
                    break;
                } else {
                    validFormat = false; // Other characters are not allowed
                    break;
                }
            }

            if (!validFormat) {
                System.out.println("Error: Invalid house number format. Please try again.");
                continue;
            }

            String mainNumberPart;
            if (hasFraction) {
                int fractionIndex = input.indexOf('/');
                mainNumberPart = input.substring(0, fractionIndex);
            } else {
                int digitEnd = 0;
                while (digitEnd < input.length() && Character.isDigit(input.charAt(digitEnd))) {
                    digitEnd++;
                }
                mainNumberPart = input.substring(0, digitEnd);
            }

            if (mainNumberPart.isEmpty()) {
                System.out.println("Error: House number must contain digits. Please try again.");
                continue;
            }

            try {
                int houseNumber = Integer.parseInt(mainNumberPart);
                if (houseNumber < MIN_HOUSE_NUMBER || houseNumber > MAX_HOUSE_NUMBER) {
                    System.out.println("Error: House number must be between " + MIN_HOUSE_NUMBER + " and " + MAX_HOUSE_NUMBER + ". Please try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid house number format. Please try again.");
                continue;
            }

            if (input.endsWith("/")) {
                System.out.println("Error: House number cannot end with a fraction. Please try again.");
                continue;
            }

            return input;
        }
    }

    private String inputApartmentNumber() {
        while (true) {
            System.out.print("Apartment number: ");
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println("Error: This field is required. Please try again.");
                continue;
            }

            boolean allDigits = true;
            for (char c : input.toCharArray()) {
                if (!Character.isDigit(c)) {
                    allDigits = false;
                    break;
                }
            }

            if (!allDigits) {
                System.out.println("Error: Apartment number can only contain digits. Please try again.");
                continue;
            }

            try {
                int apartmentNumber = Integer.parseInt(input);
                if (apartmentNumber < MIN_APARTMENT_NUMBER || apartmentNumber > MAX_APARTMENT_NUMBER) {
                    System.out.println("Error: Apartment number must be between " + MIN_APARTMENT_NUMBER + " and " + MAX_APARTMENT_NUMBER + ". Please try again.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Invalid apartment number format. Please try again.");
                continue;
            }

            return input;
        }
    }

    public void close() {
        scanner.close();
    }
}