package org.example.model;

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
        TripBasedCard card = new TripBasedCard("REG003", "Regular", futureDate, 3);

        assertTrue(card.useTrip());
        assertEquals(2, card.getTripsRemaining());
    }

    @Test
    public void testUseTripFailureWhenNoTrips() {
        LocalDate futureDate = LocalDate.now().plusDays(10);
        TripBasedCard card = new TripBasedCard("REG004", "Regular", futureDate, 0);

        assertFalse(card.useTrip());
        assertEquals(0, card.getTripsRemaining());
    }

    @Test
    public void testUseTripFailureWhenExpired() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        TripBasedCard card = new TripBasedCard("REG005", "Regular", pastDate, 5);

        assertFalse(card.useTrip());
        assertEquals(5, card.getTripsRemaining());
    }

    @Test
    public void testRemainingValidityDays() {
        LocalDate futureDate = LocalDate.now().plusDays(15);
        TripBasedCard card = new TripBasedCard("TEST001", "Student", futureDate, 5);

        long remainingDays = card.getRemainingValidityDays();
        assertTrue(remainingDays >= 14 && remainingDays <= 15);
    }

    @Test
    public void testEqualsAndHashCode() {
        LocalDate date1 = LocalDate.now().plusDays(10);
        LocalDate date2 = LocalDate.now().plusDays(10);
        LocalDate date3 = LocalDate.now().plusDays(20);

        TripBasedCard card1 = new TripBasedCard("CARD001", "Student", date1, 5);
        TripBasedCard card2 = new TripBasedCard("CARD001", "Student", date2, 5);
        TripBasedCard card3 = new TripBasedCard("CARD002", "Student", date1, 5);
        TripBasedCard card4 = new TripBasedCard("CARD001", "Pupil", date1, 5);
        TripBasedCard card5 = new TripBasedCard("CARD001", "Student", date3, 5);
        TripBasedCard card6 = new TripBasedCard("CARD001", "Student", date1, 10);

        // Test equals
        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
        assertNotEquals(card1, card4);
        assertNotEquals(card1, card5);
        assertNotEquals(card1, card6);

        // Test hashCode consistency
        assertEquals(card1.hashCode(), card2.hashCode());
        assertNotEquals(card1.hashCode(), card3.hashCode());

        // Test with null and different class
        assertNotEquals(null, card1);
        assertNotEquals("string", card1);
        assertNotEquals(card1, new AccumulatingCard("CARD001", 50.0));
    }

    @Test
    public void testHashCodeConsistency() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        TripBasedCard card = new TripBasedCard("CONSISTENT", "Regular", futureDate, 10);
        int initialHashCode = card.hashCode();

        assertEquals(initialHashCode, card.hashCode());
        assertEquals(initialHashCode, card.hashCode());
    }

    @Test
    public void testEqualsReflexivity() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        TripBasedCard card = new TripBasedCard("REF001", "Student", futureDate, 5);

        assertEquals(card, card);
    }

    @Test
    public void testEqualsSymmetry() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        TripBasedCard card1 = new TripBasedCard("SYM001", "Regular", futureDate, 10);
        TripBasedCard card2 = new TripBasedCard("SYM001", "Regular", futureDate, 10);

        assertTrue(card1.equals(card2) && card2.equals(card1));
    }

    @Test
    public void testDeactivate() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        TripBasedCard card = new TripBasedCard("DEACT001", "Pupil", futureDate, 5);

        assertTrue(card.isActive());
        assertTrue(card.isValid());

        card.deactivate();
        assertFalse(card.isActive());
        assertFalse(card.isValid());
    }

    @Test
    public void testDifferentCardTypes() {
        LocalDate futureDate = LocalDate.now().plusDays(30);
        TripBasedCard studentCard = new TripBasedCard("TYPE001", "Student", futureDate, 5);
        TripBasedCard pupilCard = new TripBasedCard("TYPE001", "Pupil", futureDate, 5);
        TripBasedCard regularCard = new TripBasedCard("TYPE001", "Regular", futureDate, 5);

        assertNotEquals(studentCard, pupilCard);
        assertNotEquals(studentCard, regularCard);
        assertNotEquals(pupilCard, regularCard);

        assertNotEquals(studentCard.hashCode(), pupilCard.hashCode());
        assertNotEquals(studentCard.hashCode(), regularCard.hashCode());
    }
}