package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class CardTest {

    @Test
    public void testCardCreation() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        Card card = new Card("BASE001", "Regular", futureDate, 10);

        assertEquals("BASE001", card.getId());
        assertEquals("Regular", card.getType());
        assertEquals(futureDate, card.getExpiryDate());
        assertEquals(10, card.getTripsRemaining());
        assertTrue(card.isActive());
        assertNotNull(card.getIssueDate());
    }

    @Test
    public void testCardValidity() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        LocalDate pastDate = LocalDate.now().minusDays(1);

        Card validCard = new Card("VALID001", "Student", futureDate, 5);
        Card expiredCard = new Card("EXPIRED001", "Student", pastDate, 5);
        Card noTripsCard = new Card("NOTRIPS001", "Student", futureDate, 0);
        Card inactiveCard = new Card("INACTIVE001", "Student", futureDate, 5);

        assertTrue(validCard.isValid());
        assertFalse(expiredCard.isValid());
        assertFalse(noTripsCard.isValid());

        inactiveCard.deactivate();
        assertFalse(inactiveCard.isValid());
    }

    @Test
    public void testUseTrip() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        Card card = new Card("USE001", "Regular", futureDate, 3);

        assertTrue(card.useTrip());
        assertEquals(2, card.getTripsRemaining());

        assertTrue(card.useTrip());
        assertEquals(1, card.getTripsRemaining());

        assertTrue(card.useTrip());
        assertEquals(0, card.getTripsRemaining());

        assertFalse(card.useTrip());
        assertEquals(0, card.getTripsRemaining());
    }

    @Test
    public void testRemainingValidityDays() {
        LocalDate futureDate = LocalDate.now().plusDays(25);
        Card card = new Card("DAYS001", "Regular", futureDate, 5);

        long remainingDays = card.getRemainingValidityDays();
        assertTrue(remainingDays >= 24 && remainingDays <= 25);
    }

    @Test
    public void testHashCodeWithNullFields() {
        Card card = new Card(null, null, null, 0);

        // Should not throw NullPointerException
        int hashCode = card.hashCode();
        assertTrue(hashCode != 0);
    }
}