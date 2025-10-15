package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class VehicleTest {
    private Person john;
    private Firefighter mike;
    private PoliceOfficer arthur;

    @BeforeEach
    void setUp() {
        john = new Person("John") {};
        mike = new Firefighter("Mike");
        arthur = new PoliceOfficer("Arthur");
    }

    @Test
    void testVehicleCreation() {
        Vehicle<Person> vehicle = new Vehicle<>(10) {};
        assertEquals(10, vehicle.getMaxCapacity());
        assertEquals(0, vehicle.getOccupiedSeats());
    }

    @Test
    void testBoardPassenger() {
        Vehicle<Person> vehicle = new Vehicle<>(3) {};
        vehicle.boardPassenger(john);
        vehicle.boardPassenger(mike);
        assertEquals(2, vehicle.getOccupiedSeats());
        assertEquals(3, vehicle.getMaxCapacity());
    }

    @Test
    void testBoardPassengerExceedsCapacity() {
        Vehicle<Person> vehicle = new Vehicle<>(1) {};
        vehicle.boardPassenger(john);
        assertThrows(IllegalStateException.class, () -> {
            vehicle.boardPassenger(mike);
        });
    }

    @Test
    void testDisembarkPassenger() {
        Vehicle<Person> vehicle = new Vehicle<>(3) {};
        vehicle.boardPassenger(john);
        vehicle.boardPassenger(mike);
        vehicle.disembarkPassenger(john);
        assertEquals(1, vehicle.getOccupiedSeats());
        assertTrue(vehicle.getPassengers().contains(mike));
        assertFalse(vehicle.getPassengers().contains(john));
    }

    @Test
    void testDisembarkNonExistentPassenger() {
        Vehicle<Person> vehicle = new Vehicle<>(3) {};
        vehicle.boardPassenger(john);
        assertThrows(IllegalArgumentException.class, () -> {
            vehicle.disembarkPassenger(mike);
        });
    }

    @Test
    void testGetPassengersReturnsCopy() {
        Vehicle<Person> vehicle = new Vehicle<>(3) {};
        vehicle.boardPassenger(john);
        List<Person> passengers1 = vehicle.getPassengers();
        List<Person> passengers2 = vehicle.getPassengers();
        assertEquals(passengers1.size(), passengers2.size());
        assertNotSame(passengers1, passengers2);
    }

    @Test
    void testToString() {
        Vehicle<Person> vehicle = new Vehicle<>(5) {};
        vehicle.boardPassenger(john);
        String result = vehicle.toString();
        assertTrue(result.contains("capacity=5"));
        assertTrue(result.contains("occupied=1"));
    }

    @Test
    void testToStringAfterDisembarkPassenger() {
        Vehicle<Person> vehicle = new Vehicle<>(5) {};
        vehicle.boardPassenger(john);
        vehicle.boardPassenger(mike);
        vehicle.boardPassenger(arthur);
        String resultAfterBoarding = vehicle.toString();
        assertTrue(resultAfterBoarding.contains("capacity=5"));
        assertTrue(resultAfterBoarding.contains("occupied=3"));
        vehicle.disembarkPassenger(mike);
        String resultAfterDisembark = vehicle.toString();
        assertTrue(resultAfterDisembark.contains("capacity=5"));
        assertTrue(resultAfterDisembark.contains("occupied=2"));
        assertEquals(2, vehicle.getOccupiedSeats());
    }
}
