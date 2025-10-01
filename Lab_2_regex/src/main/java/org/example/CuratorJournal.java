package org.example;

import java.util.ArrayList;
import java.util.List;

public class CuratorJournal {
    private final List<JournalEntry> entries;

    public CuratorJournal() {
        this.entries = new ArrayList<>();
    }

    public void addEntry(JournalEntry entry) {
        if (entry != null) {
            entries.add(entry);
        }
    }

    public List<JournalEntry> getAllEntries() {
        return new ArrayList<>(entries);
    }

    public void displayAllEntries() {
        if (entries.isEmpty()) {
            System.out.println("\nCurator journal is empty.");
            return;
        }

        System.out.println("\n=== ALL CURATOR JOURNAL ENTRIES ===");
        for (int i = 0; i < entries.size(); i++) {
            System.out.println((i + 1) + ". " + entries.get(i));
        }
        System.out.println("===================================");
    }

    public int getEntriesCount() {
        return entries.size();
    }
}