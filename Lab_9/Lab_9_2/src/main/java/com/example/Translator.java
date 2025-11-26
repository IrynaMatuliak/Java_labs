package com.example;

public class Translator implements Runnable {
    private final CircularBuffer<String> sourceBuffer;
    private final CircularBuffer<String> targetBuffer;
    private final int translatorId;

    public Translator(CircularBuffer<String> sourceBuffer, CircularBuffer<String> targetBuffer, int translatorId) {
        this.sourceBuffer = sourceBuffer;
        this.targetBuffer = targetBuffer;
        this.translatorId = translatorId;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                String message = sourceBuffer.take();
                String translatedMessage = "Thread № " + translatorId + " translated the message " + message;
                targetBuffer.put(translatedMessage);
                System.out.println("Translator " + translatorId + ": " + translatedMessage);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}