package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {
    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account("ACC_001", 1000);
    }

    @Test
    void testAccountCreation() {
        assertNotNull(account);
        assertEquals("ACC_001", account.getId());
        assertEquals(1000, account.getBalance());
    }

    @Test
    void testAccountCreationWithNegativeBalance() {
        Account acc = new Account("ACC_002", -500);
        assertEquals(0, acc.getBalance());
    }

    @Test
    void testWithdrawValidAmount() {
        account.withdraw(300);
        assertEquals(700, account.getBalance());
    }

    @Test
    void testWithdrawExactBalance() {
        account.withdraw(1000);
        assertEquals(0, account.getBalance());
    }

    @Test
    void testWithdrawNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(-100));
    }

    @Test
    void testWithdrawInsufficientFunds() {
        assertThrows(IllegalArgumentException.class, () -> account.withdraw(1500));
    }

    @Test
    void testDepositValidAmount() {
        account.deposit(500);
        assertEquals(1500, account.getBalance());
    }

    @Test
    void testDepositNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> account.deposit(-100));
    }

    @Test
    void testDepositZeroAmount() {
        account.deposit(0);
        assertEquals(1000, account.getBalance());
    }

    @Test
    void testTryLock() {
        assertTrue(account.tryLock());
        account.unlock();
    }

    @Test
    void testUnlock() {
        account.tryLock();
        assertDoesNotThrow(account::unlock);
    }

    @Test
    void testToString() {
        String toString = account.toString();
        assertTrue(toString.contains("ACC_001"));
        assertTrue(toString.contains("1000"));
    }

    @Test
    void testMultipleOperations() {
        account.deposit(200);
        assertEquals(1200, account.getBalance());

        account.withdraw(300);
        assertEquals(900, account.getBalance());

        account.deposit(100);
        assertEquals(1000, account.getBalance());
    }

    @Test
    void testLockUnlockSequence() {
        assertTrue(account.tryLock());
        account.unlock();
        assertTrue(account.tryLock());
        account.unlock();
    }
}