package org.example.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Map;

public class TurnstileControllerTest {
    private TurnstileController controller;

    @BeforeEach
    public void setUp() {
        controller = new TurnstileController();
    }

    @Test
    public void testIssueTripBasedCard() {
        boolean result = controller.issueTripBasedCard("TEST001", "Student", 30, 10);
        assertTrue(result);
        assertTrue(controller.isCardIdExists("TEST001"));
    }

    @Test
    public void testIssueDuplicateCard() {
        controller.issueTripBasedCard("TEST002", "Regular", 10, 5);
        boolean result = controller.issueTripBasedCard("TEST002", "Student", 30, 10);
        assertFalse(result);
    }

    @Test
    public void testIssueAccumulatingCard() {
        boolean result = controller.issueAccumulatingCard("ACC001", 100.0);
        assertTrue(result);
        assertTrue(controller.isCardIdExists("ACC001"));
    }

    @Test
    public void testIssueDuplicateAccumulatingCard() {
        controller.issueAccumulatingCard("ACC002", 50.0);
        boolean result = controller.issueAccumulatingCard("ACC002", 100.0);
        assertFalse(result);
    }

    @Test
    public void testValidateCardSuccess() {
        controller.issueTripBasedCard("TEST003", "Regular", 30, 5);
        TurnstileController.ValidationResult result = controller.validateCard("TEST003");

        assertTrue(result.isSuccess());
        assertEquals("Access granted", result.getMessage());
        assertNotNull(result.getCard());
        assertNotNull(result.getDetails());
    }

    @Test
    public void testValidateCardNotFound() {
        TurnstileController.ValidationResult result = controller.validateCard("NONEXISTENT");

        assertFalse(result.isSuccess());
        assertEquals("Card not found", result.getMessage());
        assertNull(result.getCard());
    }

    @Test
    public void testValidateCardNoTrips() {
        controller.issueTripBasedCard("TEST004", "Student", 30, 0);
        TurnstileController.ValidationResult result = controller.validateCard("TEST004");

        assertFalse(result.isSuccess());
        assertEquals("No trips remaining", result.getMessage());
        assertNotNull(result.getCard());
    }

    @Test
    public void testValidateCardExpired() {
        controller.issueTripBasedCard("TEST005", "Pupil", -1, 5); // Expired card
        TurnstileController.ValidationResult result = controller.validateCard("TEST005");

        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("Card expired"));
        assertNotNull(result.getCard());
    }

    @Test
    public void testValidateAccumulatingCardSuccess() {
        controller.issueAccumulatingCard("ACC003", 20.0);
        TurnstileController.ValidationResult result = controller.validateCard("ACC003");

        assertTrue(result.isSuccess());
        assertEquals("Access granted", result.getMessage());
        assertNotNull(result.getDetails());
    }

    @Test
    public void testValidateAccumulatingCardInsufficientBalance() {
        controller.issueAccumulatingCard("ACC004", 5.0);
        TurnstileController.ValidationResult result = controller.validateCard("ACC004");

        assertFalse(result.isSuccess());
        assertEquals("Insufficient balance", result.getMessage());
    }

    @Test
    public void testGetGeneralStatistics() {
        // Initial state
        TurnstileController.Statistics stats = controller.getGeneralStatistics();
        assertEquals(0, stats.getTotalPasses());
        assertEquals(0, stats.getTotalDenials());
        assertEquals(0, stats.getTotalAttempts());
        assertEquals(0.0, stats.getSuccessRate(), 0.001);

        // After some operations
        controller.issueTripBasedCard("STAT001", "Regular", 30, 2);
        controller.validateCard("STAT001"); // Success
        controller.validateCard("STAT001"); // Success
        controller.validateCard("STAT001"); // Failure - no trips
        controller.validateCard("NONEXISTENT"); // Failure - not found

        stats = controller.getGeneralStatistics();
        assertEquals(2, stats.getTotalPasses());
        assertEquals(2, stats.getTotalDenials());
        assertEquals(4, stats.getTotalAttempts());
        assertEquals(50.0, stats.getSuccessRate(), 0.001);
    }

    @Test
    public void testGetStatisticsByType() {
        controller.issueTripBasedCard("TYPE001", "Student", 30, 2);
        controller.issueTripBasedCard("TYPE002", "Pupil", 30, 1);
        controller.issueAccumulatingCard("TYPE003", 16.0); // Regular type

        // Perform validations
        controller.validateCard("TYPE001"); // Student - success
        controller.validateCard("TYPE001"); // Student - success
        controller.validateCard("TYPE001"); // Student - failure
        controller.validateCard("TYPE002"); // Pupil - success
        controller.validateCard("TYPE002"); // Pupil - failure
        controller.validateCard("TYPE003"); // Regular - success
        controller.validateCard("TYPE003"); // Regular - success
        controller.validateCard("UNKNOWN"); // Unknown - failure

        Map<String, TurnstileController.TypeStatistics> stats = controller.getStatisticsByType();

        assertTrue(stats.containsKey("Student"));
        assertTrue(stats.containsKey("Pupil"));
        assertTrue(stats.containsKey("Regular"));
        assertTrue(stats.containsKey("Unknown"));

        TurnstileController.TypeStatistics studentStats = stats.get("Student");
        assertEquals(2, studentStats.getPasses());
        assertEquals(1, studentStats.getDenials());
        assertEquals(3, studentStats.getTotalAttempts());

        TurnstileController.TypeStatistics pupilStats = stats.get("Pupil");
        assertEquals(1, pupilStats.getPasses());
        assertEquals(1, pupilStats.getDenials());
        assertEquals(2, pupilStats.getTotalAttempts());

        TurnstileController.TypeStatistics regularStats = stats.get("Regular");
        assertEquals(2, regularStats.getPasses());
        assertEquals(0, regularStats.getDenials());
        assertEquals(2, regularStats.getTotalAttempts());
    }

    @Test
    public void testStatisticsWithNoOperations() {
        Map<String, TurnstileController.TypeStatistics> stats = controller.getStatisticsByType();
        assertTrue(stats.isEmpty());
    }

    @Test
    public void testMultipleCardTypesStatistics() {
        controller.issueTripBasedCard("MULTI001", "Student", 30, 3);
        controller.issueTripBasedCard("MULTI002", "Pupil", 30, 2);
        controller.issueAccumulatingCard("MULTI003", 24.0);

        // Validate each card multiple times
        controller.validateCard("MULTI001"); // Student success
        controller.validateCard("MULTI002"); // Pupil success
        controller.validateCard("MULTI003"); // Regular success
        controller.validateCard("MULTI001"); // Student success
        controller.validateCard("MULTI002"); // Pupil success
        controller.validateCard("MULTI003"); // Regular success

        TurnstileController.Statistics generalStats = controller.getGeneralStatistics();
        assertEquals(6, generalStats.getTotalPasses());
        assertEquals(0, generalStats.getTotalDenials());
        assertEquals(100.0, generalStats.getSuccessRate(), 0.001);
    }

    @Test
    public void testValidationResultObjects() {
        // Test ValidationResult constructor and getters
        TurnstileController.ValidationResult successResult =
                new TurnstileController.ValidationResult(true, "Success", null, "Details");
        TurnstileController.ValidationResult failureResult =
                new TurnstileController.ValidationResult(false, "Failure", null);

        assertTrue(successResult.isSuccess());
        assertEquals("Success", successResult.getMessage());
        assertEquals("Details", successResult.getDetails());

        assertFalse(failureResult.isSuccess());
        assertEquals("Failure", failureResult.getMessage());
        assertNull(failureResult.getDetails());
    }

    @Test
    public void testStatisticsObjects() {
        // Test Statistics constructor and getters
        TurnstileController.Statistics stats = new TurnstileController.Statistics(10, 5, 15, 66.67);

        assertEquals(10, stats.getTotalPasses());
        assertEquals(5, stats.getTotalDenials());
        assertEquals(15, stats.getTotalAttempts());
        assertEquals(66.67, stats.getSuccessRate(), 0.001);
    }

    @Test
    public void testTypeStatisticsObjects() {
        // Test TypeStatistics constructor and getters
        TurnstileController.TypeStatistics typeStats =
                new TurnstileController.TypeStatistics("Student", 8, 2, 10, 80.0);

        assertEquals("Student", typeStats.getType());
        assertEquals(8, typeStats.getPasses());
        assertEquals(2, typeStats.getDenials());
        assertEquals(10, typeStats.getTotalAttempts());
        assertEquals(80.0, typeStats.getSuccessRate(), 0.001);
    }
}