package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class RoadTest {
    private Road road;
    private Bus<Person> bus;
    private Taxi<Person> taxi;
    private FireTruck fireTruck;
    private PoliceCar policeCar;
    private Person john;
    private Firefighter mike;
    private PoliceOfficer sarah;

    @BeforeEach
    void setUp() {
        road = new Road();
        bus = new Bus<>(30);
        taxi = new Taxi<>(4);
        fireTruck = new FireTruck(6);
        policeCar = new PoliceCar(2);

        john = new Person("John") {};
        mike = new Firefighter("Mike");
        sarah = new PoliceOfficer("Sarah");
    }

    @Test
    void testRoadCreation() {
        assertNotNull(road.carsOnRoad);
        assertTrue(road.carsOnRoad.isEmpty());
    }

    @Test
    void testAddCarToRoad() {
        road.addCarToRoad(bus);
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);
        assertEquals(4, road.carsOnRoad.size());
        assertEquals(4, road.getCarsOnRoad().size());
    }

    @Test
    void testGetCountOfHumansEmpty() {
        assertEquals(0, road.getCountOfHumans());
    }

    @Test
    void testGetCountOfHumansWithPassengers() {
        bus.boardPassenger(john);
        bus.boardPassenger(mike);
        bus.boardPassenger(sarah);
        taxi.boardPassenger(john);
        fireTruck.boardPassenger(mike);
        fireTruck.boardPassenger(new Firefighter("Bob"));
        policeCar.boardPassenger(sarah);
        road.addCarToRoad(bus);
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);
        assertEquals(7, road.getCountOfHumans());
    }

    @Test
    void testGetCountOfHumansAfterDisembark() {
        bus.boardPassenger(john);
        bus.boardPassenger(mike);
        road.addCarToRoad(bus);
        assertEquals(2, road.getCountOfHumans());
        bus.disembarkPassenger(john);
        assertEquals(1, road.getCountOfHumans());
    }

    @Test
    void testGetCarsOnRoadReturnsCopy() {
        road.addCarToRoad(bus);
        List<Vehicle<? extends Person>> list1 = road.getCarsOnRoad();
        List<Vehicle<? extends Person>> list2 = road.getCarsOnRoad();
        assertEquals(list1.size(), list2.size());
        assertNotSame(list1, list2);
    }

    @Test
    void testWildcardAcceptance() {
        // Test that Road accepts all vehicle types with wildcard
        road.addCarToRoad(bus);        // Vehicle<Person>
        road.addCarToRoad(taxi);       // Vehicle<Person>
        road.addCarToRoad(fireTruck);  // Vehicle<Firefighter>
        road.addCarToRoad(policeCar);  // Vehicle<PoliceOfficer>
        assertEquals(4, road.carsOnRoad.size());
    }

    @Test
    void testAddNullVehicleToRoad() {
        assertThrows(NullPointerException.class, () -> {
            road.addCarToRoad(null);
        });
    }

    @Test
    void testRoadWithMixedEmptyAndFullVehicles() {
        bus.boardPassenger(john);
        bus.boardPassenger(mike);
        fireTruck.boardPassenger(new Firefighter("Bob"));
        road.addCarToRoad(bus);
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);
        assertEquals(3, road.getCountOfHumans());
    }

    @Test
    void testDynamicPassengerChangesOnRoad() {
        road.addCarToRoad(bus);
        road.addCarToRoad(taxi);
        assertEquals(0, road.getCountOfHumans());
        bus.boardPassenger(john);
        taxi.boardPassenger(mike);
        assertEquals(2, road.getCountOfHumans());
        bus.disembarkPassenger(john);
        assertEquals(1, road.getCountOfHumans());
        bus.boardPassenger(sarah);
        taxi.boardPassenger(new Person("Alice") {});
        assertEquals(3, road.getCountOfHumans());
    }

    @Test
    void testRoadWithMaximumCapacityVehicles() {
        Bus<Person> fullBus = new Bus<>(3);
        fullBus.boardPassenger(john);
        fullBus.boardPassenger(mike);
        fullBus.boardPassenger(sarah);
        Taxi<Person> fullTaxi = new Taxi<>(2);
        fullTaxi.boardPassenger(new Person("Alice") {});
        fullTaxi.boardPassenger(new Person("Bob") {});
        road.addCarToRoad(fullBus);
        road.addCarToRoad(fullTaxi);
        assertEquals(5, road.getCountOfHumans());
    }

    @Test
    void testRemoveVehiclesFromRoadImplicitly() {
        road.addCarToRoad(bus);
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        assertEquals(3, road.carsOnRoad.size());
        bus.boardPassenger(john);
        taxi.boardPassenger(mike);
        fireTruck.boardPassenger(new Firefighter("Bob"));
        road.carsOnRoad.clear();
        assertEquals(0, road.carsOnRoad.size());
        assertEquals(0, road.getCountOfHumans());
    }

    @Test
    void testRoadWithSpecializedVehiclesOnly() {
        FireTruck fireTruck2 = new FireTruck(4);
        PoliceCar policeCar2 = new PoliceCar(3);
        fireTruck.boardPassenger(mike);
        fireTruck.boardPassenger(new Firefighter("Bob"));
        policeCar.boardPassenger(sarah);
        policeCar2.boardPassenger(new PoliceOfficer("Tom"));
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(fireTruck2);
        road.addCarToRoad(policeCar);
        road.addCarToRoad(policeCar2);
        assertEquals(4, road.getCountOfHumans());
    }

    @Test
    void testRoadWithGenericVehiclesOnly() {
        Bus<Person> bus2 = new Bus<>(25);
        Taxi<Person> taxi2 = new Taxi<>(3);
        bus.boardPassenger(john);
        bus.boardPassenger(mike);
        taxi.boardPassenger(sarah);
        taxi2.boardPassenger(new Person("Alice") {});
        road.addCarToRoad(bus);
        road.addCarToRoad(bus2);
        road.addCarToRoad(taxi);
        road.addCarToRoad(taxi2);
        assertEquals(4, road.getCountOfHumans());
    }

    @Test
    void testLargeScaleRoadScenario() {
        for (int i = 0; i < 5; i++) {
            Bus<Person> cityBus = new Bus<>(40);
            cityBus.boardPassenger(new Person("Passenger" + i) {});
            road.addCarToRoad(cityBus);
        }
        for (int i = 0; i < 3; i++) {
            Taxi<Person> cityTaxi = new Taxi<>(4);
            cityTaxi.boardPassenger(new Person("TaxiPassenger" + i) {});
            road.addCarToRoad(cityTaxi);
        }
        for (int i = 0; i < 2; i++) {
            FireTruck truck = new FireTruck(5);
            truck.boardPassenger(new Firefighter("Firefighter" + i));
            road.addCarToRoad(truck);
        }
        for (int i = 0; i < 2; i++) {
            PoliceCar car = new PoliceCar(2);
            car.boardPassenger(new PoliceOfficer("Officer" + i));
            road.addCarToRoad(car);
        }
        // 5 buses with 1 passenger each = 5
        // 3 taxis with 1 passenger each = 3
        // 2 fire engines with 1 firefighter each = 2
        // 2 police cars with 1 officer each = 2
        // Total: 5 + 3 + 2 + 2 = 12
        assertEquals(12, road.getCountOfHumans());
        assertEquals(12, road.carsOnRoad.size());
    }

    @Test
    void testRoadEmptyAfterAllVehiclesLeave() {
        road.addCarToRoad(bus);
        road.addCarToRoad(taxi);
        bus.boardPassenger(john);
        taxi.boardPassenger(mike);
        assertEquals(2, road.getCountOfHumans());
        assertEquals(2, road.carsOnRoad.size());
        road.carsOnRoad.clear();
        assertEquals(0, road.getCountOfHumans());
        assertEquals(0, road.carsOnRoad.size());
    }

    @Test
    void testRoadStatistics() {
        road.addCarToRoad(bus);
        road.addCarToRoad(taxi);
        road.addCarToRoad(fireTruck);
        road.addCarToRoad(policeCar);

        // Total number of vehicles
        assertEquals(4, road.carsOnRoad.size());

        // Total capacity of all vehicles
        int totalCapacity = road.carsOnRoad.stream()
                .mapToInt(Vehicle::getMaxCapacity)
                .sum();
        assertEquals(30 + 4 + 6 + 2, totalCapacity);

        // Add passengers and check occupancy
        bus.boardPassenger(john);
        taxi.boardPassenger(mike);
        fireTruck.boardPassenger(new Firefighter("Bob"));
        policeCar.boardPassenger(sarah);

        int totalOccupied = road.getCountOfHumans();
        assertEquals(4, totalOccupied);

        // Percentage of occupancy
        double occupancyRate = (double) totalOccupied / totalCapacity * 100;
        assertTrue(occupancyRate > 0 && occupancyRate <= 100);
    }
}