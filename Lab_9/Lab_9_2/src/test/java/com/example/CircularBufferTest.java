package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CircularBufferTest {
    private CircularBuffer<String> buffer;

    @BeforeEach
    void setUp() {
        buffer = new CircularBuffer<>(3);
    }

    @Test
    void testPutAndTake() throws InterruptedException {
        buffer.put("test1");
        buffer.put("test2");
        assertEquals("test1", buffer.take());
        assertEquals("test2", buffer.take());
    }

    @Test
    void testIsEmpty() throws InterruptedException {
        assertTrue(buffer.isEmpty());
        buffer.put("test");
        assertFalse(buffer.isEmpty());

        buffer.take();
        assertTrue(buffer.isEmpty());
    }

    @Test
    void testIsFull() throws InterruptedException {
        assertFalse(buffer.isFull());
        buffer.put("test1");
        buffer.put("test2");
        buffer.put("test3");
        assertTrue(buffer.isFull());

        buffer.take();
        assertFalse(buffer.isFull());
    }

    @Test
    void testCircularBehavior() throws InterruptedException {
        buffer.put("test1");
        buffer.put("test2");
        buffer.put("test3");
        assertEquals("test1", buffer.take());

        buffer.put("test4");
        assertEquals("test2", buffer.take());
        assertEquals("test3", buffer.take());
        assertEquals("test4", buffer.take());
    }

    @Test
    void testBlockingOnEmpty() throws InterruptedException {
        Thread consumerThread = new Thread(() -> {
            try {
                String item = buffer.take();
                assertEquals("test", item);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        consumerThread.start();
        Thread.sleep(100);
        buffer.put("test");
        consumerThread.join(1000);
    }

    @Test
    void testBlockingOnFull() throws InterruptedException {
        buffer.put("test1");
        buffer.put("test2");
        buffer.put("test3");

        Thread producerThread = new Thread(() -> {
            try {
                buffer.put("test4");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producerThread.start();
        Thread.sleep(100);
        buffer.take();
        producerThread.join(1000);
    }
}