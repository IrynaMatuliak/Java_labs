package com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TranslatorTest {
    private CircularBuffer<String> sourceBuffer;
    private CircularBuffer<String> targetBuffer;
    private Translator translator;

    @BeforeEach
    void setUp() {
        sourceBuffer = new CircularBuffer<>(10);
        targetBuffer = new CircularBuffer<>(10);
        translator = new Translator(sourceBuffer, targetBuffer, 1);
    }

    @Test
    void testTranslatorCreation() {
        assertNotNull(translator);
    }

    @Test
    void testTranslatorProcessesMessages() throws InterruptedException {
        sourceBuffer.put("test message");
        Thread translatorThread = new Thread(translator);
        translatorThread.start();
        Thread.sleep(100);
        assertFalse(targetBuffer.isEmpty());
        String translatedMessage = targetBuffer.take();
        assertNotNull(translatedMessage);
        assertTrue(translatedMessage.contains("Thread № 1"));
        assertTrue(translatedMessage.contains("translated the message"));
        assertTrue(translatedMessage.contains("test message"));

        translatorThread.interrupt();
        translatorThread.join(1000);
    }

    @Test
    void testTranslatorMessageFormat() throws InterruptedException {
        sourceBuffer.put("original message");
        Thread translatorThread = new Thread(translator);
        translatorThread.start();
        Thread.sleep(150);
        String translatedMessage = targetBuffer.take();
        assertEquals("Thread № 1 translated the message original message", translatedMessage);

        translatorThread.interrupt();
        translatorThread.join(1000);
    }

    @Test
    void testTranslatorWaitsForMessages() throws InterruptedException {
        Thread translatorThread = new Thread(translator);
        translatorThread.start();
        Thread.sleep(100);
        sourceBuffer.put("new message");
        Thread.sleep(150);
        assertFalse(targetBuffer.isEmpty());
        String translatedMessage = targetBuffer.take();
        assertTrue(translatedMessage.contains("new message"));

        translatorThread.interrupt();
        translatorThread.join(1000);
    }

    @Test
    void testMultipleTranslators() throws InterruptedException {
        Translator translator2 = new Translator(sourceBuffer, targetBuffer, 2);
        sourceBuffer.put("message1");
        sourceBuffer.put("message2");
        Thread thread1 = new Thread(translator);
        Thread thread2 = new Thread(translator2);
        thread1.start();
        thread2.start();
        Thread.sleep(100);
        int messageCount = 0;
        while (!targetBuffer.isEmpty() && messageCount < 3) {
            String message = targetBuffer.take();
            assertTrue(message.contains("translated the message"));
            messageCount++;
        }
        assertEquals(2, messageCount);

        thread1.interrupt();
        thread2.interrupt();
        thread1.join(1000);
        thread2.join(1000);
    }
}