package org.example.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Card {
    protected final String id;
    protected final String type;
    protected LocalDate expiryDate;
    protected int tripsRemaining;
    protected boolean isActive;
    protected final LocalDate issueDate;

    public Card(String id, String type, LocalDate expiryDate, int trips) {
        this.id = id;
        this.type = type;
        this.expiryDate = expiryDate;
        this.tripsRemaining = trips;
        this.isActive = true;
        this.issueDate = LocalDate.now();
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public int getTripsRemaining() { return tripsRemaining; }
    public boolean isActive() { return isActive; }
    public LocalDate getIssueDate() { return issueDate; }

    public boolean isValid() {
        return isActive && !isExpired() && hasTrips();
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public boolean hasTrips() {
        return tripsRemaining > 0;
    }

    public boolean useTrip() {
        if (!isValid()) return false;
        tripsRemaining--;
        return true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    public long getRemainingValidityDays() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    @Override
    public String toString() {
        return String.format("Card{id='%s', type='%s', expiryDate=%s, tripsRemaining=%d, isActive=%s}",
                id, type, expiryDate, tripsRemaining, isActive);
    }
}