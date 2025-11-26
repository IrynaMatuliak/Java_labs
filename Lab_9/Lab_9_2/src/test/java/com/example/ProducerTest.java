package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.TimeUnit;

class ProducerTest {
    private CircularBuffer<String> buffer;
    private Producer producer;

    @BeforeEach
    void setUp() {
        buffer = new CircularBuffer<>(50);
        producer = new Producer(buffer, 1);
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testProducerGeneratesCorrectNumberOfMessages() throws InterruptedException {
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producerThread.join(4000);
        int messageCount = 0;
        while (!buffer.isEmpty()) {
            String message = buffer.take();
            assertNotNull(message);
            assertTrue(message.contains("Thread № 1"));
            assertTrue(message.contains("generated message"));
            messageCount++;
        }
        assertEquals(20, messageCount, "Producer should generate exactly 20 messages");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testProducerMessageSequence() throws InterruptedException {
        Thread producerThread = new Thread(producer);
        producerThread.start();
        producerThread.join(4000);
        for (int i = 1; i <= 20; i++) {
            String expectedMessage = "Thread № 1 generated message " + i;
            String actualMessage = buffer.take();
            assertEquals(expectedMessage, actualMessage, "Message " + i + " should match expected format");
        }
        assertTrue(buffer.isEmpty(), "Buffer should be empty after reading all messages");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testProducerGeneratesMessages() throws InterruptedException {
        Thread producerThread = new Thread(producer);
        producerThread.start();
        Thread.sleep(500);
        assertFalse(buffer.isEmpty(), "Buffer should not be empty after producer runs");
        int messagesToCheck = 5;
        for (int i = 0; i < messagesToCheck && !buffer.isEmpty(); i++) {
            String message = buffer.take();
            assertNotNull(message, "Message should not be null");
            assertTrue(message.startsWith("Thread № 1 generated message "), "Message should start with correct prefix");
            assertTrue(message.matches("Thread № 1 generated message \\d+"), "Message should match expected pattern");
        }
        producerThread.join(3000);
    }

    @Test
    void testProducerCreation() {
        assertNotNull(producer, "Producer should be created successfully");
    }

    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    void testMultipleProducers() throws InterruptedException {
        // Тестуємо кілька продюсерів одночасно
        Producer producer2 = new Producer(buffer, 2);

        Thread thread1 = new Thread(producer);
        Thread thread2 = new Thread(producer2);

        thread1.start();
        thread2.start();
        thread1.join(4000);
        thread2.join(4000);
        int totalMessages = 0;
        int producer1Messages = 0;
        int producer2Messages = 0;
        while (!buffer.isEmpty()) {
            String message = buffer.take();
            if (message.contains("Thread № 1")) {
                producer1Messages++;
            } else if (message.contains("Thread № 2")) {
                producer2Messages++;
            }
            totalMessages++;
        }
        assertEquals(40, totalMessages, "Two producers should generate 40 messages total");
        assertEquals(20, producer1Messages, "Producer 1 should generate 20 messages");
        assertEquals(20, producer2Messages, "Producer 2 should generate 20 messages");
    }
}