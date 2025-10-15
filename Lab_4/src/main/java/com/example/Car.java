package com.example;

public class Car<T extends Person> extends Vehicle<T> {
    public Car(int capacity) {
        super(capacity);
    }
}
