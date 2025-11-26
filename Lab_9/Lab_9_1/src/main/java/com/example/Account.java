package com.example;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {
    private final String id;
    private int balance;
    private final Lock lock;

    public Account(String id, int initialBalance) {
        this.id = id;
        this.balance = Math.max(initialBalance, 0);
        this.lock = new ReentrantLock();
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public void withdraw(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (balance < amount) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        balance -= amount;
    }

    public void deposit(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        balance += amount;
    }

    public boolean tryLock() {
        return lock.tryLock();
    }

    public void unlock() {
        lock.unlock();
    }

    @Override
    public String toString() {
        return "Account{id='" + id + "', balance=" + balance + "}";
    }
}