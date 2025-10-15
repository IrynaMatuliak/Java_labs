package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class TaxiTest {
    private Taxi<Person> taxi;
    private Person john;
    private Firefighter mike;
    private PoliceOfficer sarah;

    @BeforeEach
    void setUp() {
        taxi = new Taxi<>(4);
        john = new Person("John") {};
        mike = new Firefighter("Mike");
        sarah = new PoliceOfficer("Sarah");
    }

    @Test
    void testTaxiCanCarryAnyPersonType() {
        taxi.boardPassenger(john);
        taxi.boardPassenger(mike);
        taxi.boardPassenger(sarah);
        assertEquals(3, taxi.getOccupiedSeats());
    }

    @Test
    void testTaxiInheritance() {
        assertTrue(taxi instanceof Car);
        assertTrue(taxi instanceof Vehicle);
    }

    @Test
    void testTaxiCapacity() {
        Taxi<Person> smallTaxi = new Taxi<>(1);
        smallTaxi.boardPassenger(john);
        assertThrows(IllegalStateException.class, () -> {
            smallTaxi.boardPassenger(mike);
        });
    }

    @Test
    void testTaxiFullCapacity() {
        Taxi<Person> smallTaxi = new Taxi<>(2);
        smallTaxi.boardPassenger(john);
        smallTaxi.boardPassenger(mike);
        assertEquals(2, smallTaxi.getOccupiedSeats());
        assertEquals(2, smallTaxi.getMaxCapacity());
        Person extraPerson = new Person("Extra") {};
        assertThrows(IllegalStateException.class, () -> {
            smallTaxi.boardPassenger(extraPerson);
        });
    }

    @Test
    void testBoardSamePassengerTwiceInTaxi() {
        taxi.boardPassenger(john);
        assertThrows(IllegalStateException.class, () -> {
            taxi.boardPassenger(john);
        });
    }

    @Test
    void testDisembarkFromEmptyTaxi() {
        assertThrows(IllegalArgumentException.class, () -> {
            taxi.disembarkPassenger(john);
        });
    }

    @Test
    void testDisembarkAllPassengersFromTaxi() {
        taxi.boardPassenger(john);
        taxi.boardPassenger(mike);
        taxi.boardPassenger(sarah);
        assertEquals(3, taxi.getOccupiedSeats());
        taxi.disembarkPassenger(john);
        taxi.disembarkPassenger(mike);
        taxi.disembarkPassenger(sarah);
        assertEquals(0, taxi.getOccupiedSeats());
        assertTrue(taxi.getPassengers().isEmpty());
    }

    @Test
    void testTaxiCapacityAfterDisembark() {
        Taxi<Person> smallTaxi = new Taxi<>(2);
        smallTaxi.boardPassenger(john);
        smallTaxi.boardPassenger(mike);
        assertEquals(2, smallTaxi.getOccupiedSeats());
        smallTaxi.disembarkPassenger(john);
        assertEquals(1, smallTaxi.getOccupiedSeats());
        smallTaxi.boardPassenger(sarah);
        assertEquals(2, smallTaxi.getOccupiedSeats());
    }

    @Test
    void testTaxiWithNullPassenger() {
        assertThrows(NullPointerException.class, () -> {
            taxi.boardPassenger(null);
        });
    }

    @Test
    void testDisembarkNullPassengerFromTaxi() {
        assertThrows(NullPointerException.class, () -> {
            taxi.disembarkPassenger(null);
        });
    }

    @Test
    void testTaxiGetPassengersReturnsCopy() {
        taxi.boardPassenger(john);
        taxi.boardPassenger(mike);
        List<Person> passengers1 = taxi.getPassengers();
        List<Person> passengers2 = taxi.getPassengers();
        assertEquals(passengers1.size(), passengers2.size());
        assertNotSame(passengers1, passengers2);
        passengers1.clear();
        assertEquals(2, taxi.getOccupiedSeats()); // Повинно залишитись 2
    }

    @Test
    void testTaxiMixedOperations() {
        taxi.boardPassenger(john);
        taxi.boardPassenger(mike);
        assertEquals(2, taxi.getOccupiedSeats());
        taxi.disembarkPassenger(john);
        assertEquals(1, taxi.getOccupiedSeats());
        taxi.boardPassenger(sarah);
        assertEquals(2, taxi.getOccupiedSeats());
        List<Person> passengers = taxi.getPassengers();
        assertTrue(passengers.contains(mike));
        assertTrue(passengers.contains(sarah));
        assertFalse(passengers.contains(john));
    }

    @Test
    void testTaxiToString() {
        taxi.boardPassenger(john);
        taxi.boardPassenger(mike);
        String result = taxi.toString();
        assertTrue(result.contains("Taxi"));
        assertTrue(result.contains("capacity=4"));
        assertTrue(result.contains("occupied=2"));
    }

    @Test
    void testTaxiGenericConstraints() {
        Taxi<Person> genericTaxi = new Taxi<>(4);
        genericTaxi.boardPassenger(new Person("Regular") {});
        genericTaxi.boardPassenger(new Firefighter("Firefighter"));
        genericTaxi.boardPassenger(new PoliceOfficer("Officer"));
        assertEquals(3, genericTaxi.getOccupiedSeats());
    }

    @Test
    void testTaxiWithSpecificPersonType() {
        Taxi<Firefighter> firefighterTaxi = new Taxi<>(3);
        Firefighter firefighter1 = new Firefighter("Mike");
        Firefighter firefighter2 = new Firefighter("Bob");
        firefighterTaxi.boardPassenger(firefighter1);
        firefighterTaxi.boardPassenger(firefighter2);
        assertEquals(2, firefighterTaxi.getOccupiedSeats());
    }

    @Test
    void testTaxiWithDifferentPersonInstancesSameName() {
        Person john1 = new Person("John") {};
        Person john2 = new Person("John") {};
        taxi.boardPassenger(john1);
        taxi.boardPassenger(john2);
        assertEquals(2, taxi.getOccupiedSeats());
    }

    @Test
    void testTaxiMultipleDisembarkSamePassenger() {
        taxi.boardPassenger(john);
        taxi.disembarkPassenger(john);
        assertThrows(IllegalArgumentException.class, () -> {
            taxi.disembarkPassenger(john);
        });
    }

    @Test
    void testTaxiEdgeCaseMaximumCapacity() {
        Taxi<Person> maxTaxi = new Taxi<>(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, maxTaxi.getMaxCapacity());
        assertEquals(0, maxTaxi.getOccupiedSeats());
        maxTaxi.boardPassenger(john);
        assertEquals(1, maxTaxi.getOccupiedSeats());
    }

    @Test
    void testTaxiPassengerListIntegrity() {
        taxi.boardPassenger(john);
        taxi.boardPassenger(mike);
        List<Person> initialPassengers = taxi.getPassengers();
        assertEquals(2, initialPassengers.size());
        taxi.disembarkPassenger(john);
        List<Person> afterDisembarkPassengers = taxi.getPassengers();
        assertEquals(1, afterDisembarkPassengers.size());
        assertTrue(afterDisembarkPassengers.contains(mike));
        assertFalse(afterDisembarkPassengers.contains(john));
        assertEquals(2, initialPassengers.size());
    }

    @Test
    void testTaxiTypicalUsageScenario() {
        Taxi<Person> cityTaxi = new Taxi<>(4);
        cityTaxi.boardPassenger(john);
        cityTaxi.boardPassenger(mike);
        assertEquals(2, cityTaxi.getOccupiedSeats());
        cityTaxi.disembarkPassenger(john);
        assertEquals(1, cityTaxi.getOccupiedSeats());
        cityTaxi.boardPassenger(sarah);
        assertEquals(2, cityTaxi.getOccupiedSeats());
        List<Person> finalPassengers = cityTaxi.getPassengers();
        assertTrue(finalPassengers.contains(mike));
        assertTrue(finalPassengers.contains(sarah));
        assertFalse(finalPassengers.contains(john));
    }

    @Test
    void testTaxiWithMixedPassengerTypes() {
        taxi.boardPassenger(john); // Regular Person
        taxi.boardPassenger(mike); // Firefighter
        taxi.boardPassenger(sarah); // PoliceOfficer
        assertEquals(3, taxi.getOccupiedSeats());
        List<Person> passengers = taxi.getPassengers();
        assertEquals(3, passengers.size());
        boolean hasRegularPerson = passengers.stream().anyMatch(p -> p instanceof Person && !(p instanceof Firefighter) && !(p instanceof PoliceOfficer));
        boolean hasFirefighter = passengers.stream().anyMatch(p -> p instanceof Firefighter);
        boolean hasPoliceOfficer = passengers.stream().anyMatch(p -> p instanceof PoliceOfficer);
        assertTrue(hasRegularPerson);
        assertTrue(hasFirefighter);
        assertTrue(hasPoliceOfficer);
    }
}