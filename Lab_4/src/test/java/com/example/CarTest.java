package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class CarTest {
    private Car<Person> car;
    private Person john;
    private Firefighter mike;
    private PoliceOfficer sarah;

    @BeforeEach
    void setUp() {
        car = new Car<>(5) {};
        john = new Person("John") {};
        mike = new Firefighter("Mike");
        sarah = new PoliceOfficer("Sarah");
    }

    @Test
    void testCarCreation() {
        assertEquals(5, car.getMaxCapacity());
        assertEquals(0, car.getOccupiedSeats());
    }

    @Test
    void testCarInheritance() {
        assertTrue(car instanceof Vehicle);
    }

    @Test
    void testCarCanCarryPersons() {
        car.boardPassenger(john);
        car.boardPassenger(mike);
        assertEquals(2, car.getOccupiedSeats());
    }

    @Test
    void testCarFullCapacity() {
        Car<Person> smallCar = new Car<>(2) {};
        smallCar.boardPassenger(john);
        smallCar.boardPassenger(mike);
        assertEquals(2, smallCar.getOccupiedSeats());
        assertEquals(2, smallCar.getMaxCapacity());
        Person extraPerson = new Person("Extra") {};
        assertThrows(IllegalStateException.class, () -> {
            smallCar.boardPassenger(extraPerson);
        });
    }

    @Test
    void testBoardSamePassengerTwice() {
        car.boardPassenger(john);
        assertThrows(IllegalStateException.class, () -> {
            car.boardPassenger(john);
        });
    }

    @Test
    void testDisembarkFromEmptyCar() {
        assertThrows(IllegalArgumentException.class, () -> {
            car.disembarkPassenger(john);
        });
    }

    @Test
    void testDisembarkAllPassengers() {
        car.boardPassenger(john);
        car.boardPassenger(mike);
        car.boardPassenger(sarah);
        assertEquals(3, car.getOccupiedSeats());
        car.disembarkPassenger(john);
        car.disembarkPassenger(mike);
        car.disembarkPassenger(sarah);
        assertEquals(0, car.getOccupiedSeats());
        assertTrue(car.getPassengers().isEmpty());
    }

    @Test
    void testCarCapacityAfterDisembark() {
        Car<Person> smallCar = new Car<>(2) {};
        smallCar.boardPassenger(john);
        smallCar.boardPassenger(mike);
        assertEquals(2, smallCar.getOccupiedSeats());
        smallCar.disembarkPassenger(john);
        assertEquals(1, smallCar.getOccupiedSeats());
        smallCar.boardPassenger(sarah);
        assertEquals(2, smallCar.getOccupiedSeats());
    }

    @Test
    void testCarWithNullPassenger() {
        assertThrows(NullPointerException.class, () -> {
            car.boardPassenger(null);
        });
    }

    @Test
    void testDisembarkNullPassenger() {
        assertThrows(NullPointerException.class, () -> {
            car.disembarkPassenger(null);
        });
    }

    @Test
    void testCarGetPassengersReturnsCopy() {
        car.boardPassenger(john);
        car.boardPassenger(mike);
        List<Person> passengers1 = car.getPassengers();
        List<Person> passengers2 = car.getPassengers();
        assertEquals(passengers1.size(), passengers2.size());
        assertNotSame(passengers1, passengers2);
        passengers1.clear();
        assertEquals(2, car.getOccupiedSeats());
    }

    @Test
    void testCarMixedOperations() {
        car.boardPassenger(john);
        car.boardPassenger(mike);
        assertEquals(2, car.getOccupiedSeats());
        car.disembarkPassenger(john);
        assertEquals(1, car.getOccupiedSeats());
        car.boardPassenger(sarah);
        assertEquals(2, car.getOccupiedSeats());
        List<Person> passengers = car.getPassengers();
        assertTrue(passengers.contains(mike));
        assertTrue(passengers.contains(sarah));
        assertFalse(passengers.contains(john));
    }

    @Test
    void testCarToString() {
        car.boardPassenger(john);
        car.boardPassenger(mike);
        String result = car.toString();
        assertTrue(result.contains("capacity=5"));
        assertTrue(result.contains("occupied=2"));
    }

    @Test
    void testCarGenericConstraints() {
        Car<Person> genericCar = new Car<>(4) {};
        genericCar.boardPassenger(new Person("Regular") {});
        genericCar.boardPassenger(new Firefighter("Firefighter"));
        genericCar.boardPassenger(new PoliceOfficer("Officer"));

        assertEquals(3, genericCar.getOccupiedSeats());
    }

    @Test
    void testCarWithSpecificPersonType() {
        Car<Firefighter> firefighterCar = new Car<>(3) {};
        Firefighter firefighter1 = new Firefighter("Mike");
        Firefighter firefighter2 = new Firefighter("Bob");
        firefighterCar.boardPassenger(firefighter1);
        firefighterCar.boardPassenger(firefighter2);
        assertEquals(2, firefighterCar.getOccupiedSeats());
    }

    @Test
    void testCarAsBaseForSpecializedVehicles() {
        assertTrue(new Taxi<>(4) instanceof Car);
        assertTrue(new FireTruck(5) instanceof Car);
        assertTrue(new PoliceCar(3) instanceof Car);
    }

    @Test
    void testCarWithDifferentPersonInstancesSameName() {
        Person john1 = new Person("John") {};
        Person john2 = new Person("John") {};
        car.boardPassenger(john1);
        car.boardPassenger(john2);
        assertEquals(2, car.getOccupiedSeats());
    }

    @Test
    void testCarMultipleDisembarkSamePassenger() {
        car.boardPassenger(john);
        car.disembarkPassenger(john);
        assertThrows(IllegalArgumentException.class, () -> {
            car.disembarkPassenger(john);
        });
    }

    @Test
    void testCarPassengerListIntegrity() {
        car.boardPassenger(john);
        car.boardPassenger(mike);
        List<Person> initialPassengers = car.getPassengers();
        assertEquals(2, initialPassengers.size());
        car.disembarkPassenger(john);
        List<Person> afterDisembarkPassengers = car.getPassengers();
        assertEquals(1, afterDisembarkPassengers.size());
        assertTrue(afterDisembarkPassengers.contains(mike));
        assertFalse(afterDisembarkPassengers.contains(john));
        assertEquals(2, initialPassengers.size());
    }
}