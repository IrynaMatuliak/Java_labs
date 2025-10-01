package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class TripBasedCardTest {

    @Test
    public void testTripBasedCardCreation() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        TripBasedCard card = new TripBasedCard("STU001", "Student", futureDate, 10);

        assertEquals("STU001", card.getId());
        assertEquals("Student", card.getType());
        assertEquals(10, card.getTripsRemaining());
        assertTrue(card.isActive());
    }

    @Test
    public void testValidWhenHasTripsAndNotExpired() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        TripBasedCard card = new TripBasedCard("REG001", "Regular", futureDate, 5);
        assertTrue(card.isValid());
    }

    @Test
    public void testInvalidWhenNoTrips() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        TripBasedCard card = new TripBasedCard("PUP001", "Pupil", futureDate, 0);
        assertFalse(card.isValid());
    }

    @Test
    public void testInvalidWhenExpired() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        TripBasedCard card = new TripBasedCard("REG002", "Regular", pastDate, 5);
        assertFalse(card.isValid());
        assertTrue(card.isExpired());
    }

    @Test
    public void testUseTripSuccess() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        TripBasedCard card = new TripBasedCard("REG002", "Regular", futureDate, 3);

        assertTrue(card.useTrip());
        assertEquals(2, card.getTripsRemaining());
    }

    @Test
    public void testUseTripFailureWhenNoTrips() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        TripBasedCard card = new TripBasedCard("REG003", "Regular", futureDate, 0);

        assertFalse(card.useTrip());
        assertEquals(0, card.getTripsRemaining());
    }

    @Test
    public void testRemainingValidityDays() {
        LocalDate futureDate = LocalDate.now().plusDays(15);
        TripBasedCard card = new TripBasedCard("TEST001", "Student", futureDate, 5);

        long remainingDays = card.getRemainingValidityDays();
        assertTrue(remainingDays >= 14 && remainingDays <= 15);
    }
}