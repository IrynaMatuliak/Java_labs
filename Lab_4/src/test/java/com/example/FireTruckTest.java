package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class FireTruckTest {
    private FireTruck fireTruck;
    private Firefighter mike;
    private Firefighter bob;
    private Firefighter alice;
    private Person john;
    private PoliceOfficer sarah;

    @BeforeEach
    void setUp() {
        fireTruck = new FireTruck(6);
        mike = new Firefighter("Mike");
        bob = new Firefighter("Bob");
        alice = new Firefighter("Alice");
        john = new Person("John") {};
        sarah = new PoliceOfficer("Sarah");
    }

    @Test
    void testFireTruckCanCarryFirefighters() {
        fireTruck.boardPassenger(mike);
        fireTruck.boardPassenger(bob);
        assertEquals(2, fireTruck.getOccupiedSeats());
        assertEquals(6, fireTruck.getMaxCapacity());
    }

    @Test
    void testFireTruckInheritance() {
        assertTrue(fireTruck instanceof Car);
        assertTrue(fireTruck instanceof Vehicle);
    }

    @Test
    void testFireTruckGenericConstraint() {
        fireTruck.boardPassenger(mike);
        assertEquals(1, fireTruck.getOccupiedSeats());
    }

    @Test
    void testFireTruckCapacityLimit() {
        FireTruck smallFireTruck = new FireTruck(1);
        smallFireTruck.boardPassenger(mike);

        assertThrows(IllegalStateException.class, () -> {
            smallFireTruck.boardPassenger(bob);
        });
    }

    @Test
    void testFireTruckFullCapacity() {
        FireTruck smallFireTruck = new FireTruck(2);
        smallFireTruck.boardPassenger(mike);
        smallFireTruck.boardPassenger(bob);

        assertEquals(2, smallFireTruck.getOccupiedSeats());
        assertEquals(2, smallFireTruck.getMaxCapacity());

        // Спроба додати третього пожежника
        Firefighter extraFirefighter = new Firefighter("Extra");
        assertThrows(IllegalStateException.class, () -> {
            smallFireTruck.boardPassenger(extraFirefighter);
        });
    }

    @Test
    void testBoardSameFirefighterTwice() {
        fireTruck.boardPassenger(mike);
        assertThrows(IllegalStateException.class, () -> {
            fireTruck.boardPassenger(mike);
        });
    }

    @Test
    void testDisembarkFromEmptyFireTruck() {
        assertThrows(IllegalArgumentException.class, () -> {
            fireTruck.disembarkPassenger(mike);
        });
    }

    @Test
    void testDisembarkAllFirefighters() {
        fireTruck.boardPassenger(mike);
        fireTruck.boardPassenger(bob);
        fireTruck.boardPassenger(alice);
        assertEquals(3, fireTruck.getOccupiedSeats());
        fireTruck.disembarkPassenger(mike);
        fireTruck.disembarkPassenger(bob);
        fireTruck.disembarkPassenger(alice);
        assertEquals(0, fireTruck.getOccupiedSeats());
        assertTrue(fireTruck.getPassengers().isEmpty());
    }

    @Test
    void testFireTruckCapacityAfterDisembark() {
        FireTruck smallFireTruck = new FireTruck(2);
        smallFireTruck.boardPassenger(mike);
        smallFireTruck.boardPassenger(bob);
        assertEquals(2, smallFireTruck.getOccupiedSeats());
        smallFireTruck.disembarkPassenger(mike);
        assertEquals(1, smallFireTruck.getOccupiedSeats());
        smallFireTruck.boardPassenger(alice);
        assertEquals(2, smallFireTruck.getOccupiedSeats());
    }

    @Test
    void testFireTruckWithNullPassenger() {
        assertThrows(NullPointerException.class, () -> {
            fireTruck.boardPassenger(null);
        });
    }

    @Test
    void testDisembarkNullPassengerFromFireTruck() {
        assertThrows(NullPointerException.class, () -> {
            fireTruck.disembarkPassenger(null);
        });
    }

    @Test
    void testFireTruckGetPassengersReturnsCopy() {
        fireTruck.boardPassenger(mike);
        fireTruck.boardPassenger(bob);
        List<Firefighter> passengers1 = fireTruck.getPassengers();
        List<Firefighter> passengers2 = fireTruck.getPassengers();
        assertEquals(passengers1.size(), passengers2.size());
        assertNotSame(passengers1, passengers2);
        passengers1.clear();
        assertEquals(2, fireTruck.getOccupiedSeats()); // Повинно залишитись 2
    }

    @Test
    void testFireTruckMixedOperations() {
        fireTruck.boardPassenger(mike);
        fireTruck.boardPassenger(bob);
        assertEquals(2, fireTruck.getOccupiedSeats());
        fireTruck.disembarkPassenger(mike);
        assertEquals(1, fireTruck.getOccupiedSeats());
        fireTruck.boardPassenger(alice);
        assertEquals(2, fireTruck.getOccupiedSeats());
        List<Firefighter> passengers = fireTruck.getPassengers();
        assertTrue(passengers.contains(bob));
        assertTrue(passengers.contains(alice));
        assertFalse(passengers.contains(mike));
    }

    @Test
    void testFireTruckToString() {
        fireTruck.boardPassenger(mike);
        fireTruck.boardPassenger(bob);
        String result = fireTruck.toString();
        assertTrue(result.contains("FireTruck"));
        assertTrue(result.contains("capacity=6"));
        assertTrue(result.contains("occupied=2"));
    }

    @Test
    void testFireTruckWithDifferentFirefighterInstancesSameName() {
        Firefighter mike1 = new Firefighter("Mike");
        Firefighter mike2 = new Firefighter("Mike");
        fireTruck.boardPassenger(mike1);
        fireTruck.boardPassenger(mike2);
        assertEquals(2, fireTruck.getOccupiedSeats());
    }

    @Test
    void testFireTruckMultipleDisembarkSameFirefighter() {
        fireTruck.boardPassenger(mike);
        fireTruck.disembarkPassenger(mike);
        assertThrows(IllegalArgumentException.class, () -> {
            fireTruck.disembarkPassenger(mike);
        });
    }

    @Test
    void testFireTruckPassengerListIntegrity() {
        fireTruck.boardPassenger(mike);
        fireTruck.boardPassenger(bob);
        List<Firefighter> initialPassengers = fireTruck.getPassengers();
        assertEquals(2, initialPassengers.size());
        fireTruck.disembarkPassenger(mike);
        List<Firefighter> afterDisembarkPassengers = fireTruck.getPassengers();
        assertEquals(1, afterDisembarkPassengers.size());
        assertTrue(afterDisembarkPassengers.contains(bob));
        assertFalse(afterDisembarkPassengers.contains(mike));
        assertEquals(2, initialPassengers.size());
    }

    @Test
    void testFireTruckTypicalEmergencyScenario() {
        FireTruck emergencyTruck = new FireTruck(5);
        emergencyTruck.boardPassenger(mike);
        emergencyTruck.boardPassenger(bob);
        emergencyTruck.boardPassenger(alice);
        assertEquals(3, emergencyTruck.getOccupiedSeats());
        emergencyTruck.disembarkPassenger(mike);
        assertEquals(2, emergencyTruck.getOccupiedSeats());
        Firefighter charlie = new Firefighter("Charlie");
        emergencyTruck.boardPassenger(charlie);
        assertEquals(3, emergencyTruck.getOccupiedSeats());
        List<Firefighter> finalPassengers = emergencyTruck.getPassengers();
        assertTrue(finalPassengers.contains(bob));
        assertTrue(finalPassengers.contains(alice));
        assertTrue(finalPassengers.contains(charlie));
        assertFalse(finalPassengers.contains(mike));
    }

    @Test
    void testFireTruckGenericTypeSafety() {
        FireTruck strictFireTruck = new FireTruck(3);
        strictFireTruck.boardPassenger(mike);
        strictFireTruck.boardPassenger(bob);
        assertEquals(2, strictFireTruck.getOccupiedSeats());
        // strictFireTruck.boardPassenger(john);    // Compilation error - Regular Person
        // strictFireTruck.boardPassenger(sarah);   // Compilation error - Police Officer
    }

    @Test
    void testFireTruckInheritanceChain() {
        assertTrue(fireTruck instanceof FireTruck);
        assertTrue(fireTruck instanceof Car);
        assertTrue(fireTruck instanceof Vehicle);
        assertEquals(6, fireTruck.getMaxCapacity());
        fireTruck.boardPassenger(mike);
        assertEquals(1, fireTruck.getOccupiedSeats());
    }

    @Test
    void testFireTruckToStringAfterMultipleOperations() {
        fireTruck.boardPassenger(mike);
        fireTruck.boardPassenger(bob);
        String result1 = fireTruck.toString();
        assertTrue(result1.contains("occupied=2"));
        fireTruck.disembarkPassenger(mike);
        String result2 = fireTruck.toString();
        assertTrue(result2.contains("occupied=1"));
        fireTruck.boardPassenger(alice);
        String result3 = fireTruck.toString();
        assertTrue(result3.contains("occupied=2"));
    }
}