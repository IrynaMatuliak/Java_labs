package org.example.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AccumulatingCardTest {

    @Test
    public void testAccumulatingCardCreation() {
        AccumulatingCard card = new AccumulatingCard("ACC001", 50.0);

        assertEquals("ACC001", card.getId());
        assertEquals("Regular", card.getType());
        assertEquals(50.0, card.getBalance(), 0.001);
        assertTrue(card.isActive());
        assertFalse(card.isExpired());
    }

    @Test
    public void testValidWhenSufficientBalance() {
        AccumulatingCard card = new AccumulatingCard("ACC002", 10.0);
        assertTrue(card.isValid());
    }

    @Test
    public void testInvalidWhenInsufficientBalance() {
        AccumulatingCard card = new AccumulatingCard("ACC003", 5.0);
        assertFalse(card.isValid());
    }

    @Test
    public void testUseTripSuccess() {
        AccumulatingCard card = new AccumulatingCard("ACC004", 20.0);
        double initialBalance = card.getBalance();

        assertTrue(card.useTrip());
        assertEquals(initialBalance - 8.0, card.getBalance(), 0.001);
    }

    @Test
    public void testUseTripFailure() {
        AccumulatingCard card = new AccumulatingCard("ACC005", 5.0);

        assertFalse(card.useTrip());
        assertEquals(5.0, card.getBalance(), 0.001);
    }

    @Test
    public void testTopUp() {
        AccumulatingCard card = new AccumulatingCard("ACC006", 20.0);
        card.topUp(30.0);

        assertEquals(50.0, card.getBalance(), 0.001);
    }

    @Test
    public void testTopUpWithZero() {
        AccumulatingCard card = new AccumulatingCard("ACC007", 20.0);
        card.topUp(0.0);

        assertEquals(20.0, card.getBalance(), 0.001);
    }

    @Test
    public void testTopUpWithNegative() {
        AccumulatingCard card = new AccumulatingCard("ACC008", 20.0);
        card.topUp(-10.0);

        assertEquals(20.0, card.getBalance(), 0.001);
    }

    @Test
    public void testEqualsAndHashCode() {
        AccumulatingCard card1 = new AccumulatingCard("ACC009", 50.0);
        AccumulatingCard card2 = new AccumulatingCard("ACC009", 50.0);
        AccumulatingCard card3 = new AccumulatingCard("ACC010", 50.0);
        AccumulatingCard card4 = new AccumulatingCard("ACC009", 100.0);

        // Test equals
        assertEquals(card1, card2);
        assertNotEquals(card1, card3);
        assertNotEquals(card1, card4);

        // Test hashCode consistency
        assertEquals(card1.hashCode(), card2.hashCode());
        assertNotEquals(card1.hashCode(), card3.hashCode());
        assertNotEquals(card1.hashCode(), card4.hashCode());

        // Test with null and different class
        assertNotEquals(null, card1);
        assertNotEquals("string", card1);
    }

    @Test
    public void testHashCodeConsistency() {
        AccumulatingCard card = new AccumulatingCard("ACC011", 75.0);
        int initialHashCode = card.hashCode();

        // Hash code should remain consistent
        assertEquals(initialHashCode, card.hashCode());
        assertEquals(initialHashCode, card.hashCode());
    }

    @Test
    public void testEqualsSymmetry() {
        AccumulatingCard card1 = new AccumulatingCard("ACC012", 25.0);
        AccumulatingCard card2 = new AccumulatingCard("ACC012", 25.0);

        assertTrue(card1.equals(card2) && card2.equals(card1));
    }

    @Test
    public void testEqualsTransitivity() {
        AccumulatingCard card1 = new AccumulatingCard("ACC013", 30.0);
        AccumulatingCard card2 = new AccumulatingCard("ACC013", 30.0);
        AccumulatingCard card3 = new AccumulatingCard("ACC013", 30.0);

        assertTrue(card1.equals(card2));
        assertTrue(card2.equals(card3));
        assertTrue(card1.equals(card3));
    }

    @Test
    public void testDeactivate() {
        AccumulatingCard card = new AccumulatingCard("ACC014", 40.0);
        assertTrue(card.isActive());

        card.deactivate();
        assertFalse(card.isActive());
        assertFalse(card.isValid());
    }
}