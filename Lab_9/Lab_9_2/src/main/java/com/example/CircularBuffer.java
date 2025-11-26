package com.example;

import java.util.LinkedList;

public class CircularBuffer<T> {
    private final LinkedList<T> buffer;
    private final int capacity;
    private int head;
    private int tail;
    private int size;

    public CircularBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new LinkedList<>();
        for (int i = 0; i < capacity; i++) {
            buffer.add(null);
        }
        this.head = 0;
        this.tail = 0;
        this.size = 0;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (isFull()) {
            wait();
        }

        buffer.set(tail, item);
        tail = (tail + 1) % capacity;
        size++;

        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while (isEmpty()) {
            wait();
        }

        T item = buffer.get(head);
        head = (head + 1) % capacity;
        size--;

        notifyAll();
        return item;
    }

    public synchronized boolean isEmpty() {
        return size == 0;
    }

    public synchronized boolean isFull() {
        return size == capacity;
    }
}