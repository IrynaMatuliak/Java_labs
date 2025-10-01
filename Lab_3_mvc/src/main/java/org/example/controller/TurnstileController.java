package org.example.controller;

import org.example.model.*;
import java.time.LocalDate;
import java.util.*;

public class TurnstileController {
    private final Map<String, Card> issuedCards;
    private int totalPasses;
    private int totalDenials;
    private Map<String, Integer> passesByType;
    private Map<String, Integer> denialsByType;

    public TurnstileController() {
        this.issuedCards = new HashMap<>();
        this.totalPasses = 0;
        this.totalDenials = 0;
        this.passesByType = new HashMap<>();
        this.denialsByType = new HashMap<>();
    }

    // Card management methods
    public boolean issueTripBasedCard(String id, String type, int validityDays, int trips) {
        if (issuedCards.containsKey(id)) {
            return false;
        }

        LocalDate expiryDate = LocalDate.now().plusDays(validityDays);
        Card card = new TripBasedCard(id, type, expiryDate, trips);
        issuedCards.put(id, card);
        initializeTypeCounters(type);
        return true;
    }

    public boolean issueAccumulatingCard(String id, double initialBalance) {
        if (issuedCards.containsKey(id)) {
            return false;
        }

        Card card = new AccumulatingCard(id, initialBalance);
        issuedCards.put(id, card);
        initializeTypeCounters("Regular");
        return true;
    }

    private void initializeTypeCounters(String type) {
        passesByType.putIfAbsent(type, 0);
        denialsByType.putIfAbsent(type, 0);
    }

    public boolean isCardIdExists(String id) {
        return issuedCards.containsKey(id);
    }

    // Card validation logic
    public ValidationResult validateCard(String cardId) {
        Card card = issuedCards.get(cardId);

        if (card == null) {
            recordDenial("Unknown");
            return new ValidationResult(false, "Card not found", null);
        }

        if (!card.isValid()) {
            String reason = getInvalidationReason(card);
            recordDenial(card.getType());
            return new ValidationResult(false, reason, card);
        }

        boolean success = card.useTrip();
        if (success) {
            recordPass(card.getType());
            String details = getCardDetails(card);
            return new ValidationResult(true, "Access granted", card, details);
        } else {
            recordDenial(card.getType());
            return new ValidationResult(false, "Unknown error", card);
        }
    }

    private String getInvalidationReason(Card card) {
        if (card.isExpired()) {
            return "Card expired on " + card.getExpiryDate();
        } else if (!card.hasTrips()) {
            if (card instanceof AccumulatingCard) {
                return "Insufficient balance";
            } else {
                return "No trips remaining";
            }
        } else if (!card.isActive()) {
            return "Card deactivated";
        }
        return "Unknown reason";
    }

    private String getCardDetails(Card card) {
        if (card instanceof AccumulatingCard) {
            return String.format("Remaining balance: %.2f UAH", ((AccumulatingCard) card).getBalance());
        } else {
            return String.format("Remaining trips: %d, Remaining validity: %d days",
                    card.getTripsRemaining(), card.getRemainingValidityDays());
        }
    }

    private void recordPass(String type) {
        totalPasses++;
        passesByType.put(type, passesByType.get(type) + 1);
    }

    private void recordDenial(String type) {
        totalDenials++;
        denialsByType.put(type, denialsByType.getOrDefault(type, 0) + 1);
    }

    // Statistics methods
    public Statistics getGeneralStatistics() {
        int totalAttempts = totalPasses + totalDenials;
        double successRate = totalAttempts > 0 ? (double) totalPasses / totalAttempts * 100 : 0;

        return new Statistics(totalPasses, totalDenials, totalAttempts, successRate);
    }

    public Map<String, TypeStatistics> getStatisticsByType() {
        Map<String, TypeStatistics> statistics = new HashMap<>();

        Set<String> allTypes = new HashSet<>();
        allTypes.addAll(passesByType.keySet());
        allTypes.addAll(denialsByType.keySet());

        for (String type : allTypes) {
            int passes = passesByType.getOrDefault(type, 0);
            int denials = denialsByType.getOrDefault(type, 0);
            int total = passes + denials;
            double successRate = total > 0 ? (double) passes / total * 100 : 0;

            statistics.put(type, new TypeStatistics(type, passes, denials, total, successRate));
        }

        return statistics;
    }

    // Data transfer objects for communication with View
    public static class ValidationResult {
        private final boolean success;
        private final String message;
        private final Card card;
        private final String details;

        public ValidationResult(boolean success, String message, Card card) {
            this(success, message, card, null);
        }

        public ValidationResult(boolean success, String message, Card card, String details) {
            this.success = success;
            this.message = message;
            this.card = card;
            this.details = details;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public Card getCard() { return card; }
        public String getDetails() { return details; }
    }

    public static class Statistics {
        private final int totalPasses;
        private final int totalDenials;
        private final int totalAttempts;
        private final double successRate;

        public Statistics(int totalPasses, int totalDenials, int totalAttempts, double successRate) {
            this.totalPasses = totalPasses;
            this.totalDenials = totalDenials;
            this.totalAttempts = totalAttempts;
            this.successRate = successRate;
        }

        public int getTotalPasses() { return totalPasses; }
        public int getTotalDenials() { return totalDenials; }
        public int getTotalAttempts() { return totalAttempts; }
        public double getSuccessRate() { return successRate; }
    }

    public static class TypeStatistics {
        private final String type;
        private final int passes;
        private final int denials;
        private final int totalAttempts;
        private final double successRate;

        public TypeStatistics(String type, int passes, int denials, int totalAttempts, double successRate) {
            this.type = type;
            this.passes = passes;
            this.denials = denials;
            this.totalAttempts = totalAttempts;
            this.successRate = successRate;
        }

        public String getType() { return type; }
        public int getPasses() { return passes; }
        public int getDenials() { return denials; }
        public int getTotalAttempts() { return totalAttempts; }
        public double getSuccessRate() { return successRate; }
    }
}