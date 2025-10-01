package org.example;

import java.util.*;

public class Turnstile {
    private final Map<String, Card> issuedCards;
    private int totalPasses;
    private int totalDenials;
    private Map<String, Integer> passesByType;
    private Map<String, Integer> denialsByType;

    public Turnstile() {
        this.issuedCards = new HashMap<>();
        this.totalPasses = 0;
        this.totalDenials = 0;
        this.passesByType = new HashMap<>();
        this.denialsByType = new HashMap<>();
    }

    public void issueCard(Card card) {
        issuedCards.put(card.getId(), card);
        // Initialize counters for this card type if not present
        passesByType.putIfAbsent(card.getType(), 0);
        denialsByType.putIfAbsent(card.getType(), 0);
    }

    public boolean isCardIdExists(String id) {
        return issuedCards.containsKey(id);
    }

    public void validateCard(String cardId) {
        Card card = issuedCards.get(cardId);

        if (card == null) {
            System.out.println("Error: Card not found!");
            recordDenial("Unknown");
            return;
        }

        System.out.println("Checking card: " + card);

        // Show card details
        if (card instanceof AccumulatingCard) {
            System.out.printf("Balance: %.2f UAH\n", ((AccumulatingCard) card).getBalance());
        } else {
            System.out.println("Remaining trips: " + card.getTripsRemaining());
            System.out.println("Expiry date: " + card.getExpiryDate());
            System.out.println("Remaining validity: " + card.getRemainingValidityDays() + " days");
        }

        if (!card.isValid()) {
            System.out.println("Access DENIED!");
            if (card.isExpired()) {
                System.out.println("Reason: Card expired on " + card.getExpiryDate());
            } else if (!card.hasTrips()) {
                if (card instanceof AccumulatingCard) {
                    System.out.println("Reason: Insufficient balance");
                } else {
                    System.out.println("Reason: No trips remaining");
                }
            } else if (!card.isActive()) {
                System.out.println("Reason: Card deactivated");
            }
            recordDenial(card.getType());
            return;
        }

        boolean success = card.useTrip();
        if (success) {
            System.out.println("Access GRANTED!");
            totalPasses++;
            passesByType.put(card.getType(), passesByType.get(card.getType()) + 1);

            if (card instanceof AccumulatingCard) {
                System.out.printf("Remaining balance: %.2f UAH\n", ((AccumulatingCard) card).getBalance());
            } else {
                System.out.println("Remaining trips: " + card.getTripsRemaining());
            }
        } else {
            System.out.println("Access DENIED! Unknown error");
            recordDenial(card.getType());
        }
    }

    private void recordDenial(String type) {
        totalDenials++;
        denialsByType.put(type, denialsByType.getOrDefault(type, 0) + 1);
    }

    public void showStatistics() {
        System.out.println("\n=== General Statistics ===");
        System.out.println("Total passes granted: " + totalPasses);
        System.out.println("Total passes denied: " + totalDenials);
        System.out.println("Total attempts: " + (totalPasses + totalDenials));

        if (totalPasses + totalDenials > 0) {
            double successRate = (double) totalPasses / (totalPasses + totalDenials) * 100;
            System.out.printf("Success rate: %.2f%%\n", successRate);
        }
    }

    public void showStatisticsByType() {
        System.out.println("\n=== Statistics by Card Type ===");

        Set<String> allTypes = new HashSet<>();
        allTypes.addAll(passesByType.keySet());
        allTypes.addAll(denialsByType.keySet());

        for (String type : allTypes) {
            int passes = passesByType.getOrDefault(type, 0);
            int denials = denialsByType.getOrDefault(type, 0);
            int total = passes + denials;

            System.out.printf("\n%s Cards:\n", type);
            System.out.println("  Passes granted: " + passes);
            System.out.println("  Passes denied: " + denials);
            System.out.println("  Total attempts: " + total);

            if (total > 0) {
                double successRate = (double) passes / total * 100;
                System.out.printf("  Success rate: %.2f%%\n", successRate);
            }
        }
    }

    // Getters for testing
    public int getTotalPasses() { return totalPasses; }
    public int getTotalDenials() { return totalDenials; }
    public Map<String, Integer> getPassesByType() { return new HashMap<>(passesByType); }
    public Map<String, Integer> getDenialsByType() { return new HashMap<>(denialsByType); }
}