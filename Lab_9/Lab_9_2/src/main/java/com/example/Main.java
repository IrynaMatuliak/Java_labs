package com.example;

public class Main {
    public static void main(String[] args) {
        CircularBuffer<String> producerBuffer = new CircularBuffer<>(10);
        CircularBuffer<String> translatorBuffer = new CircularBuffer<>(10);

        for (int i = 1; i <= 5; i++) {
            Thread producerThread = new Thread(new Producer(producerBuffer, i + 1));
            producerThread.setDaemon(true);
            producerThread.start();
        }

        for (int i = 1; i <= 2; i++) {
            Thread translatorThread = new Thread(new Translator(producerBuffer, translatorBuffer, i + 1));
            translatorThread.setDaemon(true);
            translatorThread.start();
        }

        try {
            for (int i = 0; i < 100; i++) {
                String message = translatorBuffer.take();
                System.out.println("Main: " + message);
                Thread.sleep(50);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
