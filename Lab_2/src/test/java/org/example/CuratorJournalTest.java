package org.example;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class CuratorJournalTest {

    @Test
    void testAddEntry() {
        CuratorJournal journal = new CuratorJournal();
        int initialCount = journal.getEntriesCount();

        System.out.println("=== Test Add Entry ===");
        System.out.println("Initial entries count: " + initialCount);

        JournalEntry entry = new JournalEntry("Smith", "John",
                LocalDate.of(2000, 1, 15), "+380991234567",
                "Main", "15A", "42");

        journal.addEntry(entry);
        int finalCount = journal.getEntriesCount();

        System.out.println("Final entries count: " + finalCount);

        assertEquals(initialCount + 1, finalCount,
                "Entries count should increase by 1 after adding an entry");
        assertEquals(1, finalCount,
                "Journal should contain exactly 1 entry");

        System.out.println("Entry added successfully\n");
    }

    @Test
    void testGetAllEntries() {
        CuratorJournal journal = new CuratorJournal();

        JournalEntry entry1 = new JournalEntry("Smith", "John",
                LocalDate.of(2000, 1, 15), "+380991234567",
                "Main", "15A", "42");

        JournalEntry entry2 = new JournalEntry("Johnson", "Mary",
                LocalDate.of(2001, 5, 20), "+380991234568",
                "Oak", "20", "15");

        journal.addEntry(entry1);
        journal.addEntry(entry2);

        System.out.println("=== Test Get All Entries ===");

        var allEntries = journal.getAllEntries();
        int entriesCount = allEntries.size();

        System.out.println("Retrieved entries count: " + entriesCount);
        System.out.println("Expected entries count: 2");

        assertEquals(2, entriesCount,
                "Should retrieve exactly 2 entries");
        assertTrue(allEntries.contains(entry1),
                "Retrieved entries should contain entry1");
        assertTrue(allEntries.contains(entry2),
                "Retrieved entries should contain entry2");

        System.out.println("All entries retrieved correctly\n");
    }

    @Test
    void testDisplayEmptyJournal() {
        CuratorJournal journal = new CuratorJournal();

        System.out.println("=== Test Display Empty Journal ===");
        System.out.println("Expected: No exception should be thrown");

        assertDoesNotThrow(journal::displayAllEntries,
                "Displaying empty journal should not throw exceptions");

        System.out.println("Empty journal displayed without errors\n");
    }

    @Test
    void testAddNullEntry() {
        CuratorJournal journal = new CuratorJournal();
        int initialCount = journal.getEntriesCount();

        System.out.println("=== Test Add Null Entry ===");
        System.out.println("Initial entries count: " + initialCount);

        journal.addEntry(null);
        int finalCount = journal.getEntriesCount();

        System.out.println("Final entries count: " + finalCount);
        System.out.println("Expected: Count should remain unchanged");

        assertEquals(initialCount, finalCount,
                "Adding null entry should not change entries count");
        assertEquals(0, finalCount,
                "Journal should remain empty after adding null");

        System.out.println("Null entry correctly ignored\n");
    }
}