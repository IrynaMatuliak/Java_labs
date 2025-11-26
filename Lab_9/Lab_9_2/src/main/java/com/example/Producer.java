package com.example;

public class Producer implements Runnable {
    private final CircularBuffer<String> buffer;
    private final int producerId;

    public Producer(CircularBuffer<String> buffer, int producerId) {
        this.buffer = buffer;
        this.producerId = producerId;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 20; i++) {
                String message = "Thread № " + producerId + " generated message " + (i + 1);
                buffer.put(message);
                System.out.println("Producer " + producerId + ": " + message);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}