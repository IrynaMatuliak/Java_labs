package org.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class TurnstileTest {
    private Turnstile turnstile;

    @BeforeEach
    public void setUp() {
        turnstile = new Turnstile();
    }

    @Test
    public void testIssueCard() {
        Card card = new TripBasedCard("TEST001", "Student", LocalDate.now().plusDays(30), 10);
        turnstile.issueCard(card);

        assertTrue(turnstile.isCardIdExists("TEST001"));
    }

    @Test
    public void testCardIdExists() {
        Card card = new TripBasedCard("TEST002", "Regular", LocalDate.now().plusDays(30), 5);
        turnstile.issueCard(card);

        assertTrue(turnstile.isCardIdExists("TEST002"));
        assertFalse(turnstile.isCardIdExists("NONEXISTENT"));
    }

    @Test
    public void testGetStatistics() {
        assertEquals(0, turnstile.getTotalPasses());
        assertEquals(0, turnstile.getTotalDenials());

        // Initial state should have empty maps
        assertTrue(turnstile.getPassesByType().isEmpty());
        assertTrue(turnstile.getDenialsByType().isEmpty());
    }
}