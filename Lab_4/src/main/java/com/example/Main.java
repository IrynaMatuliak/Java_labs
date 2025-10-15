package com.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== VEHICLE TRANSPORTATION SYSTEM DEMONSTRATION ===\n");

        Person john = new Person("John");
        Person alice = new Person("Alice");
        Firefighter mike = new Firefighter("Mike");
        Firefighter bob = new Firefighter("Bob");
        Firefighter emma = new Firefighter("Emma");
        PoliceOfficer sarah = new PoliceOfficer("Sarah");
        PoliceOfficer tom = new PoliceOfficer("Tom");
        PoliceOfficer david = new PoliceOfficer("David");

        Bus bus = new Bus(40);
        Taxi taxi = new Taxi(4);
        FireTruck fireTruck = new FireTruck(5);
        PoliceCar policeCar = new PoliceCar(3);

        // Demonstration 1: Basic boarding operations
        System.out.println("1. BASIC BOARDING OPERATIONS:");
        System.out.println("-----------------------------");

        // Bus - accepts all types
        bus.boardPassenger(john);
        bus.boardPassenger(alice);
        bus.boardPassenger(mike);
        bus.boardPassenger(sarah);
        System.out.println("Bus: " + bus.getOccupiedSeats() + "/" + bus.getMaxCapacity() + " seats occupied");

        // Taxi - accepts all types
        taxi.boardPassenger(bob);
        taxi.boardPassenger(tom);
        System.out.println("Taxi: " + taxi.getOccupiedSeats() + "/" + taxi.getMaxCapacity() + " seats occupied");

        // Fire Truck - only firefighters
        fireTruck.boardPassenger(mike);
        fireTruck.boardPassenger(bob);
        fireTruck.boardPassenger(emma);
        System.out.println("Fire Truck: " + fireTruck.getOccupiedSeats() + "/" + fireTruck.getMaxCapacity() + " seats occupied");

        // Police Car - only police officers
        policeCar.boardPassenger(sarah);
        policeCar.boardPassenger(tom);
        policeCar.boardPassenger(david);
        System.out.println("Police Car: " + policeCar.getOccupiedSeats() + "/" + policeCar.getMaxCapacity() + " seats occupied");

        // Demonstration 2: Road operations
        System.out.println("\n2. ROAD OPERATIONS:");
        System.out.println("-------------------");
        Road cityRoad = new Road();
        cityRoad.addCarToRoad(bus);
        cityRoad.addCarToRoad(taxi);
        cityRoad.addCarToRoad(fireTruck);
        cityRoad.addCarToRoad(policeCar);

        System.out.println("Total humans on road: " + cityRoad.getCountOfHumans());
        System.out.println("Total vehicles on road: " + cityRoad.carsOnRoad.size());

        // Demonstration 3: Disembarking operations
        System.out.println("\n3. DISEMBARKING OPERATIONS:");
        System.out.println("---------------------------");

        bus.disembarkPassenger(john);
        taxi.disembarkPassenger(bob);
        fireTruck.disembarkPassenger(emma);

        System.out.println("After disembarking:");
        System.out.println("Bus: " + bus.getOccupiedSeats() + " passengers");
        System.out.println("Taxi: " + taxi.getOccupiedSeats() + " passengers");
        System.out.println("Fire Truck: " + fireTruck.getOccupiedSeats() + " firefighters");
        System.out.println("Police Car: " + policeCar.getOccupiedSeats() + " officers");
        System.out.println("Total humans on road after disembarking: " + cityRoad.getCountOfHumans());

        // Demonstration 4: Dynamic operations
        System.out.println("\n4. DYNAMIC OPERATIONS:");
        System.out.println("----------------------");

        Person newPassenger = new Person("Robert");
        Firefighter newFirefighter = new Firefighter("Chris");
        PoliceOfficer newOfficer = new PoliceOfficer("Lisa");

        bus.boardPassenger(newPassenger);
        fireTruck.boardPassenger(newFirefighter);
        policeCar.disembarkPassenger(david);
        policeCar.boardPassenger(newOfficer);

        System.out.println("After dynamic changes:");
        System.out.println("Bus: " + bus.getOccupiedSeats() + " passengers");
        System.out.println("Fire Truck: " + fireTruck.getOccupiedSeats() + " firefighters");
        System.out.println("Police Car: " + policeCar.getOccupiedSeats() + " officers");
        System.out.println("Total humans on road: " + cityRoad.getCountOfHumans());

        // Demonstration 5: Capacity limits
        System.out.println("\n5. CAPACITY LIMITS DEMONSTRATION:");
        System.out.println("---------------------------------");

        Taxi smallTaxi = new Taxi(2);
        smallTaxi.boardPassenger(new Person("Passenger1"));
        smallTaxi.boardPassenger(new Person("Passenger2"));

        System.out.println("Small Taxi: " + smallTaxi.getOccupiedSeats() + "/" + smallTaxi.getMaxCapacity() + " seats");

        try {
            smallTaxi.boardPassenger(new Person("Passenger3"));
        } catch (IllegalStateException e) {
            System.out.println("Capacity limit enforced: " + e.getMessage());
        }

        // Demonstration 6: Type safety demonstration
        System.out.println("\n6. TYPE SAFETY DEMONSTRATION:");
        System.out.println("-----------------------------");

        FireTruck strictFireTruck = new FireTruck(3);
        PoliceCar strictPoliceCar = new PoliceCar(2);

        strictFireTruck.boardPassenger(new Firefighter("Firefighter"));
        strictPoliceCar.boardPassenger(new PoliceOfficer("Officer"));

        System.out.println("Fire Truck type safety: Only firefighters allowed ✓");
        System.out.println("Police Car type safety: Only police officers allowed ✓");

        // strictFireTruck.boardPassenger(new Person("Regular")); // Compilation error
        // strictPoliceCar.boardPassenger(new Firefighter("Firefighter")); // Compilation error

        // Demonstration 7: Complex scenario
        System.out.println("\n7. COMPLEX SCENARIO:");
        System.out.println("--------------------");

        Road highway = new Road();

        Bus expressBus = new Bus(50);
        Taxi[] taxis = { new Taxi(4), new Taxi(4), new Taxi(4) };
        FireTruck emergencyTruck = new FireTruck(6);
        PoliceCar[] patrolCars = { new PoliceCar(2), new PoliceCar(2) };

        for (int i = 0; i < 25; i++) {
            expressBus.boardPassenger(new Person("BusPassenger" + i));
        }

        taxis[0].boardPassenger(new Person("Taxi1Passenger1"));
        taxis[0].boardPassenger(new Person("Taxi1Passenger2"));
        taxis[1].boardPassenger(new Person("Taxi2Passenger1"));

        emergencyTruck.boardPassenger(new Firefighter("Chief"));
        emergencyTruck.boardPassenger(new Firefighter("Driver"));
        emergencyTruck.boardPassenger(new Firefighter("Operator"));

        patrolCars[0].boardPassenger(new PoliceOfficer("Patrol1Officer1"));
        patrolCars[0].boardPassenger(new PoliceOfficer("Patrol1Officer2"));
        patrolCars[1].boardPassenger(new PoliceOfficer("Patrol2Officer1"));

        highway.addCarToRoad(expressBus);
        for (Taxi t : taxis) {
            highway.addCarToRoad(t);
        }
        highway.addCarToRoad(emergencyTruck);
        for (PoliceCar pc : patrolCars) {
            highway.addCarToRoad(pc);
        }
        System.out.println("Highway Statistics:");
        System.out.println("- Total vehicles: " + highway.carsOnRoad.size());
        System.out.println("- Total humans: " + highway.getCountOfHumans());
        System.out.println("- Breakdown:");
        System.out.println("  * Bus: " + expressBus.getOccupiedSeats() + " passengers");
        System.out.println("  * Taxis: " + (taxis[0].getOccupiedSeats() + taxis[1].getOccupiedSeats() + taxis[2].getOccupiedSeats()) + " passengers");
        System.out.println("  * Fire Truck: " + emergencyTruck.getOccupiedSeats() + " firefighters");
        System.out.println("  * Police Cars: " + (patrolCars[0].getOccupiedSeats() + patrolCars[1].getOccupiedSeats()) + " officers");

        // Demonstration 8: Error handling
        System.out.println("\n8. ERROR HANDLING DEMONSTRATION:");
        System.out.println("--------------------------------");
        try {
            bus.disembarkPassenger(new Person("NonExistent"));
        } catch (IllegalArgumentException e) {
            System.out.println("Error handled: " + e.getMessage());
        }
        try {
            highway.addCarToRoad(null);
        } catch (NullPointerException e) {
            System.out.println("Null safety: " + e.getMessage());
        }
    }
}