package org.example.model;

public class AccumulatingCard extends Card {
    private double balance;
    private static final double TRIP_COST = 8.0;

    public AccumulatingCard(String id, double initialBalance) {
        super(id, "Regular", null, 0);
        this.balance = initialBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void topUp(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    @Override
    public boolean isValid() {
        return isActive && balance >= TRIP_COST;
    }

    @Override
    public boolean hasTrips() {
        return balance >= TRIP_COST;
    }

    @Override
    public boolean useTrip() {
        if (!isValid()) return false;
        balance -= TRIP_COST;
        return true;
    }

    @Override
    public boolean isExpired() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("AccumulatingCard{id='%s', balance=%.2f, isActive=%s}",
                id, balance, isActive);
    }
}