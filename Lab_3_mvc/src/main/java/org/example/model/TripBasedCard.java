package org.example.model;

import java.time.LocalDate;

public class TripBasedCard extends Card {

    public TripBasedCard(String id, String type, LocalDate expiryDate, int trips) {
        super(id, type, expiryDate, trips);
    }

    @Override
    public boolean isValid() {
        return isActive && !isExpired() && hasTrips();
    }

    @Override
    public String toString() {
        return String.format("TripBasedCard{id='%s', type='%s', expiryDate=%s, tripsRemaining=%d, isActive=%s}",
                id, type, expiryDate, tripsRemaining, isActive);
    }
}