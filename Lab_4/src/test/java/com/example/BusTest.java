package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class BusTest {
    private Bus<Person> bus;
    private Person john;
    private Firefighter mike;
    private PoliceOfficer sarah;

    @BeforeEach
    void setUp() {
        bus = new Bus<>(30);
        john = new Person("John") {};
        mike = new Firefighter("Mike");
        sarah = new PoliceOfficer("Sarah");
    }

    @Test
    void testBusCanCarryAnyPersonType() {
        bus.boardPassenger(john);
        bus.boardPassenger(mike);
        bus.boardPassenger(sarah);
        assertEquals(3, bus.getOccupiedSeats());
        assertEquals(30, bus.getMaxCapacity());
    }

    @Test
    void testBusInheritance() {
        assertTrue(bus instanceof Vehicle);
        assertEquals(30, bus.getMaxCapacity());
    }

    @Test
    void testBusWithGenericType() {
        Bus<Firefighter> firefighterBus = new Bus<>(20);
        Firefighter firefighter = new Firefighter("Bob");
        firefighterBus.boardPassenger(firefighter);
        assertEquals(1, firefighterBus.getOccupiedSeats());
    }

    @Test
    void testBusFullCapacity() {
        Bus<Person> smallBus = new Bus<>(3);
        smallBus.boardPassenger(john);
        smallBus.boardPassenger(mike);
        smallBus.boardPassenger(sarah);
        assertEquals(3, smallBus.getOccupiedSeats());
        assertEquals(3, smallBus.getMaxCapacity());
        Person extraPerson = new Person("Extra") {};
        assertThrows(IllegalStateException.class, () -> {
            smallBus.boardPassenger(extraPerson);
        });
    }

    @Test
    void testBoardSamePassengerTwice() {
        bus.boardPassenger(john);
        assertThrows(IllegalStateException.class, () -> {
            bus.boardPassenger(john);
        });
    }

    @Test
    void testDisembarkFromEmptyBus() {
        assertThrows(IllegalArgumentException.class, () -> {
            bus.disembarkPassenger(john);
        });
    }

    @Test
    void testDisembarkAllPassengers() {
        bus.boardPassenger(john);
        bus.boardPassenger(mike);
        bus.boardPassenger(sarah);
        assertEquals(3, bus.getOccupiedSeats());
        bus.disembarkPassenger(john);
        bus.disembarkPassenger(mike);
        bus.disembarkPassenger(sarah);
        assertEquals(0, bus.getOccupiedSeats());
        assertTrue(bus.getPassengers().isEmpty());
    }

    @Test
    void testBusCapacityAfterDisembark() {
        Bus<Person> smallBus = new Bus<>(2);
        smallBus.boardPassenger(john);
        smallBus.boardPassenger(mike);
        assertEquals(2, smallBus.getOccupiedSeats());
        smallBus.disembarkPassenger(john);
        assertEquals(1, smallBus.getOccupiedSeats());
        smallBus.boardPassenger(sarah);
        assertEquals(2, smallBus.getOccupiedSeats());
    }

    @Test
    void testBusWithNullPassenger() {
        assertThrows(NullPointerException.class, () -> {
            bus.boardPassenger(null);
        });
    }

    @Test
    void testDisembarkNullPassenger() {
        assertThrows(NullPointerException.class, () -> {
            bus.disembarkPassenger(null);
        });
    }

    @Test
    void testBusMixedOperations() {
        bus.boardPassenger(john);
        bus.boardPassenger(mike);
        assertEquals(2, bus.getOccupiedSeats());
        bus.disembarkPassenger(john);
        assertEquals(1, bus.getOccupiedSeats());
        bus.boardPassenger(sarah);
        assertEquals(2, bus.getOccupiedSeats());
        List<Person> passengers = bus.getPassengers();
        assertTrue(passengers.contains(mike));
        assertTrue(passengers.contains(sarah));
        assertFalse(passengers.contains(john));
    }

    @Test
    void testBusToString() {
        bus.boardPassenger(john);
        bus.boardPassenger(mike);

        String result = bus.toString();
        assertTrue(result.contains("Bus"));
        assertTrue(result.contains("capacity=30"));
        assertTrue(result.contains("occupied=2"));
    }

    @Test
    void testBusGenericConstraints() {
        Bus<Person> genericBus = new Bus<>(10);
        genericBus.boardPassenger(new Person("Regular") {});
        genericBus.boardPassenger(new Firefighter("Firefighter"));
        genericBus.boardPassenger(new PoliceOfficer("Officer"));
        assertEquals(3, genericBus.getOccupiedSeats());
    }

    @Test
    void testBusWithDifferentPersonInstancesSameName() {
        Person john1 = new Person("John") {};
        Person john2 = new Person("John") {};
        bus.boardPassenger(john1);
        bus.boardPassenger(john2);
        assertEquals(2, bus.getOccupiedSeats());
    }

    @Test
    void testBusMultipleDisembarkSamePassenger() {
        bus.boardPassenger(john);
        bus.disembarkPassenger(john);
        assertThrows(IllegalArgumentException.class, () -> {
            bus.disembarkPassenger(john);
        });
    }
}