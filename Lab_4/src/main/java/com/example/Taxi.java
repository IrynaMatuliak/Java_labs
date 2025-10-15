package com.example;

public class Taxi<T extends Person> extends Car<T>  {
    public Taxi(int capacity) {
        super(capacity);
    }
}
