package com.example;

import java.util.ArrayList;
import java.util.List;

public class Vehicle<T extends Person> {
    private final int maxCapacity;
    private final List<T> passengers;

    public Vehicle(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        this.passengers = new ArrayList<>();
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public int getOccupiedSeats() {
        return passengers.size();
    }

    public void boardPassenger(T passenger) {
        if (passenger == null) {
            throw new NullPointerException("Passenger cannot be null");
        }
        if (passengers.contains(passenger)) {
            throw new IllegalStateException("Passenger " + passenger.getName() +
                    " is already in " + getClass().getSimpleName());
        }
        if (passengers.size() >= maxCapacity) {
            throw new IllegalStateException("No available seats in " + getClass().getSimpleName());
        }
        passengers.add(passenger);
    }

    public void disembarkPassenger(T passenger) {
        if (passenger == null) {
            throw new NullPointerException("Passenger cannot be null");
        }
        if (!passengers.contains(passenger)) {
            throw new IllegalArgumentException("Passenger " + passenger.getName() +
                    " is not in " + getClass().getSimpleName());
        }
        passengers.remove(passenger);
    }

    public List<T> getPassengers() {
        return new ArrayList<>(passengers);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{capacity=" + maxCapacity +
                ", occupied=" + getOccupiedSeats() + "}";
    }
}