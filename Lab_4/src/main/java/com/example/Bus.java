package com.example;

public class Bus<T extends Person> extends Vehicle<T> {
    public Bus(int capacity) {
        super(capacity);
    }
}
