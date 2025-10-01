package org.example;

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
}