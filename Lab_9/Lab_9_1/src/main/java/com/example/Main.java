package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    private static final int ACCOUNT_COUNT = 200;
    private static final int THREAD_COUNT = 5000;
    private static final int MIN_BALANCE = 100;
    private static final int MAX_BALANCE = 10000;

    public static void main(String[] args) throws InterruptedException {
        Bank bank = new Bank();
        List<Account> accounts = createAccounts();

        long totalMoneyBefore = bank.getTotalBalance(accounts);
        System.out.println("Number of accounts: " + accounts.size());
        System.out.println("Number of threads: " + THREAD_COUNT);
        System.out.println("Total amount of money before transfers: " + totalMoneyBefore);

        List<Thread> threads = createTransferThreads(bank, accounts);
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        long totalMoneyAfter = bank.getTotalBalance(accounts);

        System.out.println("\n--- Test results ---");
        System.out.println("Amount of money before transfers: " + totalMoneyBefore);
        System.out.println("Amount of money after transfers: " + totalMoneyAfter);
        if (totalMoneyBefore == totalMoneyAfter) {
            System.out.println("\nTest passed: The total amount in the bank is consistent");
        } else {
            System.out.println("\nTest failed: The total amount in the bank is inconsistent");
        }
        printAccountsInfo(accounts);
    }

    private static List<Account> createAccounts() {
        List<Account> accounts = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < ACCOUNT_COUNT; i++) {
            int balance = MIN_BALANCE + random.nextInt(MAX_BALANCE - MIN_BALANCE + 1);
            accounts.add(new Account("ACC_" + i, balance));
        }
        return accounts;
    }

    private static List<Thread> createTransferThreads(Bank bank, List<Account> accounts) {
        List<Thread> threads = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread thread = new Thread(() -> {
                int fromIndex = random.nextInt(accounts.size());
                Account from = accounts.get(fromIndex);
                int toIndex;
                do {
                    toIndex = random.nextInt(accounts.size());
                } while (fromIndex == toIndex);
                Account to = accounts.get(toIndex);
                int availableAmount = from.getBalance();
                if (availableAmount > 0) {
                    int amount = random.nextInt(availableAmount) + 1;
                    bank.transfer(from, to, amount);
                }
            });
            threads.add(thread);
        }
        return threads;
    }

    private static void printAccountsInfo(List<Account> accounts) {
        System.out.println("\n--- Account information ---");
        boolean hasNegativeBalance = false;
        int negativeAccounts = 0;
        for (Account account : accounts) {
            if (account.getBalance() < 0) {
                hasNegativeBalance = true;
                negativeAccounts++;
            }
        }
        System.out.println("Accounts with a negative balance: " + negativeAccounts);
        System.out.println("All accounts have a non-negative balance: " + !hasNegativeBalance);
    }
}