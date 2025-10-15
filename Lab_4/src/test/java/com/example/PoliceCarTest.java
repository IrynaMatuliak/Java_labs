package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class PoliceCarTest {
    private PoliceCar policeCar;
    private PoliceOfficer sarah;
    private PoliceOfficer tom;
    private PoliceOfficer alice;
    private Person john;
    private Firefighter mike;

    @BeforeEach
    void setUp() {
        policeCar = new PoliceCar(2);
        sarah = new PoliceOfficer("Sarah");
        tom = new PoliceOfficer("Tom");
        alice = new PoliceOfficer("Alice");
        john = new Person("John") {};
        mike = new Firefighter("Mike");
    }

    @Test
    void testPoliceCarCanCarryPoliceOfficers() {
        policeCar.boardPassenger(sarah);
        policeCar.boardPassenger(tom);
        assertEquals(2, policeCar.getOccupiedSeats());
        assertEquals(2, policeCar.getMaxCapacity());
    }

    @Test
    void testPoliceCarInheritance() {
        assertTrue(policeCar instanceof Car);
        assertTrue(policeCar instanceof Vehicle);
    }

    @Test
    void testPoliceCarCapacityLimit() {
        PoliceCar smallPoliceCar = new PoliceCar(1);
        smallPoliceCar.boardPassenger(sarah);

        assertThrows(IllegalStateException.class, () -> {
            smallPoliceCar.boardPassenger(tom);
        });
    }

    @Test
    void testPoliceCarDisembark() {
        policeCar.boardPassenger(sarah);
        policeCar.disembarkPassenger(sarah);
        assertEquals(0, policeCar.getOccupiedSeats());
    }

    @Test
    void testPoliceCarFullCapacity() {
        PoliceCar smallPoliceCar = new PoliceCar(2);
        smallPoliceCar.boardPassenger(sarah);
        smallPoliceCar.boardPassenger(tom);
        assertEquals(2, smallPoliceCar.getOccupiedSeats());
        assertEquals(2, smallPoliceCar.getMaxCapacity());
        PoliceOfficer extraOfficer = new PoliceOfficer("Extra");
        assertThrows(IllegalStateException.class, () -> {
            smallPoliceCar.boardPassenger(extraOfficer);
        });
    }

    @Test
    void testBoardSamePoliceOfficerTwice() {
        policeCar.boardPassenger(sarah);
        assertThrows(IllegalStateException.class, () -> {
            policeCar.boardPassenger(sarah);
        });
    }

    @Test
    void testDisembarkFromEmptyPoliceCar() {
        assertThrows(IllegalArgumentException.class, () -> {
            policeCar.disembarkPassenger(sarah);
        });
    }

    @Test
    void testDisembarkAllPoliceOfficers() {
        policeCar.boardPassenger(sarah);
        policeCar.boardPassenger(tom);
        assertEquals(2, policeCar.getOccupiedSeats());
        policeCar.disembarkPassenger(sarah);
        policeCar.disembarkPassenger(tom);
        assertEquals(0, policeCar.getOccupiedSeats());
        assertTrue(policeCar.getPassengers().isEmpty());
    }

    @Test
    void testPoliceCarCapacityAfterDisembark() {
        PoliceCar smallPoliceCar = new PoliceCar(2);
        smallPoliceCar.boardPassenger(sarah);
        smallPoliceCar.boardPassenger(tom);
        assertEquals(2, smallPoliceCar.getOccupiedSeats());
        smallPoliceCar.disembarkPassenger(sarah);
        assertEquals(1, smallPoliceCar.getOccupiedSeats());
        smallPoliceCar.boardPassenger(alice);
        assertEquals(2, smallPoliceCar.getOccupiedSeats());
    }

    @Test
    void testPoliceCarWithNullPassenger() {
        assertThrows(NullPointerException.class, () -> {
            policeCar.boardPassenger(null);
        });
    }

    @Test
    void testDisembarkNullPassengerFromPoliceCar() {
        assertThrows(NullPointerException.class, () -> {
            policeCar.disembarkPassenger(null);
        });
    }

    @Test
    void testPoliceCarGetPassengersReturnsCopy() {
        policeCar.boardPassenger(sarah);
        policeCar.boardPassenger(tom);
        List<PoliceOfficer> passengers1 = policeCar.getPassengers();
        List<PoliceOfficer> passengers2 = policeCar.getPassengers();
        assertEquals(passengers1.size(), passengers2.size());
        assertNotSame(passengers1, passengers2);
        passengers1.clear();
        assertEquals(2, policeCar.getOccupiedSeats()); // Повинно залишитись 2
    }

    @Test
    void testPoliceCarMixedOperations() {
        policeCar.boardPassenger(sarah);
        policeCar.boardPassenger(tom);
        assertEquals(2, policeCar.getOccupiedSeats());
        policeCar.disembarkPassenger(sarah);
        assertEquals(1, policeCar.getOccupiedSeats());
        policeCar.boardPassenger(alice);
        assertEquals(2, policeCar.getOccupiedSeats());
        List<PoliceOfficer> passengers = policeCar.getPassengers();
        assertTrue(passengers.contains(tom));
        assertTrue(passengers.contains(alice));
        assertFalse(passengers.contains(sarah));
    }

    @Test
    void testPoliceCarToString() {
        policeCar.boardPassenger(sarah);
        policeCar.boardPassenger(tom);
        String result = policeCar.toString();
        assertTrue(result.contains("PoliceCar"));
        assertTrue(result.contains("capacity=2"));
        assertTrue(result.contains("occupied=2"));
    }

    @Test
    void testPoliceCarWithDifferentOfficerInstancesSameName() {
        PoliceOfficer sarah1 = new PoliceOfficer("Sarah");
        PoliceOfficer sarah2 = new PoliceOfficer("Sarah");
        policeCar.boardPassenger(sarah1);
        policeCar.boardPassenger(sarah2);
        assertEquals(2, policeCar.getOccupiedSeats());
    }

    @Test
    void testPoliceCarMultipleDisembarkSameOfficer() {
        policeCar.boardPassenger(sarah);
        policeCar.disembarkPassenger(sarah);
        assertThrows(IllegalArgumentException.class, () -> {
            policeCar.disembarkPassenger(sarah);
        });
    }

    @Test
    void testPoliceCarPassengerListIntegrity() {
        policeCar.boardPassenger(sarah);
        policeCar.boardPassenger(tom);
        List<PoliceOfficer> initialPassengers = policeCar.getPassengers();
        assertEquals(2, initialPassengers.size());
        policeCar.disembarkPassenger(sarah);
        List<PoliceOfficer> afterDisembarkPassengers = policeCar.getPassengers();
        assertEquals(1, afterDisembarkPassengers.size());
        assertTrue(afterDisembarkPassengers.contains(tom));
        assertFalse(afterDisembarkPassengers.contains(sarah));
        assertEquals(2, initialPassengers.size());
    }

    @Test
    void testPoliceCarPatrolScenario() {
        PoliceCar patrolCar = new PoliceCar(3);
        patrolCar.boardPassenger(sarah);
        patrolCar.boardPassenger(tom);
        assertEquals(2, patrolCar.getOccupiedSeats());
        patrolCar.disembarkPassenger(sarah);
        assertEquals(1, patrolCar.getOccupiedSeats());
        patrolCar.boardPassenger(alice);
        assertEquals(2, patrolCar.getOccupiedSeats());
        List<PoliceOfficer> finalPassengers = patrolCar.getPassengers();
        assertTrue(finalPassengers.contains(tom));
        assertTrue(finalPassengers.contains(alice));
        assertFalse(finalPassengers.contains(sarah));
    }

    @Test
    void testPoliceCarGenericTypeSafety() {
        PoliceCar strictPoliceCar = new PoliceCar(3);
        strictPoliceCar.boardPassenger(sarah);
        strictPoliceCar.boardPassenger(tom);
        assertEquals(2, strictPoliceCar.getOccupiedSeats());
        // strictPoliceCar.boardPassenger(john);  // Compilation error - Regular Person
        // strictPoliceCar.boardPassenger(mike);  // Compilation error - Firefighter
    }

    @Test
    void testPoliceCarInheritanceChain() {
        assertTrue(policeCar instanceof PoliceCar);
        assertTrue(policeCar instanceof Car);
        assertTrue(policeCar instanceof Vehicle);
        assertEquals(2, policeCar.getMaxCapacity());
        policeCar.boardPassenger(sarah);
        assertEquals(1, policeCar.getOccupiedSeats());
    }

    @Test
    void testPoliceCarToStringAfterMultipleOperations() {
        policeCar.boardPassenger(sarah);
        policeCar.boardPassenger(tom);
        String result1 = policeCar.toString();
        assertTrue(result1.contains("occupied=2"));
        policeCar.disembarkPassenger(sarah);
        String result2 = policeCar.toString();
        assertTrue(result2.contains("occupied=1"));
        policeCar.boardPassenger(alice);
        String result3 = policeCar.toString();
        assertTrue(result3.contains("occupied=2"));
    }
}