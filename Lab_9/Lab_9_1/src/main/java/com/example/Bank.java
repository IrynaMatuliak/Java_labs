package com.example;

import java.util.List;

public class Bank {

    public void transfer(Account from, Account to, int amount) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Accounts cannot be null");
        }
        if (from == to) {
            return;
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        Account firstLock, secondLock;
        if (from.getId().compareTo(to.getId()) < 0) {
            firstLock = from;
            secondLock = to;
        } else {
            firstLock = to;
            secondLock = from;
        }

        boolean firstLockAcquired = false;
        boolean secondLockAcquired = false;

        try {
            firstLockAcquired = firstLock.tryLock();
            if (!firstLockAcquired) {
                return;
            }
            secondLockAcquired = secondLock.tryLock();
            if (!secondLockAcquired) {
                return;
            }
            if (from.getBalance() >= amount) {
                from.withdraw(amount);
                to.deposit(amount);
            }
        } finally {
            if (secondLockAcquired) {
                secondLock.unlock();
            }
            if (firstLockAcquired) {
                firstLock.unlock();
            }
        }
    }

    public long getTotalBalance(List<Account> accounts) {
        long total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }
}