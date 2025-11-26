package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BankTest {
    private Bank bank;
    private Account account1;
    private Account account2;
    private Account account3;

    @BeforeEach
    void setUp() {
        bank = new Bank();
        account1 = new Account("ACC_001", 1000);
        account2 = new Account("ACC_002", 1000);
        account3 = new Account("ACC_003", 1000);
    }

    @Test
    @DisplayName("Transfer between two accounts")
    void testTransferBetweenTwoAccounts() {
        bank.transfer(account1, account2, 500);

        assertEquals(500, account1.getBalance());
        assertEquals(1500, account2.getBalance());
    }

    @Test
    @DisplayName("Transfer with insufficient funds")
    void testTransferWithInsufficientFunds() {
        bank.transfer(account1, account2, 1500);

        assertEquals(1000, account1.getBalance());
        assertEquals(1000, account2.getBalance());
    }

    @Test
    @DisplayName("Transfer to same account")
    void testTransferToSameAccount() {
        bank.transfer(account1, account1, 500);
        assertEquals(1000, account1.getBalance());
    }

    @Test
    @DisplayName("Transfer negative amount")
    void testTransferNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> bank.transfer(account1, account2, -100));
    }

    @Test
    @DisplayName("Transfer zero amount")
    void testTransferZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> bank.transfer(account1, account2, 0));
    }

    @Test
    @DisplayName("Transfer with null accounts")
    void testTransferWithNullAccounts() {
        assertThrows(IllegalArgumentException.class, () -> bank.transfer(null, account2, 100));
        assertThrows(IllegalArgumentException.class, () -> bank.transfer(account1, null, 100));
    }

    @Test
    @DisplayName("Multiple sequential transfers")
    void testMultipleSequentialTransfers() {
        bank.transfer(account1, account2, 300);
        bank.transfer(account2, account3, 200);
        bank.transfer(account3, account1, 100);

        assertEquals(800, account1.getBalance());
        assertEquals(1100, account2.getBalance());
        assertEquals(1100, account3.getBalance());
    }

    @Test
    @DisplayName("Get total balance")
    void testGetTotalBalance() {
        List<Account> accounts = List.of(account1, account2, account3);
        long totalBalance = bank.getTotalBalance(accounts);
        assertEquals(3000, totalBalance);
    }

    @Test
    @DisplayName("Get total balance with empty list")
    void testGetTotalBalanceEmptyList() {
        List<Account> accounts = new ArrayList<>();
        long totalBalance = bank.getTotalBalance(accounts);
        assertEquals(0, totalBalance);
    }

    @Test
    @DisplayName("Concurrent transfers - total balance consistency")
    void testConcurrentTransfersTotalBalanceConsistency() {
        int numberOfAccounts = 10;
        int initialBalance = 1000;
        int numberOfThreads = 20;
        int transfersPerThread = 50;

        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < numberOfAccounts; i++) {
            accounts.add(new Account("ACC_" + i, initialBalance));
        }

        long initialTotalBalance = bank.getTotalBalance(accounts);

        try (ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads)) {
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            for (int i = 0; i < numberOfThreads; i++) {
                executor.execute(() -> {
                    for (int j = 0; j < transfersPerThread; j++) {
                        int fromIndex = (int) (Math.random() * numberOfAccounts);
                        int toIndex = (int) (Math.random() * numberOfAccounts);

                        if (fromIndex != toIndex) {
                            Account from = accounts.get(fromIndex);
                            Account to = accounts.get(toIndex);
                            int amount = Math.min(from.getBalance(), 100);

                            if (amount > 0) {
                                bank.transfer(from, to, amount);
                            }
                        }
                    }
                    latch.countDown();
                });
            }

            boolean completed = latch.await(10, TimeUnit.SECONDS);
            assertTrue(completed, "All transfers should complete within timeout");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test was interrupted", e);
        }

        long finalTotalBalance = bank.getTotalBalance(accounts);
        assertEquals(initialTotalBalance, finalTotalBalance);
    }

    @Test
    @DisplayName("Concurrent transfers - no negative balances")
    void testConcurrentTransfersNoNegativeBalances() {
        int numberOfAccounts = 5;
        int initialBalance = 500;
        int numberOfThreads = 10;
        int transfersPerThread = 100;

        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < numberOfAccounts; i++) {
            accounts.add(new Account("ACC_" + i, initialBalance));
        }

        try (ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads)) {
            CountDownLatch latch = new CountDownLatch(numberOfThreads);

            for (int i = 0; i < numberOfThreads; i++) {
                executor.execute(() -> {
                    for (int j = 0; j < transfersPerThread; j++) {
                        int fromIndex = (int) (Math.random() * numberOfAccounts);
                        int toIndex = (int) (Math.random() * numberOfAccounts);

                        if (fromIndex != toIndex) {
                            Account from = accounts.get(fromIndex);
                            Account to = accounts.get(toIndex);
                            int amount = Math.min(from.getBalance(), 50);

                            if (amount > 0) {
                                bank.transfer(from, to, amount);
                            }
                        }
                    }
                    latch.countDown();
                });
            }

            boolean completed = latch.await(10, TimeUnit.SECONDS);
            assertTrue(completed, "All transfers should complete within timeout");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            fail("Test was interrupted", e);
        }

        for (Account account : accounts) {
            assertTrue(account.getBalance() >= 0, "Account balance should not be negative: " + account);
        }
    }

    @Test
    @DisplayName("Deadlock prevention test")
    void testDeadlockPrevention() throws InterruptedException {
        AtomicInteger completedTransfers = new AtomicInteger(0);
        int numberOfTransfers = 1000;
        CountDownLatch latch = new CountDownLatch(2);

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < numberOfTransfers; i++) {
                bank.transfer(account1, account2, 1);
                completedTransfers.incrementAndGet();
            }
            latch.countDown();
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < numberOfTransfers; i++) {
                bank.transfer(account2, account1, 1);
                completedTransfers.incrementAndGet();
            }
            latch.countDown();
        });

        thread1.start();
        thread2.start();

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        assertTrue(completed, "Threads should complete without deadlock");
        assertEquals(2000, completedTransfers.get());

        long totalBalance = account1.getBalance() + account2.getBalance();
        assertEquals(2000, totalBalance);
    }

    @Test
    @DisplayName("Transfer ordering by ID")
    void testTransferOrderingById() {
        Account accountA = new Account("A", 1000);
        Account accountZ = new Account("Z", 1000);

        bank.transfer(accountZ, accountA, 100);
        bank.transfer(accountA, accountZ, 50);

        assertEquals(1050, accountA.getBalance());
        assertEquals(950, accountZ.getBalance());
    }

    @Test
    @DisplayName("Complex transfer scenario")
    void testComplexTransferScenario() {
        bank.transfer(account1, account2, 200);
        bank.transfer(account2, account3, 300);
        bank.transfer(account3, account1, 100);
        bank.transfer(account2, account1, 150);

        assertEquals(1050, account1.getBalance());
        assertEquals(750, account2.getBalance());
        assertEquals(1200, account3.getBalance());

        long totalBalance = account1.getBalance() + account2.getBalance() + account3.getBalance();
        assertEquals(3000, totalBalance);
    }

    @Test
    @DisplayName("Transfer with maximum amount")
    void testTransferWithMaximumAmount() {
        bank.transfer(account1, account2, 1000);
        assertEquals(0, account1.getBalance());
        assertEquals(2000, account2.getBalance());
    }

    @Test
    @DisplayName("Multiple transfers from same account")
    void testMultipleTransfersFromSameAccount() {
        bank.transfer(account1, account2, 100);
        bank.transfer(account1, account3, 200);
        bank.transfer(account1, account2, 300);

        assertEquals(400, account1.getBalance());
        assertEquals(1400, account2.getBalance());
        assertEquals(1200, account3.getBalance());
    }

    @Test
    @DisplayName("Rapid small transfers")
    void testRapidSmallTransfers() {
        try (ExecutorService executor = Executors.newFixedThreadPool(4)) {
            for (int i = 0; i < 100; i++) {
                executor.execute(() -> bank.transfer(account1, account2, 1));
                executor.execute(() -> bank.transfer(account2, account1, 1));
            }
        }

        long totalBalance = account1.getBalance() + account2.getBalance();
        assertEquals(2000, totalBalance);
    }
}