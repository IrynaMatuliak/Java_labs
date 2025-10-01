package org.example;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class JournalEntryTest {

    @Test
    void testJournalEntryCreation() {
        LocalDate birthDate = LocalDate.of(2000, 1, 15);
        JournalEntry entry = new JournalEntry("Smith", "John", birthDate,
                "+380991234567", "Main", "15A", "42");

        System.out.println("=== Test JournalEntry Creation ===");

        assertEquals("Smith", entry.getLastName(),
                "Expected last name: Smith, but got: " + entry.getLastName());
        assertEquals("John", entry.getFirstName(),
                "Expected first name: John, but got: " + entry.getFirstName());
        assertEquals(birthDate, entry.getBirthDate(),
                "Expected birth date: " + birthDate + ", but got: " + entry.getBirthDate());
        assertEquals("+380991234567", entry.getPhone(),
                "Expected phone: +380991234567, but got: " + entry.getPhone());
        assertEquals("Main", entry.getStreet(),
                "Expected street: Main, but got: " + entry.getStreet());
        assertEquals("15A", entry.getHouse(),
                "Expected house: 15A, but got: " + entry.getHouse());
        assertEquals("42", entry.getApartment(),
                "Expected apartment: 42, but got: " + entry.getApartment());

        System.out.println("All fields correctly initialized\n");
    }

    @Test
    void testToString() {
        LocalDate birthDate = LocalDate.of(2000, 1, 15);
        JournalEntry entry = new JournalEntry("Smith", "John", birthDate,
                "+380991234567", "Main", "15A", "42");

        String expected = "Student: Smith John, Birth Date: 15.01.2000, " +
                "Phone: +380991234567, Address: Main St., Building 15A, Apt. 42";
        String actual = entry.toString();

        System.out.println("=== Test toString Method ===");
        System.out.println("Expected: " + expected);
        System.out.println("Actual:   " + actual);

        assertEquals(expected, actual,
                "ToString output doesn't match expected format");

        System.out.println("ToString method works correctly\n");
    }

    @Test
    void testEqualsAndHashCode() {
        LocalDate birthDate = LocalDate.of(2000, 1, 15);
        JournalEntry entry1 = new JournalEntry("Smith", "John", birthDate,
                "+380991234567", "Main", "15A", "42");
        JournalEntry entry2 = new JournalEntry("Smith", "John", birthDate,
                "+380991234567", "Main", "15A", "42");
        JournalEntry entry3 = new JournalEntry("Johnson", "Mary", birthDate,
                "+380991234568", "Oak", "20", "15");

        System.out.println("=== Test Equals and HashCode ===");

        // Test equals method
        assertTrue(entry1.equals(entry2),
                "Entry1 should be equal to Entry2");
        assertFalse(entry1.equals(entry3),
                "Entry1 should not be equal to Entry3");
        assertFalse(entry1.equals(null),
                "Entry should not be equal to null");
        assertFalse(entry1.equals("Some String"),
                "Entry should not be equal to different type");

        // Test hashCode consistency
        int hash1 = entry1.hashCode();
        int hash2 = entry2.hashCode();
        int hash3 = entry3.hashCode();

        System.out.println("HashCode Entry1: " + hash1);
        System.out.println("HashCode Entry2: " + hash2);
        System.out.println("HashCode Entry3: " + hash3);

        assertEquals(hash1, hash2,
                "Equal objects should have equal hash codes");

        System.out.println("Equals and hashCode work correctly\n");
    }

    @Test
    void testEqualsWithNullFields() {
        JournalEntry entry1 = new JournalEntry(null, "John", null, null, null, null, null);
        JournalEntry entry2 = new JournalEntry(null, "John", null, null, null, null, null);
        JournalEntry entry3 = new JournalEntry("Smith", "John", null, null, null, null, null);

        System.out.println("=== Test Equals with Null Fields ===");

        assertTrue(entry1.equals(entry2),
                "Entries with null fields should be equal if all fields match");
        assertFalse(entry1.equals(entry3),
                "Entries should not be equal if fields differ");

        System.out.println("Equals with null fields works correctly\n");
    }
}