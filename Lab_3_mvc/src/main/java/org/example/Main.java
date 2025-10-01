package org.example;

import org.example.controller.TurnstileController;
import org.example.view.TurnstileView;

public class Main {
    private final TurnstileController controller;
    private final TurnstileView view;

    public Main() {
        this.controller = new TurnstileController();
        this.view = new TurnstileView();
    }

    public void run() {
        view.showMessage("Welcome to Tram Turnstile System!");

        while (true) {
            view.showMenu();
            String choice = view.getInput("Choose option: ");

            switch (choice) {
                case "1":
                    handleIssueCard();
                    break;
                case "2":
                    handleCheckCard();
                    break;
                case "3":
                    handleShowStatistics();
                    break;
                case "4":
                    handleShowStatisticsByType();
                    break;
                case "5":
                    view.showMessage("Exiting program. Goodbye!");
                    view.close();
                    return;
                default:
                    view.showError("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }

    private void handleIssueCard() {
        String type = view.getCardTypeInput();
        String id = getUniqueCardId();

        if (type.equalsIgnoreCase("Regular")) {
            handleIssueRegularCard(id);
        } else {
            handleIssueTripBasedCard(id, type);
        }
    }

    private String getUniqueCardId() {
        while (true) {
            String id = view.getInput("Enter unique card ID: ");
            if (!controller.isCardIdExists(id)) {
                return id;
            }
            view.showError("This ID is already in use. Please try another one.");
        }
    }

    private void handleIssueRegularCard(String id) {
        String isAccumulating = view.getYesNoInput("Is this an accumulating card?");

        if (isAccumulating.equalsIgnoreCase("y")) {
            double balance = view.getBalanceInput();
            boolean success = controller.issueAccumulatingCard(id, balance);
            if (success) {
                view.showMessage("Accumulating card issued successfully!");
            } else {
                view.showError("Failed to issue accumulating card.");
            }
        } else {
            handleIssueTripBasedCard(id, "Regular");
        }
    }

    private void handleIssueTripBasedCard(String id, String type) {
        int trips = view.getTripCountInput();
        int validityDays = view.getValidityPeriodInput();

        boolean success = controller.issueTripBasedCard(id, type, validityDays, trips);
        if (success) {
            view.showMessage("Card issued successfully!");
        } else {
            view.showError("Failed to issue card.");
        }
    }

    private void handleCheckCard() {
        String id = view.getInput("Enter card ID: ");
        TurnstileController.ValidationResult result = controller.validateCard(id);
        view.showValidationResult(result);
    }

    private void handleShowStatistics() {
        TurnstileController.Statistics stats = controller.getGeneralStatistics();
        view.showStatistics(stats);
    }

    private void handleShowStatisticsByType() {
        var statsByType = controller.getStatisticsByType();
        view.showStatisticsByType(statsByType);
    }

    public static void main(String[] args) {
        new Main().run();
    }
}