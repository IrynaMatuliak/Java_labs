package com.example;

import java.util.ArrayList;
import java.util.List;

public class Road {
    public List<Vehicle<? extends Person>> carsOnRoad = new ArrayList<>();

    public int getCountOfHumans() {
        int total = 0;
        for (Vehicle<? extends Person> vehicle : carsOnRoad) {
            total += vehicle.getOccupiedSeats();
        }
        return total;
    }

    public void addCarToRoad(Vehicle<? extends Person> vehicle) {
        if (vehicle == null) {
            throw new NullPointerException("Vehicle cannot be null");
        }
        carsOnRoad.add(vehicle);
    }

    public List<Vehicle<? extends Person>> getCarsOnRoad() {
        return new ArrayList<>(carsOnRoad);
    }
}