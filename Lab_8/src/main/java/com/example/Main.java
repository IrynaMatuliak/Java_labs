package com.example;

import java.util.concurrent.ForkJoinPool;
import java.util.Random;

public class Main {
    private static final int ARRAY_SIZE = 1_000_000;
    private static final int MAX_VALUE = 100;

    public static void main(String[] args) {
        int[] array = new int[ARRAY_SIZE];
        Random random = new Random();
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = random.nextInt(MAX_VALUE + 1);
        }

        ArraySum task = new ArraySum(array, 0, array.length);

        try (ForkJoinPool pool = new ForkJoinPool()) {
            long startTime = System.nanoTime();
            long result = pool.invoke(task);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime) / 1_000_000;

            System.out.println("\nCalculation results:");
            System.out.println("Number of threads: " + pool.getParallelism());
            System.out.println("Sum of all array elements: " + result);
            System.out.println("Execution time: " + duration + " ms");
        }
    }
}